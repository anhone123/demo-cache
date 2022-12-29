package cache.demo.service.locale;

import cache.demo.dto.user.UserInternationalHelloRequest;
import javax.servlet.http.HttpServletRequest;

public interface LocaleService {

  String getMessage(String code, HttpServletRequest request);

  String getInternationalMessageWithValidation(String msgCode, UserInternationalHelloRequest request);

  String generateInternationalHelloMessage(String name, Integer age);

  String generateInternationalErrorMessage(String baseMessageCode);

}
