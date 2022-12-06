package cache.demo.config;

import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.UrlPathHelper;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler extends SecurityContextLogoutHandler implements LogoutHandler {

  private final UrlPathHelper urlPathHelper;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    Assert.notNull(request, "HttpServletRequest required");
    HttpSession session = request.getSession();

    //Invalidate session
    if (session != null) {
      session.invalidate();
      log.info("Invalidated sessionId: {}", session.getId());
    }

    Optional<Cookie> sessionCookie = Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals("JSESSIONID"))
        .findFirst();

    if (sessionCookie.isPresent()) {
      sessionCookie.get().setMaxAge(0);//ok
      log.info("Removed JSESSIONID cookie!");
      response.addCookie(sessionCookie.get());
    }

    log.info("Response's headers: " + response.getHeaderNames());


    //clearAuthentication
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication((Authentication)null);
    SecurityContextHolder.clearContext();
  }

}
