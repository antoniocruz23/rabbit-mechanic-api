package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.auth.CredentialsDto;
import com.rabbit.mechanic.command.auth.LoggedInDto;
import com.rabbit.mechanic.command.auth.PrincipalDto;
import com.rabbit.mechanic.converter.EmployeeConverter;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.WrongCredentialsException;
import com.rabbit.mechanic.exception.employee.EmployeeNotFoundException;
import com.rabbit.mechanic.persistence.entity.EmployeeEntity;
import com.rabbit.mechanic.persistence.repository.EmployeeRepository;
import com.rabbit.mechanic.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link AuthService} implementation
 */
@Service
public class AuthServiceImp implements AuthService {

    //Logger
    private static final Logger LOGGER = LogManager.getLogger(AuthServiceImp.class);

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    private String secretKey;

    public AuthServiceImp(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, JwtProperties jwtProperties) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProperties = jwtProperties;
    }


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    /**
     * @see AuthService#loginEmployee(CredentialsDto)
     */
    @Override
    public LoggedInDto loginEmployee(CredentialsDto credentialsDto) {

        // Get employee by username
        EmployeeEntity employeeEntity =
                employeeRepository.findByUsername(credentialsDto.getUsername())
                        .orElseThrow(() -> {
                            LOGGER.error("Employee with username {} not found on database", credentialsDto.getUsername());
                            return new WrongCredentialsException(ErrorMessages.WRONG_CREDENTIALS);
                        });

        // Verify password
        boolean passwordMatches = passwordEncoder.matches(credentialsDto.getPassword(), employeeEntity.getEncryptedPassword());
        if (!passwordMatches) {
            LOGGER.error("The password doesn't match");
            throw new WrongCredentialsException(ErrorMessages.WRONG_CREDENTIALS);
        }

        // Build principal for the logged in employee
        PrincipalDto principal = EmployeeConverter.fromEmployeeEntityToPrincipalDto(employeeEntity);

        // Get JWT token
        LOGGER.info("Generating JWT token for the employee with id {}...", employeeEntity.getEmployeeId());
        String token = createJwtToken(principal);

        // Build LoggedInDto for the response
        return LoggedInDto.builder()
                .principal(principal)
                .token(token)
                .build();
    }

    /**
     * @see AuthService#validateToken(String)
     */
    @Override
    public PrincipalDto validateToken(String token) {

        // Parse token (if the token is has expired or has an invalid signature it will throw an exception)
        Jws<Claims> jwtClaims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);

        // Get employeeId from payload/body
        Long employeeId = jwtClaims.getBody()
                .get("employeeId", Long.class);

        // Get employee from database
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND));

        // Build principalDto
        return EmployeeConverter.fromEmployeeEntityToPrincipalDto(employeeEntity);
    }

    /**
     * Helper to create JWT Token
     * @param principalDto
     * @return the token as {@link String}
     */
    private String createJwtToken(PrincipalDto principalDto) {
        // Set claims
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("employeeId", principalDto.getEmployeeId());
        claimsMap.put("firstName", principalDto.getFirstName());
        claimsMap.put("lastName", principalDto.getLastName());
        claimsMap.put("role", principalDto.getEmployeeRole());

        Claims claims = Jwts.claims(claimsMap);

        // Calculate issued at and expire at
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + jwtProperties.getExpiresIn());

        // Build jwt token and compact as string
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
