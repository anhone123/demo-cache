package cache.demo.repository;

import cache.demo.dto.user.UserDto;
import cache.demo.dto.user.UserProjection;
import cache.demo.entities.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  @EntityGraph(attributePaths = {"roles"})
  Optional<UserEntity> findUserEntityByUserIdAndUserPassword(String userId, String userPassword);

  @EntityGraph(attributePaths = {"roles"})
  Optional<UserEntity> findUserEntityByUserId(String userId);

//  @EntityGraph(attributePaths = {"roles"})
  Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);

  @EntityGraph(attributePaths = {"roles"})
  List<UserEntity> findAllByIdIn(Set<Long> ids, Sort sort);

//  @Query(value = "select new cache.demo.dto.user.UserDto(userEntity.id, userEntity.user_id, userEntity.first_name, userEntity.last_name, userEntity.email, STRING_AGG(roleEntity.name, ', '))"
////      + "    userEntity.id as id,"
////      + "    userEntity.user_id as userId,"
////      + "    userEntity.first_name as firstName,"
////      + "    userEntity.last_name as lastName,"
////      + "    userEntity.email as email,"
////      + "    STRING_AGG(roleEntity.name, ', ') roleNames "
//      + "from"
//      + "    test_user userEntity left outer join test_user_role user_role_entity"
//      + "        on userEntity.id=user_role_entity.user_id"
//      + "        left outer join test_role roleEntity"
//      + "            on user_role_entity.role_id=roleEntity.id "
//      + "where"
//      + "        (:keyword IS NULL "
//      + "            or lower(userEntity.first_name) like :keyword"
//      + "            or lower(userEntity.last_name) like :keyword"
//      + "            or lower(userEntity.email) like :keyword)"
//      + "group by userEntity.id "
//      + "order by"
//      + "    userEntity.id asc "
////      + "limit (:limit) offset (:offset)"
//  )
//  List<UserDto> findUserWithNativeQuery(@Param("keyword") String keyword
////      ,
////      @Param("limit") int limit,
////      @Param("offset") long offset
//  );

  @Query(value = "SELECT "
      + "user.id AS id, "
      + "user.userId AS userId, "
      + "user.firstName AS firstName, "
      + "user.lastName AS lastName, "
      + "user.email AS email,  "
      + "STRING_AGG(roles.name, ', ') AS roleNames "
      + "FROM UserEntity user "
      + "INNER JOIN user.roles roles "
      + "WHERE "
      + "    (:keyword is null "
      + "     or lower(user.firstName) like :keyword "
      + "     or lower(user.lastName) like :keyword "
      + "     or lower(user.email) like :keyword) "
      + "GROUP BY user.id "
      + "ORDER BY "
      + "user.id asc "
//      + "limit (:limit) offset (:offset)"
  )
  Page<UserProjection> findUserWithNativeQueryReturnInterface(@Param("keyword") String keyword,
      Pageable pageable);

  @Query(nativeQuery = true ,value = "select"
      + "    userEntity.id as id,"
      + "    userEntity.user_id as userId,"
      + "    userEntity.first_name as firstName,"
      + "    userEntity.last_name as lastName,"
      + "    userEntity.email as email,"
      + "    STRING_AGG(roleEntity.name, ', ') as roleNames "
      + "from"
      + "    test_user userEntity left outer join test_user_role user_role_entity"
      + "        on userEntity.id = user_role_entity.user_id"
      + "        left outer join test_role roleEntity"
      + "            on user_role_entity.role_id = roleEntity.id "
      + "where"
      + "        (:keyword = 'null' "
      + "            or lower(userEntity.first_name) like :keyword"
      + "            or lower(userEntity.last_name) like :keyword"
      + "            or lower(userEntity.email) like :keyword)"
      + "group by userEntity.id "
      + "order by"
      + "    userEntity.id asc "
      + "limit (:limit) offset (:offset)"
  )
  List<UserProjection> findUserWithNativeQueryReturnInterface(@Param("keyword") String keyword,
      @Param("limit") int limit,
      @Param("offset") long offset
  );
}
