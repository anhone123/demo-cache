package cache.demo.controllers;

import cache.demo.controllers.endpoint.DemoCacheEndpoint;
import cache.demo.dto.merchant.ChildrenResponsePayload;
import cache.demo.dto.merchant.ChildrenResponsePayload.Children;
import cache.demo.dto.merchant.GetMerchantTransactionsResponsePayload;
import cache.demo.dto.merchant.GetMerchantTransactionsResponsePayload.TransactionInfo;
import cache.demo.dto.merchant.GetMerchantTransactionsResponsePayloadEntity;
import cache.demo.exceptions.MerchantNotFoundException;
import cache.demo.service.merchant.MerchantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

  private final ObjectMapper objectMapper;

  private final ApplicationContext applicationContext;

  @PostMapping("/jsontest")
  @ApiOperation(value = "This method is used to get transactions of a merchant.")
  public ChildrenResponsePayload parseChildrenPayload(String payload) {
    try {
      System.out.println("hihi:" + applicationContext.getClass());
      ChildrenResponsePayload childrenResponsePayload = objectMapper.readValue(payload,
          ChildrenResponsePayload.class);

      log.info("Payload is Children: {}", childrenResponsePayload.getChildren() instanceof Children);
      log.info("Payload is Object: {}", childrenResponsePayload.getChildren() instanceof Object);
      log.info("Payload children type: {}", childrenResponsePayload.getChildren().getClass());

      if (childrenResponsePayload.getChildren() instanceof List) {
        log.info("Payload's children is: {}", childrenResponsePayload.getChildren().getClass().getName());

      }
//      Children children = (Children) childrenResponsePayload.getChildren();
//      log.info("children: {}", children);

      return childrenResponsePayload;
    } catch (JsonProcessingException e) {
      log.error("Parse json fail!", e);
      throw new RuntimeException(e.getMessage());
    }
  }

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
  public GetMerchantTransactionsResponsePayload getAllTransactionsOfAllMerchants(@ApiParam(hidden = true) HttpServletRequest request) {
    String sessionId = request.getSession().getId();
    log.info("Receive get all transactions of all merchants request with sessionId: {}", sessionId);
    long start = System.currentTimeMillis();
    GetMerchantTransactionsResponsePayload result = merchantService.getAllTransactionsOfAllMerchants();
    log.warn("Get transactions of all merchants finished in: {}!", System.currentTimeMillis() - start);
    return result;
  }

  @GetMapping(DemoCacheEndpoint.MERCHANT_ALL_TRANSACTIONS + "ENTITY")
  @ApiOperation(value = "This method is used to get all transactions of all merchants ENTITY.")
  public GetMerchantTransactionsResponsePayloadEntity getAllTransactionsOfAllMerchantsENTITY() {
    long start = System.currentTimeMillis();
    GetMerchantTransactionsResponsePayloadEntity result = merchantService.getAllTransactionsOfAllMerchantsENTITY();
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

