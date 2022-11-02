package cache.demo.service.merchant;

import cache.demo.dto.merchant.GetMerchantTransactionsResponsePayload;
import cache.demo.exceptions.MerchantNotFoundException;

public interface MerchantService {

  GetMerchantTransactionsResponsePayload getTransactionsOfMerchant(String merchantId) throws MerchantNotFoundException;

  GetMerchantTransactionsResponsePayload getAllTransactionsOfAllMerchants();

  GetMerchantTransactionsResponsePayload getAllTransactionsOfAllMerchantsAndSaveDupCacheKey();

  GetMerchantTransactionsResponsePayload putMerchantTransactionsCache(String merchantId) throws MerchantNotFoundException;

  void clearMerchantTransactionsCache(String merchantId);

}
