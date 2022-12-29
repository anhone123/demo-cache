package cache.demo.service.locale;

import cache.demo.dto.user.UserInternationalHelloRequest;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {

  private final MessageSource messageSource;
  private final MessageSource errorMessageSource123;
  private final LocaleResolver localeResolver;

//  private static final ResourceBundleMessageSource errorMessageSource123 = new ResourceBundleMessageSource();
//  static {
//    errorMessageSource123.setBasenames("language2/errormessage");
//    errorMessageSource123.setDefaultEncoding("UTF-8");
//    errorMessageSource123.setFallbackToSystemLocale(true);
//  }

  @Override
  public String getMessage(String code, HttpServletRequest request) {
    return messageSource.getMessage(code, null, localeResolver.resolveLocale(request));
  }

  @Override
  public String getInternationalMessageWithValidation(String msgCode, UserInternationalHelloRequest request) {
    Object[] textes = {request.getName(), request.getAge(), request.getWords()};
    Locale locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(msgCode, textes, locale);
  }

  @Override
  public String generateInternationalHelloMessage( String name, Integer age) {
    Locale locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage("international.hello", new Object[]{name, age}, locale);
  }

  @Override
  public String generateInternationalErrorMessage(String baseMessageCode) {
    if (baseMessageCode == null || baseMessageCode.isBlank()) {
      return null;
    }
    return errorMessageSource123.getMessage(baseMessageCode, null, LocaleContextHolder.getLocale());
  }


  //Guild format https://www.baeldung.com/java-localization-messages-formatting
}
