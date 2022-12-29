/*
 * Copyright 2022 SpeedX Team.
 */

package cache.demo.config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * WebMvcConfiguration class.
 *
 * <p>Contains information about Web MVC Configuration Object
 */
@Configuration
public class InternationalizationConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver ahlr = new AcceptHeaderLocaleResolver();
        ahlr.setDefaultLocale(Locale.US);
        return ahlr;
    }

    //detect any change in the user’s locale and then switch to the new locale.
//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
//        localeChangeInterceptor.setParamName("language");
//        return localeChangeInterceptor;
//    }

    //same with spring.messages.basename=language/messages in application.properties
    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("language1/messagesne");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(true);
        return messageSource;
    }

    @Bean
    public MessageSource errorMessageSource123() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("language2/errormessage");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(true);
//        messageSource.setAlwaysUseMessageFormat(true);
        return messageSource;
    }

}
