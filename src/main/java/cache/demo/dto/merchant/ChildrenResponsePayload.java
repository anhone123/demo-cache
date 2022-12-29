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
public class ChildrenResponsePayload implements Serializable {

  private long totalChildren;
  private Object children;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Children implements Serializable {

    private Long age;
    private String name;

  }

}
