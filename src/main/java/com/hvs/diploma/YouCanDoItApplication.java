package com.hvs.diploma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
public class YouCanDoItApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouCanDoItApplication.class, args);
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Kiev")));
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

}
