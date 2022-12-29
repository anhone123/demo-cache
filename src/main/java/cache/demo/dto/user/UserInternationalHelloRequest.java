package cache.demo.dto.user;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInternationalHelloRequest {

  @NotBlank
  @Pattern(regexp = "[a-zA-Z ]+")
  private String name;

  @NotNull
  @Max(value = 200, message = "Age can not more than 200!")
//  @NotBlank
  private Integer age;

  @NotBlank
  @Length(min = 1, message = "Words to say must be more than 1 character!")
  private String words;

}
