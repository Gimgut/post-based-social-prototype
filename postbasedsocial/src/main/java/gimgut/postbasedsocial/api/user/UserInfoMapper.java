package gimgut.postbasedsocial.api.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {

    @Mapping(target = "role", source = "userInfo.role.name")
    UserInfoDto toDto(UserInfo userInfo);

    List<UserInfoDto> toListDto(List<UserInfo> users);
}
