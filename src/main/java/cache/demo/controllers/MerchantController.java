package cache.demo.controllers;

import cache.demo.controllers.endpoint.DemoCacheEndpoint;
import cache.demo.dto.merchant.GetMerchantTransactionsResponsePayload;
import cache.demo.dto.merchant.GetMerchantTransactionsResponsePayload.TransactionInfo;
import cache.demo.exceptions.MerchantNotFoundException;
import cache.demo.service.merchant.MerchantService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DemoCacheEndpoint.MERCHANT)
@RequiredArgsConstructor
@Slf4j
public class MerchantController {

  private final MerchantService merchantService;

  @GetMapping(DemoCacheEndpoint.MERCHANT_TRANSACTIONS)
  @ApiOperation(value = "This method is used to get transactions of a merchant.")
  public GetMerchantTransactionsResponsePayload getTransactionsOfMerchant(@RequestParam String merchantId)
      throws MerchantNotFoundException {
    long start = System.currentTimeMillis();
    GetMerchantTransactionsResponsePayload result = merchantService.getTransactionsOfMerchant(merchantId);
    log.warn("Get transactions of merchant: {} finished in: {}!", merchantId, System.currentTimeMillis() - start);
    return result;
  }

  @GetMapping(DemoCacheEndpoint.MERCHANT_ALL_TRANSACTIONS)
  @ApiOperation(value = "This method is used to get all transactions of all merchants.")
  public GetMerchantTransactionsResponsePayload getAllTransactionsOfAllMerchants() {
    long start = System.currentTimeMillis();
    GetMerchantTransactionsResponsePayload result = merchantService.getAllTransactionsOfAllMerchants();
    log.warn("Get transactions of all merchants finished in: {}!", System.currentTimeMillis() - start);
    return result;
  }

  @GetMapping(DemoCacheEndpoint.MERCHANT_ALL_TRANSACTIONS_DUP_CACHE_KEY)
  @ApiOperation(value = "This method is used to get all transactions of all merchants. And save cache with DUPLICATED key")
  public GetMerchantTransactionsResponsePayload getAllTransactionsOfAllMerchantsToGetDuplicatedKey() {
    long start = System.currentTimeMillis();
    GetMerchantTransactionsResponsePayload result = merchantService.getAllTransactionsOfAllMerchantsAndSaveDupCacheKey();

//    GetMerchantTransactionsResponsePayload result = getAllTransactionsOfAllMerchants();
//    result.getTransactions().add(TransactionInfo.builder().transactionId("55555").build());
//    result.getTransactions().add(TransactionInfo.builder().transactionId("66666").build());
//    result.setTotalTransaction(result.getTransactions().size());

    log.warn("Get transactions of all merchants duplicated finished in: {}!", System.currentTimeMillis() - start);
    return result;
  }

  @DeleteMapping(DemoCacheEndpoint.MERCHANT_TRANSACTIONS)
  @ApiOperation(value = "This method is used clear transactions cache of a merchant.")
  public void clearMerchantTransactionsCache(@RequestParam String merchantId) {
    merchantService.clearMerchantTransactionsCache(merchantId);
    log.warn("Transactions cache of merchant: {} cleared!", merchantId);
  }

  @PutMapping(DemoCacheEndpoint.MERCHANT_TRANSACTIONS)
  @ApiOperation(value = "This method is used set default transactions cache of a merchant.")
  public void putMerchantTransactionsCache(@RequestParam String merchantId)
      throws MerchantNotFoundException {
    merchantService.putMerchantTransactionsCache(merchantId);
    log.warn("Transactions cache of merchant: {} have been put with default value!", merchantId);
  }
}

