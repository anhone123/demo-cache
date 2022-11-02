package cache.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType {

  CREDIT,
  DEBIT;

  public static TransactionType getRandom() {
    return values()[(int) (Math.random() * values().length)];
  }

}
