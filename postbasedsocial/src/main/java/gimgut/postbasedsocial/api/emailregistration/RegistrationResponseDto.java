package gimgut.postbasedsocial.api.emailregistration;

public class RegistrationResponseDto {

    private final RegistrationResponseStatus status;

    public RegistrationResponseDto(RegistrationResponseStatus status) {
        this.status = status;
    }

    public RegistrationResponseStatus getStatus() {
        return status;
    }
}

