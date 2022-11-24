package cache.demo.entities;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

@Entity
@Table(name = "test_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(exclude = {"permissions"})
public class RoleEntity implements Comparable<RoleEntity>{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String codeName;

  @Column
  private String name;

  @Column
  private String description;

  @CreatedDate
  private ZonedDateTime createdDate;

  @LastModifiedDate
  private ZonedDateTime modifiedDate;

  @Override
  public int compareTo(RoleEntity otherRole) {
    //SORT ASC
    return this.getId().compareTo(otherRole.getId());
  }
}
