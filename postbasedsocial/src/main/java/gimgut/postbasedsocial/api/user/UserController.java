package gimgut.postbasedsocial.api.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserInfoRepository userInfoRepository;
    private final UserInfoMapper userInfoMapper;

    public UserController(UserInfoRepository userInfoRepository, UserInfoMapper userInfoMapper) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoMapper = userInfoMapper;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity getUserByName(@PathVariable @NotNull String name) {
        UserInfo userInfo = userInfoRepository.findByUsername(name);
        if (userInfo == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        UserInfoDto userDto = userInfoMapper.toUserInfoDto(userInfo);
        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity getUserById(@PathVariable @NotNull Long id) {
        Optional<UserInfo> userInfo = userInfoRepository.findById(id);
        if (!userInfo.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        UserInfoDto userDto = userInfoMapper.toUserInfoDto(userInfo.get());
        return new ResponseEntity(userDto, HttpStatus.OK);
    }


}
