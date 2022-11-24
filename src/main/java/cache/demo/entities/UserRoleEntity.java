package cache.demo.entities;

import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode
@Entity
@Table(name = "test_user_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@IdClass(UserRoleId.class)
public class UserRoleEntity {

  @Id
  private Long userId;

  @Id
  private Integer roleId;

  @CreatedDate
  private ZonedDateTime createdDate;

  @LastModifiedDate
  private ZonedDateTime modifiedDate;

}
