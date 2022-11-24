package cache.demo.config;

import static cache.demo.controllers.endpoint.DemoCacheEndpoint.ACCESS_DENIED;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.AUTH;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.AUTH_LOGIN;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.AUTH_LOGOUT;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.AUTH_LOGOUT_SUCCESS;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.CACHE_MANAGEMENT;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.MERCHANT;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AnHoSecurityBasicConfig extends WebSecurityConfigurerAdapter {

  private final DataSource dataSource;
  private final UserDetailsService anhoUserDetailsService;
  private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
//  private final MyBasicAuthenticationEntryPoint basicAuthenticationEntryPoint;

  //https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
  @Bean
  @Override
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
    BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
    basicAuthenticationEntryPoint.setRealmName("anho_ne");
    return basicAuthenticationEntryPoint;
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    // Requests send to the Web Server request must be authenticated
    http.authorizeRequests()
//        .antMatchers("/swagger-ui/**").authenticated()
        .antMatchers(MERCHANT + "/**").authenticated()
        .antMatchers(CACHE_MANAGEMENT + "/**").authenticated()
        .antMatchers(AUTH + AUTH_LOGOUT + "/**").authenticated()
        .antMatchers(AUTH + AUTH_LOGIN +"/**").permitAll()
        .antMatchers(ACCESS_DENIED +"/**").permitAll()

    ;
    //add logout handler
    http.logout()
        .logoutSuccessHandler(customLogoutSuccessHandler)
        .logoutUrl(AUTH_LOGOUT)
//        .logoutSuccessUrl("https://www.google.com/")
//        .invalidateHttpSession(true) // default = true
//        .deleteCookies("JSESSIONID")
    ;
    http.exceptionHandling()
        .accessDeniedPage(AUTH + ACCESS_DENIED);

    // Use AuthenticationEntryPoint to handle fail authenticate user/password
    // Normally return fail login page, but its more simple for REST-api
    http.httpBasic()
        .authenticationEntryPoint(basicAuthenticationEntryPoint())
    ;

//    http.rememberMe()
//        .key("superSecretKey")
//        .tokenRepository(persistentTokenRepository());

//    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    //auth.inMemoryAuthentication()
    //        .withUser("bryan").password(passwordEncoder().encode("pass")).roles("USER");
    auth
//      .jdbcAuthentication()
//      .dataSource(dataSource)
        .userDetailsService(anhoUserDetailsService)
        .passwordEncoder(passwordEncoder())
    ;
  }

  //Nếu không custom config, spring dùng default table persistent_logins đề lưu token, save login cho user.
  //org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
  //Trong này có sẵn ddl tạo table luôn.
  //cusstom lại thì override mấy method như getTokenForSeries, createNewToken, updateToken,... nhé
  //Search gg thêm nha.
  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
    db.setDataSource(dataSource);
//    db.setCreateTableOnStartup(true);
    return db;
  }

}
