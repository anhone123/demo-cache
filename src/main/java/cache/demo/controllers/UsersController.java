package cache.demo.controllers;

import cache.demo.controllers.endpoint.DemoCacheEndpoint;
import cache.demo.dto.user.GetUserResponsePayload.GetUserResponsePage;
import cache.demo.exceptions.LimitNotGreaterThanZeroException;
import cache.demo.exceptions.PageNotGreaterThanZeroException;
import cache.demo.exceptions.PaginationRequestMissingException;
import cache.demo.exceptions.SortPropertyInvalidException;
import cache.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping(DemoCacheEndpoint.USERS)
@Slf4j
public class UsersController {

  private final UserService userService;

  @GetMapping("/get-all-user-with-n+1")
  public GetUserResponsePage getAllUsersWithNplus1Issue(
      @RequestParam Integer page,
      @RequestParam Integer limit,
      @ApiIgnore @SortDefault(sort = "id", direction = Direction.DESC) Sort sort,
      @RequestParam(required = false) String keyword)
      throws PageNotGreaterThanZeroException, LimitNotGreaterThanZeroException,
      SortPropertyInvalidException, PaginationRequestMissingException {
    return userService.getAllUsersWithNplus1Issue(keyword, page, limit, sort);
  }

  @GetMapping("/get-all-user-with-2-queries")
  public GetUserResponsePage getAllUsersWith2Queries(
      @RequestParam Integer page,
      @RequestParam Integer limit,
      @ApiIgnore @SortDefault(sort = "id", direction = Direction.DESC) Sort sort,
      @RequestParam(required = false) String keyword)
      throws PageNotGreaterThanZeroException, LimitNotGreaterThanZeroException,
      SortPropertyInvalidException, PaginationRequestMissingException {
    return userService.getAllUsersWith2Query(keyword, page, limit, sort);
  }

//  @GetMapping("/get-all-user-with-1-query-and-return-userDtos")
//  public GetUserResponsePage getAllUsersWith1QueryAndReturnUserDtos(
//      @RequestParam Integer page,
//      @RequestParam Integer limit,
//      @ApiIgnore @SortDefault(sort = "id", direction = Direction.DESC) Sort sort,
//      @RequestParam(required = false) String keyword)
//      throws PageNotGreaterThanZeroException, LimitNotGreaterThanZeroException,
//      SortPropertyInvalidException, PaginationRequestMissingException {
//    return userService.getAllUsersWith1QueryAndReturnUserDtos(keyword, page, limit, sort);
//  }

  @GetMapping("/get-all-user-return-userProjections-with-1-JPA-QUERY")
  public GetUserResponsePage getAllUsersReturnUserProjectionInterfaceWith1JpaQuery(
      @RequestParam Integer page,
      @RequestParam Integer limit,
      @ApiIgnore @SortDefault(sort = "id", direction = Direction.DESC) Sort sort,
      @RequestParam(required = false) String keyword)
      throws PageNotGreaterThanZeroException, LimitNotGreaterThanZeroException,
      SortPropertyInvalidException, PaginationRequestMissingException {
    return userService.getAllUsersReturnUserProjectionInterfaceWith1JpaQuery(keyword, page, limit, sort);
  }

  @GetMapping("/get-all-user-return-userProjections-with-1-NATIVE-QUERY")
  public GetUserResponsePage getAllUsersReturnUserProjectionInterfaceWith1NativeQuery(
      @RequestParam Integer page,
      @RequestParam Integer limit,
      @ApiIgnore @SortDefault(sort = "id", direction = Direction.DESC) Sort sort,
      @RequestParam(required = false) String keyword)
      throws PageNotGreaterThanZeroException, LimitNotGreaterThanZeroException,
      SortPropertyInvalidException, PaginationRequestMissingException {
    return userService.getAllUsersReturnUserProjectionInterfaceWith1NativeQuery(keyword, page, limit, sort);
  }

}
