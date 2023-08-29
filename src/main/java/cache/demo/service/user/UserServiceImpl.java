package cache.demo.service.user;

import cache.demo.dto.user.GetUserResponsePayload;
import cache.demo.dto.user.GetUserResponsePayload.GetUserResponsePage;
import cache.demo.dto.user.UserDto;
import cache.demo.dto.user.UserLogInRequest;
import cache.demo.dto.user.UserLogInResponse;
import cache.demo.dto.user.UserProjection;
import cache.demo.entities.UserEntity;
import cache.demo.exceptions.CanNotLogInException;
import cache.demo.exceptions.LimitNotGreaterThanZeroException;
import cache.demo.exceptions.PageNotGreaterThanZeroException;
import cache.demo.exceptions.PaginationRequestMissingException;
import cache.demo.exceptions.SortPropertyInvalidException;
import cache.demo.exceptions.UserNotFoundException;
import cache.demo.repository.UserRepository;
import cache.demo.repository.UserSpecs;
import cache.demo.service.user.mapper.UserMapper;
import cache.demo.utils.CommonUtils;
import cache.demo.utils.RandomUtils;
import cache.demo.utils.ValidatorUtils;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ValidatorUtils validatorUtils;

  @Override
  public UserLogInResponse logIn(UserLogInRequest request)
      throws UserNotFoundException, CanNotLogInException {

    Optional<UserEntity> existedUser = userRepository.findUserEntityByUserId(request.getUserId());

    if (existedUser.isEmpty()) {
      throw new UserNotFoundException();
    }

    if (!passwordEncoder.matches(request.getUserPassword(), existedUser.get().getUserPassword())) {
      throw new CanNotLogInException();
    }

    UserEntity loggedInUser = existedUser.get();
    loggedInUser.setSessionToken(RandomUtils.randomStringUUID(true));
    loggedInUser.setSessionExpireDate(ZonedDateTime.now().plusMinutes(15));
    userRepository.save(loggedInUser);

    return UserLogInResponse.builder()
        .success(true)
        .sessionToken(loggedInUser.getSessionToken())
        .sessionExpireDate(loggedInUser.getSessionExpireDate())
        .build();
  }

  @Override
  public UserEntity getUserByUserId(String userId) throws UserNotFoundException {

    Optional<UserEntity> existedUser = userRepository.findUserEntityByUserId(userId);

    if (existedUser.isEmpty()) {
      throw new UserNotFoundException();
    }

    return existedUser.get();
  }

  @Override
  public GetUserResponsePage getAllUsersWithNplus1Issue(String keyword, Integer page,
      Integer size, Sort sort)
      throws LimitNotGreaterThanZeroException, PageNotGreaterThanZeroException,
      PaginationRequestMissingException, SortPropertyInvalidException {

    try {
      validatorUtils.validatePaginationRequest(page, size);
      Pageable queryPageable = CommonUtils.getPagination(page, size, sort);

      Page<UserEntity> users = userRepository.findAll(
          UserSpecs.adminFindAllUserSpecs(
              StringUtils.trimWhitespace(keyword)),
          queryPageable);

      List<GetUserResponsePayload> userResponses =
          UserMapper.INSTANCE.entitiesToGetUserResponsePayloads(users.getContent());

      return GetUserResponsePage.builder()
          .totalUsers(users.getTotalElements())
          .totalPages(users.getTotalPages())
          .currentPage(users.getPageable().getPageNumber() + 1)
          .data(userResponses)
          .build();
    } catch (PropertyReferenceException ex) {
      log.error("Received invalid sort property: {}", ex.getPropertyName());
      throw new SortPropertyInvalidException(ex);
    }
  }

  @Override
  public GetUserResponsePage getAllUsersWith2Query(String keyword, Integer page, Integer size,
      Sort sort)
      throws LimitNotGreaterThanZeroException, PageNotGreaterThanZeroException,
      PaginationRequestMissingException, SortPropertyInvalidException {

    try {
      validatorUtils.validatePaginationRequest(page, size);
      Pageable queryPageable = CommonUtils.getPagination(page, size, sort);

      Page<UserEntity> users = userRepository.findAll(
          UserSpecs.adminFindAllUserSpecs(
              StringUtils.trimWhitespace(keyword)),
          queryPageable);

      Set<Long> ids = users.getContent().stream().map(UserEntity::getId)
          .collect(Collectors.toSet());
      List<UserEntity> result = userRepository.findAllByIdIn(ids, queryPageable.getSort());

      List<GetUserResponsePayload> userResponses =
          UserMapper.INSTANCE.entitiesToGetUserResponsePayloads(result);

      return GetUserResponsePage.builder()
          .totalUsers(users.getTotalElements())
          .totalPages(users.getTotalPages())
          .currentPage(users.getPageable().getPageNumber() + 1)
          .data(userResponses)
          .build();
    } catch (PropertyReferenceException ex) {
      log.error("Received invalid sort property: {}", ex.getPropertyName());
      throw new SortPropertyInvalidException(ex);
    }
  }

