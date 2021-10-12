package gimgut.postbasedsocial.security.registration;

public class RegistrationResponseDto {

    private final String status;

    public RegistrationResponseDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

