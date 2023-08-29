package cache.demo.service.user.mapper;

import cache.demo.dto.role.RolePayload;
import cache.demo.dto.user.GetUserResponsePayload;
import cache.demo.entities.RoleEntity;
import cache.demo.entities.UserEntity;
import cache.demo.utils.CommonUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  GetUserResponsePayload entityToGetUserResponsePayload(UserEntity source);

  default List<GetUserResponsePayload> entitiesToGetUserResponsePayloads(List<UserEntity> sources) {
    if (sources == null) {
      return Collections.emptyList();
    }

    List<GetUserResponsePayload> list = new ArrayList<>(sources.size());
    for (UserEntity userEntity : sources) {
      GetUserResponsePayload response = entityToGetUserResponsePayload(userEntity);
      response.setCreatedDate(CommonUtils.convertUTCDate(userEntity.getCreatedDate()));
      response.setModifiedDate(CommonUtils.convertUTCDate(userEntity.getModifiedDate()));
      list.add(response);
    }

    return list;
  }

  default RolePayload mapRoleObjectToRolePayload(RoleEntity role) {
    return RolePayload.builder()
        .name(role.getName())
        .id(role.getId())
        .description(role.getDescription())
        .build();
  }


  default Integer mapRoleObjectToId(RoleEntity role) {
    return role.getId();
  }

}
