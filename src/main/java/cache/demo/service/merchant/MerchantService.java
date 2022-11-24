package cache.demo.service.merchant;

import cache.demo.dto.merchant.GetMerchantTransactionsResponsePayload;
import cache.demo.dto.merchant.GetMerchantTransactionsResponsePayloadEntity;
import cache.demo.exceptions.MerchantNotFoundException;

public interface MerchantService {

  GetMerchantTransactionsResponsePayload getTransactionsOfMerchant(String merchantId) throws MerchantNotFoundException;

  GetMerchantTransactionsResponsePayload getAllTransactionsOfAllMerchants();

  GetMerchantTransactionsResponsePayloadEntity getAllTransactionsOfAllMerchantsENTITY();

  GetMerchantTransactionsResponsePayload getAllTransactionsOfAllMerchantsAndSaveDupCacheKey();

  GetMerchantTransactionsResponsePayload putMerchantTransactionsCache(String merchantId) throws MerchantNotFoundException;

  void clearMerchantTransactionsCache(String merchantId);

}
