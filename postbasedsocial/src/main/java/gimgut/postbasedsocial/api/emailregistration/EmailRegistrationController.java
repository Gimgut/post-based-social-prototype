package gimgut.postbasedsocial.api.emailregistration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class EmailRegistrationController {

    private final EmailRegistrationService emailRegistrationService;
    private final UserCredentialsEmailRegistrationMapper userCredentialsEmailRegistrationMapper;

    public EmailRegistrationController(EmailRegistrationService emailRegistrationService, UserCredentialsEmailRegistrationMapper userCredentialsEmailRegistrationMapper) {
        this.emailRegistrationService = emailRegistrationService;
        this.userCredentialsEmailRegistrationMapper = userCredentialsEmailRegistrationMapper;
    }

    @PostMapping("signup")
    public ResponseEntity<RegistrationResponseDto> registerNewUser(@RequestBody EmailRegistrationRequestDto emailRegistrationRequestDto) {
        UserCredentialsEmailRegistration user = userCredentialsEmailRegistrationMapper.toUserCredentialsEmailRegistration(emailRegistrationRequestDto);
        RegistrationResponseDto registrationResponseDto = new RegistrationResponseDto(emailRegistrationService.registerNewUser(user).name());
        return new ResponseEntity(registrationResponseDto, HttpStatus.OK);
    }
}
