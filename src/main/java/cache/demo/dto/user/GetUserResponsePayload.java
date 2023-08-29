package cache.demo.dto.user;

import cache.demo.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class GetUserResponsePayload {

  private Long id;
  private String userId;
  private String firstName;
  private String lastName;
  private String email;
  private List<Integer> roles;
  private ZonedDateTime createdDate;
  private ZonedDateTime modifiedDate;

  @Builder
  @Getter
  public static class GetUserResponsePage {

    private final Long totalUsers;
    private final Integer totalPages;
    private final Integer currentPage;
    private final List<GetUserResponsePayload> data;
    private final List<UserEntity> entitiesData;
//    private final List<UserDto> dtoData;
    private final List<UserProjection> projectionData;

  }

}
