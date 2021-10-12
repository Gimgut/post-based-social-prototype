package gimgut.postbasedsocial.security.registration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserCredentialsEmailRegistrationMapper userCredentialsEmailRegistrationMapper;

    public RegistrationController(RegistrationService registrationService, UserCredentialsEmailRegistrationMapper userCredentialsEmailRegistrationMapper) {
        this.registrationService = registrationService;
        this.userCredentialsEmailRegistrationMapper = userCredentialsEmailRegistrationMapper;
    }

    @PostMapping("signup")
    public ResponseEntity<RegistrationResponseDto> registerNewUser(@RequestBody RegistrationRequestDto registrationRequestDto) {
        UserCredentialsEmailRegistration user = userCredentialsEmailRegistrationMapper.toUserCredentialsEmailRegistration(registrationRequestDto);
        RegistrationResponseDto registrationResponseDto = new RegistrationResponseDto(registrationService.registerNewUser(user).name());
        return new ResponseEntity(registrationResponseDto, HttpStatus.OK);
    }
}
