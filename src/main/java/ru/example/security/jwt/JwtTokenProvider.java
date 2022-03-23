package ru.example.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.example.dao.entity.user.Role;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.security.JwtUserDetailsService;
import ru.example.utils.DateConverter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final static String ROLES_LIST_NAME = "roles";
    private final static String CREDENTIALS = "";
    private final static String AUTHORIZATION_HEADER_NAME = "Authorization";
    private final static String BEARER_TOKEN_STARTS_STRING = "Bearer_";

    private final static Integer BEARER_TOKEN_STARTS_INDEX = 7;

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.duration.minutes}")
    private Long tokenDurationMinutes;

    private final JwtUserDetailsService jwtUserDetailsService;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String studentNumber, Set<Role> roles) {
        Claims claims = Jwts
                .claims()
                .setSubject(studentNumber);

        claims.put(ROLES_LIST_NAME, getRolesNames(roles));

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiredTime = currentTime.plusMinutes(tokenDurationMinutes);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(DateConverter.toDate(currentTime))
                .setExpiration(DateConverter.toDate(expiredTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String studentNumber = getStudentNumber(token);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(studentNumber);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                CREDENTIALS,
                userDetails.getAuthorities()
        );
    }

    public String getStudentNumber(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER_NAME);

        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(BEARER_TOKEN_STARTS_STRING)) {
            return bearerToken.substring(BEARER_TOKEN_STARTS_INDEX);
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);

            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new ApiException(ErrorContainer.AUTHENTICATION_ERROR, exception.getMessage());
        }
    }

    private List<String> getRolesNames(Set<Role> roles) {
        return Optional.ofNullable(roles)
                .orElse(Collections.emptySet())
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
