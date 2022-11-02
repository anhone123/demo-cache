package cache.demo.dto.merchant;

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
public class GetMerchantTransactionsResponsePayload implements Serializable {

  private long totalTransaction;
  private List<TransactionInfo> transactions;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TransactionInfo implements Serializable {

    private Long id;
    private String transactionId;
    private Long merchantId;
    private Long amount;
    private TransactionType transactionType;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime createdDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime modifiedDate;

  }

}
