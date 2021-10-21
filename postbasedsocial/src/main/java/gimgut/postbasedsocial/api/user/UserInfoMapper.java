package gimgut.postbasedsocial.api.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {

    @Mapping(target = "role", source = "userInfo.role.name")
    UserInfoDto toUserInfoDto(UserInfo userInfo);
}
