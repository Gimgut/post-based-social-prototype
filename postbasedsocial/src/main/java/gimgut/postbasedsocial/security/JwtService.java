package gimgut.postbasedsocial.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import gimgut.postbasedsocial.security.authentication.UserDetailsImpl;
import gimgut.postbasedsocial.security.oauth2.UserCredentialsGoogleRegistration;
import gimgut.util.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final String secret;
    private final long accessTokenExpiryTimeMs;
    private final long refreshTokenExpiryTimeMs;

    private final Algorithm accessTokenAlgorithm;
    private final JWTVerifier accessTokenVerifier;

    public JwtService(@Value("${app.security.secret}") String secret,
                      @Value("${app.security.accessTokenExpiryTimeMs}") Long accessTokenExpiryTimeMs,
                      @Value("${app.security.refreshTokenExpiryTimeMs}") Long refreshTokenExpiryTimeMs) {
        this.secret = secret;
        this.accessTokenExpiryTimeMs = accessTokenExpiryTimeMs;
        this.refreshTokenExpiryTimeMs = refreshTokenExpiryTimeMs;

        this.accessTokenAlgorithm = Algorithm.HMAC256(secret);
        this.accessTokenVerifier = JWT.require(this.accessTokenAlgorithm).build();
    }

    public DecodedJWT verifyAccessToken(String accessToken) throws JWTVerificationException {
        return accessTokenVerifier.verify(accessToken);
    }

    public Pair<String, String> getAccessRefreshTokens(UserCredentialsGoogleRegistration userCredentialsGoogleRegistration) {
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

    public Pair<String, String> getAccessRefreshTokens(UserDetailsImpl userDetails, AuthenticationType authenticationType) {
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
}
