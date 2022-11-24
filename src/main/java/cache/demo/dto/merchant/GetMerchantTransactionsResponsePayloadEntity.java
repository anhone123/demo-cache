package cache.demo.dto.merchant;

import cache.demo.entities.TransactionEntity;
import cache.demo.enums.TransactionType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMerchantTransactionsResponsePayloadEntity implements Serializable {

  private long totalTransaction;
  private List<TransactionEntity> transactions;

}