//  @Override
//  public GetUserResponsePage getAllUsersWith1QueryAndReturnUserDtos(String keyword,
//      Integer page, Integer size,
//      Sort sort)
//      throws LimitNotGreaterThanZeroException, PageNotGreaterThanZeroException,
//      PaginationRequestMissingException, SortPropertyInvalidException {
//
//    try {
//      validatorUtils.validatePaginationRequest(page, size);
//      Pageable queryPageable = CommonUtils.getPagination(page, size, sort);
//
//      List<UserDto> users = userRepository.findUserWithNativeQuery(
//          keyword != null ? "%" + keyword.toLowerCase() + "%" : null
////          ,
////          queryPageable.getPageSize(), queryPageable.getOffset()
//      );
//
//      return GetUserResponsePage.builder()
//          .currentPage(Long.valueOf(queryPageable.getOffset()).intValue() + 1)
//          .dtoData(users)
//          .build();
//    } catch (PropertyReferenceException ex) {
//      log.error("Received invalid sort property: {}", ex.getPropertyName());
//      throw new SortPropertyInvalidException(ex);
//    }
//  }

  @Override
  public GetUserResponsePage getAllUsersReturnUserProjectionInterfaceWith1JpaQuery(
      String keyword, Integer page, Integer size, Sort sort)
      throws LimitNotGreaterThanZeroException, PageNotGreaterThanZeroException,
      PaginationRequestMissingException, SortPropertyInvalidException {

    try {
      validatorUtils.validatePaginationRequest(page, size);
      Pageable queryPageable = CommonUtils.getPagination(page, size, sort);

      Page<UserProjection> users = userRepository.findUserWithNativeQueryReturnInterface(
          keyword != null ? "%" + keyword.toLowerCase() + "%" : null, queryPageable
      );

      return GetUserResponsePage.builder()
          .totalPages(users.getTotalPages())
          .totalUsers(users.getTotalElements())
          .currentPage(users.getPageable().getPageNumber()+1)
          .projectionData(users.getContent())
          .build();
    } catch (PropertyReferenceException ex) {
      log.error("Received invalid sort property: {}", ex.getPropertyName());
      throw new SortPropertyInvalidException(ex);
    }
  }

  @Override
  public GetUserResponsePage getAllUsersReturnUserProjectionInterfaceWith1NativeQuery(
      String keyword, Integer page, Integer size, Sort sort)
      throws LimitNotGreaterThanZeroException, PageNotGreaterThanZeroException,
      PaginationRequestMissingException, SortPropertyInvalidException {

    try {
      validatorUtils.validatePaginationRequest(page, size);
      Pageable queryPageable = CommonUtils.getPagination(page, size, sort);

      List<UserProjection> users = userRepository.findUserWithNativeQueryReturnInterface(
          keyword != null ? "%" + keyword.toLowerCase() + "%" : null,
          queryPageable.getPageSize(), queryPageable.getOffset()
      );

      return GetUserResponsePage.builder()
          .currentPage(queryPageable.getPageNumber()+1)
          .projectionData(users)
          .build();
    } catch (PropertyReferenceException ex) {
      log.error("Received invalid sort property: {}", ex.getPropertyName());
      throw new SortPropertyInvalidException(ex);
    }
  }
}
