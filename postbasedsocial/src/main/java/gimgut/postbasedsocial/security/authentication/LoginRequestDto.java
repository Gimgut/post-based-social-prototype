package gimgut.postbasedsocial.security.authentication;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class LoginRequestDto {

    @Email
    private String email;
    @Size(min = 4, max = 256)
    private String password;

    public LoginRequestDto() {
    }

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}