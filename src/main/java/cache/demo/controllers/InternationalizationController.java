package cache.demo.controllers;

import cache.demo.controllers.endpoint.DemoCacheEndpoint;
import cache.demo.dto.user.UserInternationalHelloRequest;
import cache.demo.exceptions.ApplicationException;
import cache.demo.exceptions.errorDetail.ErrorCode;
import cache.demo.exceptions.errorDetail.ErrorMessage;
import cache.demo.service.locale.LocaleService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(DemoCacheEndpoint.INTERNATIONALIZATION)
@Slf4j
public class InternationalizationController {

  private final LocaleService localeService;

  @GetMapping("/get-message")
  public String getInternationalMessage(HttpServletRequest request) {
    return localeService.getMessage("international.message", request);
  }

  @PostMapping("/get-message-with-validation'")
  public String getInternationalMessageWithValidation(@RequestBody @Valid UserInternationalHelloRequest request
      , BindingResult bindingResult
  ) throws ApplicationException {
    if (bindingResult.hasErrors()) {
      log.error("bindingResult has error!");
      Map<String, String> errors = new HashMap<>();

      bindingResult.getFieldErrors()
          .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

      for(Map.Entry<String, String> entry : errors.entrySet()){
        log.error("Field: {}, error message: {}", entry.getKey(), entry.getValue());
      }

      throw new ApplicationException(ErrorCode.BINDING_RESULT_ERROR, ErrorMessage.BINDING_RESULT_ERROR);
    }
    return localeService.getInternationalMessageWithValidation("international.user.say", request);
  }

  @GetMapping("/hello")
  public String generateInternationalHelloMessage(@RequestParam String name, @RequestParam Integer age) {
    return localeService.generateInternationalHelloMessage(name, age);
  }
}
