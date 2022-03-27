package ru.example.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

        StringUtils.isNotBlank(token);

        if (StringUtils.isNotBlank(token) && isValidToken(token)) {
            doAuthentication(token);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void doAuthentication(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private boolean isValidToken(String token) {
        try {
            return jwtTokenProvider.validateToken(token);
        } catch (AuthenticationException e){
            return false;
        }
    }
}
