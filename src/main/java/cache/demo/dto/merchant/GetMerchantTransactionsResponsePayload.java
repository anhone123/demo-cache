package cache.demo.dto.merchant;

import cache.demo.entities.MerchantEntity;
import cache.demo.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
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
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy - HH:mm:ss Z")
    private ZonedDateTime createdDate;
    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy - HH:mm:ss Z")
    private ZonedDateTime modifiedDate;

  }

}
