package gimgut.postbasedsocial.security;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    GUEST, READER, WRITER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}

