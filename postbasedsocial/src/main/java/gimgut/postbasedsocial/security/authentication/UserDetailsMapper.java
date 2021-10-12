package gimgut.postbasedsocial.security.authentication;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    UserDetailsDto toUserDetailsDto(UserDetailsImpl userDetails);
}