package com.rabbit.mechanic.configuration;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Class for beans and configurations
 */
@Configuration
public class WebConfigs {

    /**
     * CORS configuration
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean corsFilter() {

        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter());
        bean.setOrder(0);
        return bean;
    }

    /**
     * PasswordEncoder configuration
     * Set the password encoder we want to use returned a generic {@link PasswordEncoder}
     * @return {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
