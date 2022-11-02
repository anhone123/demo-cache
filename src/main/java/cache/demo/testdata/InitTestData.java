package cache.demo.testdata;

import cache.demo.entities.MerchantEntity;
import cache.demo.entities.TransactionEntity;
import cache.demo.enums.TransactionType;
import cache.demo.repository.TransactionRepository;
import cache.demo.repository.MerchantRepository;
import cache.demo.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile({"local", "test"})
@Transactional
public class InitTestData {

  private final MerchantRepository merchantRepository;
  private final TransactionRepository transactionRepository;

  private static final List<MerchantEntity> merchantEntities = List.of(
      MerchantEntity.builder()
          .id(1L)
          .merchantId("merchant1")
          .merchantName("name01")
          .merchantAdress("Da Neng")
          .build(),
      MerchantEntity.builder()
          .id(2L)
          .merchantId("merchant2")
          .merchantName("name02")
          .merchantAdress("Hue")
          .build()
  );

  @PostConstruct
  void initTestData() {

    initTestMerchant();
    initTestTransaction();

  }

  private void initTestMerchant() {
    for (MerchantEntity merchant : merchantEntities) {
      Optional<MerchantEntity> existedMerchant = merchantRepository.findMerchantEntityByMerchantId(merchant.getMerchantId());
      if (existedMerchant.isEmpty()) {
        merchantRepository.save(merchant);
        log.info("Inserted merchantId: {} ", merchant.getMerchantId());
      }
    }

  }

  private void initTestTransaction() {
    for (MerchantEntity merchant : merchantEntities) {
      long wantedTransactionRecordNumberOfMerchant = 5000;

      Long merchantTransactions = transactionRepository.countAllTransactionOfMerchant(merchant.getId());
      long numberOfNewTransactions = merchantTransactions != null ? merchantTransactions : 0;

      if (numberOfNewTransactions < wantedTransactionRecordNumberOfMerchant) {
        List<TransactionEntity> newTransactionOfMerchant = new ArrayList<>();
        for (int i = 0; i < wantedTransactionRecordNumberOfMerchant - numberOfNewTransactions; i++) {
          newTransactionOfMerchant.add(TransactionEntity.builder()
              .transactionId(RandomUtils.randomStringUUID())
              .merchantEntity(merchant)
              .amount((long) RandomUtils.nextInt(10000))
              .transactionType(TransactionType.getRandom())
              .build());
        }
        List<TransactionEntity> savedTransactions = transactionRepository.saveAll(newTransactionOfMerchant);
        log.info("Inserted: {} transaction of merchantId: {} ", savedTransactions.size(), merchant.getMerchantId());
      }
    }
  }
}
