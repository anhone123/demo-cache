package cache.demo.testdata;

import cache.demo.entities.MerchantEntity;
import cache.demo.entities.RoleEntity;
import cache.demo.entities.TransactionEntity;
import cache.demo.entities.UserEntity;
import cache.demo.entities.UserRoleEntity;
import cache.demo.enums.TransactionType;
import cache.demo.repository.MerchantRepository;
import cache.demo.repository.RoleRepository;
import cache.demo.repository.TransactionRepository;
import cache.demo.repository.UserRepository;
import cache.demo.repository.UserRoleRepository;
import cache.demo.utils.RandomUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile({"local", "test"})
@Transactional
public class InitTestData {

  private static final String ANHO_MASTER = "anhomaster";
  private static final String ANHO_VIEWER = "anhoviewer";
  private static final String ANHO_PASSWORD = "ocschos123";

  private static final String ROLE_VIEWER = "VIEWER";
  private static final String ROLE_EDIOR = "EDITOR";

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;
  private final MerchantRepository merchantRepository;
  private final TransactionRepository transactionRepository;

  private final PasswordEncoder passwordEncoder;

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

  private static final Set<RoleEntity> roleEntitySet = Set.of(
      RoleEntity.builder()
          .codeName(ROLE_VIEWER)
          .name("Role Viewer")
          .description("Just view data only.")
          .build(),
      RoleEntity.builder()
          .codeName(ROLE_EDIOR)
          .name("Role Edior")
          .description("Can edit any data.")
          .build()
  );

  private static final Map<String, RoleEntity> savedRoleEntitiesMap = new HashMap<>();
  private static final List<UserEntity> savedUserEntities = new ArrayList<>();

  @PostConstruct
  void initTestData() {
    initUsers();
    initRolesAndBindToUsers();
    initTestMerchant();
    initTestTransaction();
  }

  private void initUsers() {
    List<UserEntity> userEntities = List.of(
        UserEntity.builder()
            .userId(ANHO_MASTER)
            .userPassword(passwordEncoder.encode(ANHO_PASSWORD))
            .build(),
        UserEntity.builder()
            .userId(ANHO_VIEWER)
            .userPassword(passwordEncoder.encode(ANHO_PASSWORD))
            .build()
    );

    for (UserEntity userEntity : userEntities) {
      Optional<UserEntity> existedUser = userRepository.findUserEntityByUserId(userEntity.getUserId());
      if (existedUser.isEmpty()) {
        savedUserEntities.add(userRepository.save(userEntity));
        log.info("Inserted user with userId: {} ", userEntity.getUserId());
      }
    }
  }

  private void initRolesAndBindToUsers() {
    for (RoleEntity role : roleEntitySet) {
      Optional<RoleEntity> existedRoleEntity = roleRepository.findRoleEntityByCodeName(role.getCodeName());

      if (existedRoleEntity.isEmpty()) {
        RoleEntity savedRole = roleRepository.save(role);
        savedRoleEntitiesMap.put(savedRole.getCodeName(), savedRole);
      } else {
        savedRoleEntitiesMap.put(existedRoleEntity.get().getCodeName(), existedRoleEntity.get());
      }
    }

    for (UserEntity userEntity : savedUserEntities) {
      if (userEntity.getUserId().equals(ANHO_MASTER)) {
        for (Entry<String, RoleEntity> entry : savedRoleEntitiesMap.entrySet()) {
          UserRoleEntity userRoleEntity = UserRoleEntity.builder()
              .userId(userEntity.getId())
              .roleId(entry.getValue().getId())
              .build();
          userRoleRepository.save(userRoleEntity);
        }
      }

      if (userEntity.getUserId().equals(ANHO_VIEWER)) {
        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
            .userId(userEntity.getId())
            .roleId(savedRoleEntitiesMap.get(ROLE_VIEWER).getId())
            .build();
        userRoleRepository.save(userRoleEntity);
      }

    }
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
      long wantedTransactionRecordNumberOfMerchant = 1;

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
