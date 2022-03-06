package gimgut.postbasedsocial.security.oauth2;

public class GoogleRegistrationException extends RuntimeException {
    public GoogleRegistrationException(String errorMessage) {
        super(errorMessage);
    }

    public GoogleRegistrationException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
