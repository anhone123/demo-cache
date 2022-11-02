package cache.demo.repository;

import cache.demo.entities.TransactionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

  Optional<TransactionEntity> findTransactionEntityById(Long id);

  @Query("SELECT t FROM TransactionEntity t " +
      "LEFT JOIN FETCH t.merchantEntity m " +
      "WHERE (:foreignMerchantId IS NULL OR t.foreignMerchantId = :foreignMerchantId) " +
      "ORDER BY t.foreignMerchantId")
  List<TransactionEntity> findTransactionEntitiesByForeignMerchantId(Long foreignMerchantId);

  @Query("SELECT COUNT (t.id) FROM TransactionEntity t " +
      " WHERE t.foreignMerchantId = :merchantId " +
      " GROUP BY t.foreignMerchantId ")
  Long countAllTransactionOfMerchant(Long merchantId);

}
