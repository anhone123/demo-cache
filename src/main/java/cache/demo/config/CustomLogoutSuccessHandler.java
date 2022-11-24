package cache.demo.config;

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

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements
    LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    String refererUrl = request.getHeader("Referer");
    System.out.println("Request cookies:" + Arrays.stream(request.getCookies()).map(Cookie::getName).collect(Collectors.toList()));
    System.out.println("Request session:" + request.getSession().getId());
    log.info("logged out with CustomLogoutSuccessHandler! " + refererUrl);

    super.onLogoutSuccess(request, response, authentication);
  }

}
