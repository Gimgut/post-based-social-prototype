package gimgut.postbasedsocial.security.registration;

import gimgut.postbasedsocial.api.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserCredentialsEmailRegistrationMapper {

    @Mapping(target = "userInfo.username", source = "username")
    UserCredentialsEmailRegistration toUserCredentialsEmailRegistration(RegistrationRequestDto registrationRequestDto);
}
