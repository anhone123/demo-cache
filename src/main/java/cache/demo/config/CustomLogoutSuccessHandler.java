package cache.demo.config;

import static cache.demo.controllers.endpoint.DemoCacheEndpoint.AUTH;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.AUTH_LOGOUT_SUCCESS;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements
    LogoutSuccessHandler {

  private final UrlPathHelper urlPathHelper;

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    log.info("Receive onLogoutSuccess call, sessionId: {}", request.getSession().getId());

    String context = urlPathHelper.getContextPath(request);
    log.info("Redirect url: {}", context + AUTH + AUTH_LOGOUT_SUCCESS);
    response.sendRedirect(context + AUTH + AUTH_LOGOUT_SUCCESS);
//    response.sendRedirect("https://www.google.com/");

    super.onLogoutSuccess(request, response, authentication);

    log.info("Request cookies: {}. ", Arrays.stream(request.getCookies())
        .map(Cookie::getName)
        .collect(Collectors.toList()));
    log.info("logged out then redirected with CustomLogoutSuccessHandler for user {}. ", authentication.getName());

  }

}
