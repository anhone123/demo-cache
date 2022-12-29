package cache.demo.controllers;

import cache.demo.controllers.endpoint.DemoCacheEndpoint;
import cache.demo.dto.user.AccessDeniedResponse;
import cache.demo.dto.user.LogOutResponse;
import cache.demo.dto.user.UserLogInRequest;
import cache.demo.dto.user.UserLogInResponse;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DemoCacheEndpoint.AUTH)
@RequiredArgsConstructor
@Slf4j
public class AuthControllerBasic {

//  private final UserService userService;
  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;

  @PostMapping(DemoCacheEndpoint.AUTH_LOGIN)
  @ApiOperation(value = "This method is used to login a user with userId and userPassword." )
  public ResponseEntity<UserLogInResponse> logIn(HttpServletRequest request, HttpServletResponse response,
      @RequestBody UserLogInRequest userLogInRequest) {

    log.info("Receive login request for user: {}", userLogInRequest.getUserId());
    Authentication authentication;
    UserDetails userDetails = userDetailsService.loadUserByUsername(userLogInRequest.getUserId());
    try {
       authentication = authenticationManager.authenticate(
           //input hashed password of userDetails object will cause AuthenticationException,
           //need to input plain password
          new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userLogInRequest.getUserPassword(), userDetails.getAuthorities()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (AuthenticationException e) {
      log.error("Error when basic-login user!", e);
      throw e;
    }

    log.info("User's sessionId: {}", request.getSession().getId());
    log.info("{} principal: {}", authentication.getName(), authentication.getPrincipal());
    log.info("{} credentials: {}", authentication.getName(), authentication.getCredentials());
    log.info("Response's headers: " + response.getHeaderNames());

    return new ResponseEntity(UserLogInResponse.builder()
        .success(true)
        .sessionToken(request.getSession().getId())
        .build(),
        HttpStatus.OK);
  }

  @GetMapping(DemoCacheEndpoint.AUTH_LOGOUT_MANUAL)
  @ApiOperation(value = "This method is used to logout a user with session and manual way." )
  public ResponseEntity<LogOutResponse> manualLogout(
      HttpServletRequest request, HttpServletResponse response) {

    HttpSession session = request.getSession(); //co the truyen HttpSession tu method param
    String sessionId = session.getId();
    log.info("Receive logout request for sessionId: {}", session.getId());

    // Server MUST invalidate session. invalidate cookie is optional.
    session.invalidate();
    log.info("Session invalidated for sessionId: {}", session.getId());

    Cookie[] requestCookies = request.getCookies();
//    log.info("Request's cookies names: {}", Arrays.stream(requestCookies).map(Cookie::getName).collect(Collectors.toList()));
//    log.info("Request's cookies values: {}", Arrays.stream(requestCookies).map(Cookie::getValue).collect(Collectors.toList()));

    Optional<Cookie> sessionCookieOptional = Arrays.stream(requestCookies)
        .filter(cookie -> cookie.getName().equals("JSESSIONID"))
        .findFirst();

    if (sessionCookieOptional.isPresent()) {
      Cookie sessionCookie = sessionCookieOptional.get();
      log.info("Cookie JSESSIONID=" + sessionCookie.getValue());
      sessionCookie.setMaxAge(0);//cookie value still there
      sessionCookie.setValue("");
      log.info("Removed JSESSIONID cookie! (setMaxAge(0))");
      response.addCookie(sessionCookie);
    }

    log.info("Response's headers: " + response.getHeaderNames());

    return new ResponseEntity(LogOutResponse.builder()
        .description(String.format("You have been logged out from sessionId: %s", sessionId))
        .build(),
        HttpStatus.OK);
  }

  @GetMapping(DemoCacheEndpoint.AUTH_LOGOUT_SUCCESS)
  @ApiOperation(value = "This method is used to show description after logout." )
  public ResponseEntity<LogOutResponse> logoutSuccess() {
    log.info("logout-success-api called");
    return new ResponseEntity(LogOutResponse.builder()
        .description("You have been logged out!")
        .build(),
        HttpStatus.OK);
  }

  @GetMapping(DemoCacheEndpoint.AUTH_LOGOUT_SPRING)
  @ApiOperation(value = "This method is used to logout a user with session using Spring config." )
  public ResponseEntity<LogOutResponse> springLogout(
      HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    log.info("Receive spring-logout request for sessionId: {}", request.getSession().getId());
    return new ResponseEntity(LogOutResponse.builder()
        .description(String.format("You have been logged out from sessionId: %s", session.getId()))
        .build(),
        HttpStatus.OK);
  }

  @GetMapping(DemoCacheEndpoint.ACCESS_DENIED)
  @ApiOperation(value = "This method is used to return access denied description." )
  public ResponseEntity<AccessDeniedResponse> accessDenied() {
    return new ResponseEntity(AccessDeniedResponse.builder()
        .description("Access Denied nha!")
        .build(),
        HttpStatus.FORBIDDEN);
  }

}

