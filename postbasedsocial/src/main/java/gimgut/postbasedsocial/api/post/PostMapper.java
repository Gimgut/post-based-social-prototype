package gimgut.postbasedsocial.api.post;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoDto;
import gimgut.postbasedsocial.api.user.UserInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserInfoMapper.class)
public interface PostMapper {

    PostDto toPostDto(Post post);
}
