package cache.demo.repository;

import cache.demo.entities.RoleEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

  Optional<RoleEntity> findRoleEntityByCodeName(String codeName);

  Set<RoleEntity> findRoleEntitiesByCodeNameIn(List<String> codeName);
}
