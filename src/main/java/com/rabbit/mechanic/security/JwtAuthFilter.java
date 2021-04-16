package com.rabbit.mechanic.security;

import com.rabbit.mechanic.command.auth.PrincipalDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Jwt authentication filter
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LogManager.getLogger(JwtAuthFilter.class);
    private final EmployeeAuthenticationProvider employeeAuthenticationProvider;

    public JwtAuthFilter(EmployeeAuthenticationProvider employeeAuthenticationProvider) {
        this.employeeAuthenticationProvider = employeeAuthenticationProvider;
    }

    /**
     * Filter implementation to authenticate with jwt token if provided
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (Objects.nonNull(header)) {
            String[] authElements = header.split(" ");

            if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                try {
                    Authentication auth = employeeAuthenticationProvider.validateToken(authElements[1]);

                    SecurityContextHolder.getContext().setAuthentication(auth);

                    LOGGER.info("Employee id {} successfully authenticated via token",
                            ((PrincipalDto) auth.getPrincipal()).getEmployeeId());
                } catch (RuntimeException e) {
                    SecurityContextHolder.clearContext();
                    LOGGER.error("Failed to parse token", e);
                    throw e;
                }
            }
        }

        // Allways call this in order to tell Spring Security to continue
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
