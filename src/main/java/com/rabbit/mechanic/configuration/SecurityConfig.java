package com.rabbit.mechanic.configuration;

import com.rabbit.mechanic.security.AuthorizationValidatorService;
import com.rabbit.mechanic.security.EmployeeAuthenticationEntryPoint;
import com.rabbit.mechanic.security.EmployeeAuthenticationProvider;
import com.rabbit.mechanic.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Class for security configurations
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final EmployeeAuthenticationEntryPoint employeeAuthenticationEntryPoint;
    private final EmployeeAuthenticationProvider employeeAuthenticationProvider;

    public SecurityConfig(EmployeeAuthenticationEntryPoint employeeAuthenticationEntryPoint, EmployeeAuthenticationProvider employeeAuthenticationProvider) {
        this.employeeAuthenticationEntryPoint = employeeAuthenticationEntryPoint;
        this.employeeAuthenticationProvider = employeeAuthenticationProvider;
    }

    /**
     * Override HttpSecurity configs
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .exceptionHandling().authenticationEntryPoint(employeeAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new JwtAuthFilter(employeeAuthenticationProvider), BasicAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/auth/login", "/employees").permitAll()
                .anyRequest().authenticated();

    }

    @Bean
    public AuthorizationValidatorService authorized() {
        return new AuthorizationValidatorService();
    }
}
