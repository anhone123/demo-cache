package cache.demo.repository;

import cache.demo.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  @EntityGraph(attributePaths = {"roles"})
  Optional<UserEntity> findUserEntityByUserIdAndUserPassword(String userId, String userPassword);

  @EntityGraph(attributePaths = {"roles"})
  Optional<UserEntity> findUserEntityByUserId(String userId);

}
