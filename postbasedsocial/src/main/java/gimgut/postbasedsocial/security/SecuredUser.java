package gimgut.postbasedsocial.security;

import gimgut.postbasedsocial.api.user.UserInfo;

public interface SecuredUser {
    String getPassword();

    UserInfo getUserInfo();
}
