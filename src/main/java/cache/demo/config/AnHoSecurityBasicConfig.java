package cache.demo.config;

import static cache.demo.controllers.endpoint.DemoCacheEndpoint.ACCESS_DENIED;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.AUTH;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.AUTH_LOGIN;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.AUTH_LOGOUT;
import static cache.demo.controllers.endpoint.DemoCacheEndpoint.AUTH_LOGOUT_SPRING;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AnHoSecurityBasicConfig extends WebSecurityConfigurerAdapter {
  //tuong duong voi config bean SecurityFilterChain

  private final DataSource dataSource;
  private final UserDetailsService anhoUserDetailsService;
  private final CustomLogoutHandler customLogoutHandler;
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

  //modify request/response if request is UNAUTHORIZED
  //override lai cac method cua BasicAuthenticationEntryPoint la ok nha
  @Bean
  public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
    BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
    basicAuthenticationEntryPoint.setRealmName("anho_ne");
    return basicAuthenticationEntryPoint;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .formLogin()//default url: domain/login
//        .loginPage("/login-form")
    ;

    // Requests send to the Web Server request must be authenticated
    http.authorizeRequests()
//        .antMatchers("/swagger-ui/**").authenticated()
        .antMatchers("/swagger-ui/**").permitAll()
        .antMatchers(MERCHANT + "/**").authenticated()
        .antMatchers(CACHE_MANAGEMENT + "/**").authenticated()
        .antMatchers(AUTH + AUTH_LOGOUT + "/**").authenticated()
        .antMatchers(AUTH + AUTH_LOGIN +"/**").permitAll()
        .antMatchers(AUTH + AUTH_LOGOUT_SUCCESS + "/**").permitAll()
        .antMatchers(ACCESS_DENIED +"/**").permitAll()

    ;
    //vao day xem giai thich nha
    // https://docs.spring.io/spring-security/reference/servlet/authentication/logout.html
    http
        .logout()
        .logoutUrl(AUTH + AUTH_LOGOUT_SPRING)
        .addLogoutHandler(customLogoutHandler)
//        .logoutRequestMatcher(new AntPathRequestMatcher(AUTH_LOGOUT_SPRING, "GET"))
//        .logoutSuccessUrl(AUTH + AUTH_LOGOUT_SUCCESS) // neu config .logoutSuccessHandler(...) thi bypass .logoutSuccessUrl(...)
        .logoutSuccessHandler(customLogoutSuccessHandler)
        .invalidateHttpSession(true) // default = true
        .deleteCookies("JSESSIONID") //session cookie
    ;

    //return api nay neu trong co quyen truy cap api
    http.exceptionHandling()
        .accessDeniedPage(AUTH + ACCESS_DENIED);

    // Use AuthenticationEntryPoint to handle fail authenticate user/password
    // Normally return fail login page, but its more simple for REST-api
    // override its method to customize
    http.httpBasic()
        .authenticationEntryPoint(basicAuthenticationEntryPoint())
    ;

    http.rememberMe()
        .userDetailsService(anhoUserDetailsService)
        .key("superSecretKey")
        .rememberMeCookieName("rememberMeCookieName")
        .rememberMeParameter("rememberMeParameter")
        .tokenValiditySeconds(60)
        .tokenRepository(persistentTokenRepository());

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
//    db.setCreateTableOnStartup(true); //Nen tao bang liquibase
    return db;
  }

}
