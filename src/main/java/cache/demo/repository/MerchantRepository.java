package cache.demo.repository;

import cache.demo.entities.MerchantEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

  Optional<MerchantEntity> findMerchantEntityByMerchantId(String merchantId);

}
