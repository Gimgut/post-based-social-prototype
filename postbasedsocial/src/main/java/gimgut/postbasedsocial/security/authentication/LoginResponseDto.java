package gimgut.postbasedsocial.security.authentication;

public class LoginResponseDto {
    private LoginResponseStatus status;
    private UserDetailsDto user;

    public LoginResponseDto(LoginResponseStatus status, UserDetailsDto userDetailsDto) {
        this.status = status;
        this.user = userDetailsDto;
    }

    public LoginResponseStatus getStatus() {
        return status;
    }

    public void setStatus(LoginResponseStatus status) {
        this.status = status;
    }

    public UserDetailsDto getUser() {
        return user;
    }

    public void setUser(UserDetailsDto user) {
        this.user = user;
    }
}
