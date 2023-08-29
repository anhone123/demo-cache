package cache.demo.repository;

import cache.demo.entities.UserEntity;
import cache.demo.entities.UserEntity_;
import cache.demo.utils.CommonUtils;
import javax.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecs {

  public static Specification<UserEntity> filterKeywordSpec(String keyword, boolean isIgnoreCase) {
    return (root, query, builder) -> {
      if (StringUtils.hasText(keyword)) {

        String lowercase = CommonUtils.buildPatternKeyword(keyword, true);
        String original = CommonUtils.buildPatternKeyword(keyword, false);

        Predicate likeEmail = isIgnoreCase
            ? builder.like(builder.lower(root.get(UserEntity_.email)), lowercase)
            : builder.like(root.get(UserEntity_.email), original);

        Predicate likeFirstName = isIgnoreCase
            ? builder.like(builder.lower(root.get(UserEntity_.firstName)), lowercase)
            : builder.like(root.get(UserEntity_.firstName), original);

        Predicate likeLastName = isIgnoreCase
            ? builder.like(builder.lower(root.get(UserEntity_.lastName)), lowercase)
            : builder.like(root.get(UserEntity_.lastName), original);

        return builder.or(likeEmail, likeFirstName, likeLastName);
      }
      return null;
    };
  }

  public static Specification<UserEntity> adminFindAllUserSpecs(String keyword) {
    return filterKeywordSpec(keyword, true);
  }
}
