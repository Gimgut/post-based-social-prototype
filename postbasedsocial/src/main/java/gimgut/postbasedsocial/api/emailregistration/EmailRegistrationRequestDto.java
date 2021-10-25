package gimgut.postbasedsocial.api.emailregistration;

import org.jsoup.Jsoup;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EmailRegistrationRequestDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9а-яА-Я-_]{4,32}")
    private String username;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9а-яА-Я-_]{8,32}")
    private String password;
    //Advanced: Checks that a password has a minimum of 6 characters, at least 1 uppercase letter, 1 lowercase letter, and 1 number with no spaces.
    //^((?=\S*?[A-Z])(?=\S*?[a-z])(?=\S*?[0-9]).{6,})\S$

    public EmailRegistrationRequestDto(String email, String username, String password) {
        this.email = email.trim();
        this.username = username.trim();
        this.password = password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

