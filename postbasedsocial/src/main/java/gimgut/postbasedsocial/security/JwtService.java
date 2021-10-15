package gimgut.postbasedsocial.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.security.authentication.UserDetailsImpl;
import gimgut.postbasedsocial.security.oauth2.UserCredentialsGoogleRegistration;
import gimgut.postbasedsocial.security.oauth2.UserCredentialsGoogleRepository;
import gimgut.postbasedsocial.security.registration.UserCredentialsEmailRepository;
import gimgut.util.Pair;
import gimgut.util.Triplet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

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
                      @Value("${app.security.refreshTokenExpiryTimeMs}") Long refreshTokenExpiryTimeMs, ObjectMapper mapper, UserCredentialsEmailRepository userCredentialsEmailRepository, UserCredentialsGoogleRepository userCredentialsGoogleRepository) {
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

    public DecodedJWT verifyRefreshToken(String refreshToken, String password) throws JWTVerificationException {
        JWTVerifier refreshTokenVerifier = JWT.require(Algorithm.HMAC256(secret + password)).build();
        return refreshTokenVerifier.verify(refreshToken);
    }

    public Pair<String, String> getAccessAndRefreshTokens(UserCredentialsGoogleRegistration userCredentialsGoogleRegistration) {
        String role = userCredentialsGoogleRegistration.getUserInfo().getRole().getName();

        String access_token = getAccessToken(
                userCredentialsGoogleRegistration.getUserInfo().getUsername(),
                role,
                userCredentialsGoogleRegistration.getUserInfo().getId(),
                AuthenticationType.GOOGLE);
        String refresh_token = getRefreshToken(
                userCredentialsGoogleRegistration.getUserInfo().getUsername(),
                userCredentialsGoogleRegistration.getPassword(),
                role,
                userCredentialsGoogleRegistration.getUserInfo().getId(),
                AuthenticationType.GOOGLE);

        return new Pair<>(access_token, refresh_token);
    }

    public Pair<String, String> getAccessAndRefreshTokens(UserDetailsImpl userDetails, AuthenticationType authenticationType) {
        //TODO: check these 2 implementations
        //String role = userDetails.getAuthorities().stream().toList().get(0).toString();
        String role = userDetails.getAuthorities().toArray()[0].toString();

        String access_token = getAccessToken(userDetails.getUsername(), role, userDetails.getId(), authenticationType);
        String refresh_token = getRefreshToken(userDetails.getUsername(), userDetails.getPassword(), role, userDetails.getId(), authenticationType);

        return new Pair<>(access_token, refresh_token);
    }

    /**
     * @param username
     * @param role
     * @param uiid UserInfo id
     * @param authenticationType
     * @return
     */
    public String getAccessToken(String username, String role, Long uiid, AuthenticationType authenticationType) {
        String access_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiryTimeMs))
                .withPayload(Map.of(
                        "role", role,
                        "ap", authenticationType.name(), //ap - authentication provider/type
                        "uiid", uiid
                ))
                .sign(accessTokenAlgorithm);

        return access_token;
    }

    /**
     * @param username
     * @param password
     * @param role
     * @param uiid UserInfo id
     * @param authenticationType
     * @return
     */
    public String getRefreshToken(String username, String password, String role, Long uiid, AuthenticationType authenticationType) {
        Algorithm refreshTokenAlgorithm = Algorithm.HMAC256(secret + password);

        String refresh_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiryTimeMs))
                .withPayload(Map.of(
                        "role", role,
                        "ap", authenticationType.name(), //ap - authentication provider/type
                        "uiid", uiid
                ))
                .sign(refreshTokenAlgorithm);
        return refresh_token;
    }

    /**
     * @param refreshToken
     * @return Pair<access_token, refresh_token>, or null if error
     */
    @Transactional(readOnly = true)
    public Triplet<String, String, UserInfo> refreshToken(String refreshToken) {
        if (refreshToken == null)
            return null;
        String[] chunks = refreshToken.split("\\.");
        Map<String, String> payload;
        try {
            payload = mapper.readValue(decoder.decode(chunks[1]), HashMap.class);
            Long uiid = Long.parseLong(payload.get("uiid"));
            AuthenticationType authenticationType = AuthenticationType.valueOf(payload.get("ap"));
            SecuredUser securedUser;
            switch (authenticationType) {
                case EMAIL -> securedUser = userCredentialsEmailRepository.findByUserInfoId_Eager(uiid);
                case GOOGLE -> securedUser = userCredentialsGoogleRepository.findByUserInfoId_Eager(uiid);
                default -> {
                    return null;
                }
            }

            verifyRefreshToken(refreshToken, securedUser.getPassword());

            String access_token = getAccessToken(
                    securedUser.getUserInfo().getUsername(),
                    securedUser.getUserInfo().getRole().getName(),
                    securedUser.getUserInfo().getId(),
                    authenticationType);

            String refresh_token = getRefreshToken(
                    securedUser.getUserInfo().getUsername(),
                    securedUser.getPassword(),
                    securedUser.getUserInfo().getRole().getName(),
                    securedUser.getUserInfo().getId(),
                    authenticationType);

            return new Triplet<String, String, UserInfo>(access_token, refresh_token, securedUser.getUserInfo());
        } catch (Exception e) {
            return null;
        }
    }
}
