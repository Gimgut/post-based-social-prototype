package gimgut.postbasedsocial.api.emailregistration;

public enum RegistrationResponseStatus {
    SUCCESS, FAILED,
    EMAIL_EXISTS, USERNAME_EXISTS,
    BAD_EMAIL, BAD_USERNAME, BAD_PASSWORD
}

