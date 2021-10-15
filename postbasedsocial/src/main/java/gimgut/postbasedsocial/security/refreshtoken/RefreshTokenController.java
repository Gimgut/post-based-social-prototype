package gimgut.postbasedsocial.security.refreshtoken;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.security.JwtService;
import gimgut.util.Triplet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/auth/refresh_token")
public class RefreshTokenController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final JwtService jwtService;

    public RefreshTokenController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(HttpServletResponse request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Refresh ")) {
            try {
                String refreshToken = authorizationHeader.substring(8);
                Triplet<String, String, UserInfo> tokens = jwtService.refreshToken(refreshToken);
                if (tokens != null) {
                    logger.info("Refresh token success");
                    return new ResponseEntity<>(
                            new RefreshTokenResponseDto(
                                    RefreshTokenStatus.SUCCESS,
                                    tokens.getThird(),
                                    tokens.getFirst(),
                                    tokens.getSecond()),
                                    HttpStatus.OK
                    );
                } else {
                    logger.error("Refresh token error");
                    return new ResponseEntity<>(new RefreshTokenResponseDto(RefreshTokenStatus.FAILED), HttpStatus.OK);
                }
            } catch (Exception e) {
                logger.info("Refresh token failed");
                return new ResponseEntity<>(new RefreshTokenResponseDto(RefreshTokenStatus.FAILED), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new RefreshTokenResponseDto(RefreshTokenStatus.BAD_REQUEST), HttpStatus.OK);
        }
    }
}
