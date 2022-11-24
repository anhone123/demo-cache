package cache.demo.dto.user;

import java.io.Serializable;
import java.time.ZonedDateTime;
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
public class UserLogInResponse implements Serializable {

  private boolean success;
  private String sessionToken;
  private ZonedDateTime sessionExpireDate;


}
