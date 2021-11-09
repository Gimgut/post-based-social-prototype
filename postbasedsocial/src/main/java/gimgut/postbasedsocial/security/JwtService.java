package gimgut.postbasedsocial.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import gimgut.postbasedsocial.api.emailregistration.UserCredentialsEmailRepository;
import gimgut.postbasedsocial.security.oauth2.UserCredentialsGoogleRepository;
import gimgut.postbasedsocial.security.refreshtoken.RefreshTokenResponse;
import gimgut.postbasedsocial.security.refreshtoken.RefreshTokenStatus;
import gimgut.postbasedsocial.security.refreshtoken.TypeConversionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final String secret;
    private final long accessTokenExpiryTimeMs;
    private final long refreshTokenExpiryTimeMs;
    private final ObjectMapper mapper;
    private final UserCredentialsEmailRepository userCredentialsEmailRepository;
    private final UserCredentialsGoogleRepository userCredentialsGoogleRepository;

    private final Algorithm accessTokenAlgorithm;
    private final JWTVerifier accessTokenVerifier;
    private final Base64.Decoder decoder;

    public JwtService(@Value("${app.security.secret}") String secret,
                      @Value("${app.security.accessTokenExpiryTimeMs}") Long accessTokenExpiryTimeMs,
                      @Value("${app.security.refreshTokenExpiryTimeMs}") Long refreshTokenExpiryTimeMs,
                      ObjectMapper mapper,
                      UserCredentialsEmailRepository userCredentialsEmailRepository,
                      UserCredentialsGoogleRepository userCredentialsGoogleRepository) {
        this.secret = secret;
        this.accessTokenExpiryTimeMs = accessTokenExpiryTimeMs;
        this.refreshTokenExpiryTimeMs = refreshTokenExpiryTimeMs;
        this.mapper = mapper;
        this.userCredentialsEmailRepository = userCredentialsEmailRepository;
        this.userCredentialsGoogleRepository = userCredentialsGoogleRepository;

        this.accessTokenAlgorithm = Algorithm.HMAC256(secret);
        this.accessTokenVerifier = JWT.require(this.accessTokenAlgorithm).build();
        this.decoder = Base64.getDecoder();
    }

    public DecodedJWT verifyAccessToken(String accessToken) throws JWTVerificationException {
        return accessTokenVerifier.verify(accessToken);
    }

    private DecodedJWT verifyRefreshToken(String refreshToken, String password) throws JWTVerificationException {
        JWTVerifier refreshTokenVerifier = JWT.require(Algorithm.HMAC256(secret + password)).build();
        return refreshTokenVerifier.verify(refreshToken);
    }

    public Tokens getAccessAndRefreshTokens(SecuredUser securedUser) {
        return getAccessAndRefreshTokens(securedUser, AuthenticationType.GOOGLE);
    }

    public Tokens getAccessAndRefreshTokens(SecuredUser securedUser, AuthenticationType authenticationType) {
        String accessToken = getAccessToken(securedUser, authenticationType);
        String refreshToken = getRefreshToken(securedUser, authenticationType);
        return new Tokens(accessToken, refreshToken);
    }

    public String getAccessToken(SecuredUser securedUser, AuthenticationType authenticationType) {
        String accessToken = JWT.create()
                .withSubject(securedUser.getUserInfo().getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiryTimeMs))
                .withPayload(Map.of(
                        "role", securedUser.getUserInfo().getRole().getName(),
                        "ap", authenticationType.name(), //ap - authentication provider/type
                        "uiid", securedUser.getUserInfo().getId()
                ))
                .sign(accessTokenAlgorithm);

        return accessToken;
    }

    public String getRefreshToken(SecuredUser securedUser, AuthenticationType authenticationType) {
        Algorithm refreshTokenAlgorithm = Algorithm.HMAC256(secret + securedUser.getPassword());

        String refreshToken = JWT.create()
                .withSubject(securedUser.getUserInfo().getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiryTimeMs))
                .withPayload(Map.of(
                        "role", securedUser.getUserInfo().getRole().getName(),
                        "ap", authenticationType.name(), //ap - authentication provider/type
                        "uiid", securedUser.getUserInfo().getId(),
                        "aexp", this.accessTokenExpiryTimeMs //access token expiry time
                ))
                .sign(refreshTokenAlgorithm);
        return refreshToken;
    }

    @Transactional(readOnly = true)
    public RefreshTokenResponse refreshToken(String refreshToken) {
        String[] chunks = refreshToken.split("\\.");
        //TODO: make payload as class?
        Map<String, String> payload = this.payloadToMap(chunks[1]);
        if (payload.isEmpty()) {
            return new RefreshTokenResponse(RefreshTokenStatus.BAD_TOKEN);
        }

        Long userInfoId = this.objectToLong(payload.get("uiid"));
        AuthenticationType authenticationType = AuthenticationType.valueOf(payload.get("ap"));
        SecuredUser securedUser = this.findUserByAuthenticationType(userInfoId, authenticationType);
        if (securedUser == null) {
            return new RefreshTokenResponse(RefreshTokenStatus.USER_NOT_FOUND);
        }

        try {
            verifyRefreshToken(refreshToken, securedUser.getPassword());
        } catch (Exception e) {
            return new RefreshTokenResponse(RefreshTokenStatus.VERIFICATION_FAILED);
        }

        String newAccessToken = this.getAccessToken(securedUser, authenticationType);
        String newRefreshToken = this.getRefreshToken(securedUser, authenticationType);
        RefreshTokenResponse refreshResponse = new RefreshTokenResponse();
        refreshResponse.setStatus(RefreshTokenStatus.SUCCESS);
        refreshResponse.setAccessToken(newAccessToken);
        refreshResponse.setRefreshToken(newRefreshToken);
        refreshResponse.setUserInfo(securedUser.getUserInfo());
        return refreshResponse;
    }

    @Transactional(readOnly = true)
    private SecuredUser findUserByAuthenticationType(Long userInfoId, AuthenticationType authenticationType) {
        if (authenticationType == AuthenticationType.EMAIL) {
            return userCredentialsEmailRepository.findByUserInfoId_Eager(userInfoId);
        } else if (authenticationType == AuthenticationType.GOOGLE) {
            return userCredentialsGoogleRepository.findByUserInfoId_JoinFetchInfo(userInfoId);
        }
        return null;
    }

    private Map<String, String> payloadToMap(String payload) {
        try {
            return mapper.readValue(decoder.decode(payload), HashMap.class);
        } catch (IOException e) {
            return Map.of();
        }
    }

    private Long objectToLong(Object object) {
        if (object.getClass() == Integer.class) {
            return ((Integer) object).longValue();
        } else if (object.getClass() == Long.class) {
            return (Long) object;
        } else {
            throw new TypeConversionException("Failed to convert Object to Long");
        }
    }
}
