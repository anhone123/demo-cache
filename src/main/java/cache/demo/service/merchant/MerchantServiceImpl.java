package cache.demo.service.merchant;

import static cache.demo.RedisExample.CACHES_OF_CACHEMANAGER_TEMPLATE;

import cache.demo.config.ApplicationPropertiesConfig;
import cache.demo.constains.CacheNames;
import cache.demo.dto.merchant.GetMerchantTransactionsResponsePayload;
import cache.demo.dto.merchant.GetMerchantTransactionsResponsePayload.TransactionInfo;
import cache.demo.entities.MerchantEntity;
import cache.demo.entities.TransactionEntity;
import cache.demo.exceptions.MerchantNotFoundException;
import cache.demo.repository.MerchantRepository;
import cache.demo.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

  private final MerchantRepository merchantRepository;
  private final TransactionRepository transactionRepository;

  @Qualifier("minorRedisCacheManager")
  @Autowired // NOT recommend
  private final RedisCacheManager minorRedisCacheManager;

  public static final SimpleKey simpleKey = new SimpleKey("Element_1", "Element_2");


  // Nếu không chỉ rõ key, Spring kiểm tra key #merchantId - hoặc tất cả param khác nếu có - đã lưu
  // value trong cache chưa? Nếu có thì trả về, không thì thực thi method
  // Quan trọng là cặp cache name và cache key, nếu khác cacheManager nhưng trùng cacheName và cacheKey thì như nhau
  // Redis quản lý theo cache name và cache key, còn framework java thì dùng cacheManager quản lý cache
  @Override
  @Cacheable(value = CacheNames.CACHE_TRANSACTIONS_OF_A_MERCHANT, key = "#merchantId", cacheManager = "mainRedisCacheManager")
  public GetMerchantTransactionsResponsePayload getTransactionsOfMerchant(String merchantId)
      throws MerchantNotFoundException {
    MerchantEntity existedMerchant = merchantRepository.findMerchantEntityByMerchantId(merchantId)
        .orElseThrow(MerchantNotFoundException::new);

    List<TransactionEntity> transactions = transactionRepository.findTransactionEntitiesByForeignMerchantId(
        existedMerchant.getId());

    return GetMerchantTransactionsResponsePayload.builder()
        .totalTransaction(transactions.size())
        .transactions(mapTransactionEntitiesToTransactionInfos(transactions))
        .build();
  }

  @Override
  @Cacheable(value = CacheNames.CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS, cacheManager = "mainRedisCacheManager")
  public GetMerchantTransactionsResponsePayload getAllTransactionsOfAllMerchants() {

    List<TransactionEntity> transactions = transactionRepository.findTransactionEntitiesByForeignMerchantId(null);
    log.info("Got {} transaction record from DB!", transactions.size());
    return GetMerchantTransactionsResponsePayload.builder()
        .totalTransaction(transactions.size())
        .transactions(mapTransactionEntitiesToTransactionInfos(transactions))
        .build();
  }

  @Override
//  @Cacheable(value = CacheNames.CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS, key = "#root.methodName", cacheManager = "mainRedisCacheManager")
  @Cacheable(value = CacheNames.CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS, cacheManager = "minorRedisCacheManager")
  public GetMerchantTransactionsResponsePayload getAllTransactionsOfAllMerchantsAndSaveDupCacheKey() {
    GetMerchantTransactionsResponsePayload returnPayload = this.getAllTransactionsOfAllMerchants();

    List<TransactionInfo> newTransactionsReturn = returnPayload.getTransactions();
    newTransactionsReturn.add(TransactionInfo.builder()
            .id(999999999L)
            .transactionId("ahihi")
        .build());
    log.info("Got {} transaction record!", newTransactionsReturn.size());
    log.info("minorRedisCacheManager's cacheNames: {}", minorRedisCacheManager.getCacheNames());

    return GetMerchantTransactionsResponsePayload.builder()
        .totalTransaction(newTransactionsReturn.size())
        .transactions(newTransactionsReturn)
        .build();
  }

  @Override
  @CachePut(value = CacheNames.CACHE_TRANSACTIONS_OF_A_MERCHANT, key = "#merchantId", cacheManager = "mainRedisCacheManager")
  public GetMerchantTransactionsResponsePayload putMerchantTransactionsCache(String merchantId)
      throws MerchantNotFoundException {
    merchantRepository.findMerchantEntityByMerchantId(merchantId)
        .orElseThrow(MerchantNotFoundException::new);

    List<TransactionInfo> transactions = List.of(TransactionInfo.builder()
            .id(123456789L)
            .transactionId("abcdefghiklmnpoq")
        .build());

    return GetMerchantTransactionsResponsePayload.builder()
        .totalTransaction(transactions.size())
        .transactions(transactions)
        .build();
  }


  private List<TransactionInfo> mapTransactionEntitiesToTransactionInfos(List<TransactionEntity> transactionEntities) {
    if (transactionEntities == null || transactionEntities.isEmpty()) {
      return Collections.emptyList();
    }

    List<TransactionInfo> result = new ArrayList<>();

    for (TransactionEntity transactionEntity : transactionEntities) {
      result.add(TransactionInfo.builder()
          .id(transactionEntity.getId())
          .transactionId(transactionEntity.getTransactionId())
          .merchantId(transactionEntity.getForeignMerchantId())
          .amount(transactionEntity.getAmount())
          .transactionType(transactionEntity.getTransactionType())
          .createdDate(transactionEntity.getCreatedDate())
          .modifiedDate(transactionEntity.getModifiedDate())
          .build());
    }

    return result;
  }

  @Override
  @CacheEvict(value = CacheNames.CACHE_TRANSACTIONS_OF_A_MERCHANT, key = "#merchantId", cacheManager = "mainRedisCacheManager")
  public void clearMerchantTransactionsCache(String merchantId) {
  }

  // KHÔNG args và có thể có return value (recomment là k nên)
  @Scheduled(fixedRateString = "180000", initialDelay = 180000)
  public void putAllMerchantTransactionsCacheSchedule() {
    getAllTransactionsOfAllMerchants();
    log.warn("All transactions of all merchants cache updated with schedule");
  }

}
