package gimgut.postbasedsocial.security.refreshtoken;

import gimgut.postbasedsocial.api.user.UserInfoMapper;
import gimgut.postbasedsocial.security.JwtService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("api/v1/auth/refresh_token")
public class RefreshTokenController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final JwtService jwtService;
    private final UserInfoMapper userInfoMapper;
    private final RefreshTokenMapper refreshTokenMapper;

    public RefreshTokenController(JwtService jwtService, UserInfoMapper userInfoMapper, RefreshTokenMapper refreshTokenMapper) {
        this.jwtService = jwtService;
        this.userInfoMapper = userInfoMapper;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @PostMapping("")
    public ResponseEntity refreshToken(@RequestBody @NotEmpty String refreshToken) {
        RefreshTokenResponse tokens;
        try {
            tokens = jwtService.refreshToken(refreshToken);
        } catch (Exception e) {
            return new ResponseEntity<>(RefreshTokenStatus.FAILED, HttpStatus.BAD_REQUEST);
        }
        if (tokens.getStatus() == RefreshTokenStatus.SUCCESS) {
            RefreshTokenResponseDto responseDto = refreshTokenMapper.toDto(tokens);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(tokens.getStatus(), HttpStatus.BAD_REQUEST);
        }
    }
}
