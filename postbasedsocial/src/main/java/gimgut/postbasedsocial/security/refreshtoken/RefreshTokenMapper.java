package gimgut.postbasedsocial.security.refreshtoken;

import gimgut.postbasedsocial.api.user.UserInfoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserInfoMapper.class)
public interface RefreshTokenMapper {

    RefreshTokenResponseDto toDto(RefreshToken refreshToken);
}
