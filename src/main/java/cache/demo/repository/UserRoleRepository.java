package cache.demo.repository;

import cache.demo.entities.UserRoleEntity;
import cache.demo.entities.UserRoleId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleId> {

  int deleteByUserId(Long userId);

  Optional<UserRoleEntity> findByUserIdAndRoleId(Long userId, Integer roleId);

}
