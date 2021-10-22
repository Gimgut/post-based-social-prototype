package gimgut.postbasedsocial.api.emailregistration;

public class RegistrationResponseDto {

    private final String status;

    public RegistrationResponseDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

