package gimgut.postbasedsocial.security.registration;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserCredentialsEmailRegistrationMapper {

    @Mapping(target = "userInfo.username", source = "username")
    UserCredentialsEmailRegistration toUserCredentialsEmailRegistration(EmailRegistrationRequestDto emailRegistrationRequestDto);
}
