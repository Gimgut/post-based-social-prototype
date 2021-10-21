package gimgut.postbasedsocial.security.refreshtoken;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoMapper;
import gimgut.postbasedsocial.security.JwtService;
import gimgut.util.Triplet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/refresh_token")
public class RefreshTokenController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final JwtService jwtService;
    private final UserInfoMapper userInfoMapper;

    public RefreshTokenController(JwtService jwtService, UserInfoMapper userInfoMapper) {
        this.jwtService = jwtService;
        this.userInfoMapper = userInfoMapper;
    }

    @PostMapping("")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@RequestBody String refreshToken) {
        try {
            Triplet<String, String, UserInfo> tokens = jwtService.refreshToken(refreshToken);
            if (tokens != null) {
                logger.info("Refresh token success");
                return new ResponseEntity<>(
                        new RefreshTokenResponseDto(
                                RefreshTokenStatus.SUCCESS,
                                userInfoMapper.toUserInfoDto(tokens.getThird()),
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
    }
}
