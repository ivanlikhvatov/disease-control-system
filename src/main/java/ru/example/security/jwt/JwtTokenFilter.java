package ru.example.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import ru.example.error.ApiError;
import ru.example.error.ErrorContainer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

//    @Autowired
//    @Qualifier("handlerExceptionResolver")
//    private HandlerExceptionResolver resolver;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

        StringUtils.isNotBlank(token);

        if (StringUtils.isNotBlank(token) && isValidToken(token, servletResponse)) {
            doAuthentication(token);
        }

//        filterChain.doFilter(servletRequest, servletResponse);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
//            HandlerExceptionResolver resolver = new DefaultHandlerExceptionResolver();
//            resolver.resolveException()

            int status = ErrorContainer.AUTHENTICATION_ERROR.getHttpStatus().value();
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            httpResponse.setStatus(status);
        }
    }

    private void doAuthentication(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private boolean isValidToken(String token, ServletResponse servletResponse) throws IOException {

        try {
            return jwtTokenProvider.validateToken(token);
        } catch (AuthenticationException e){
            ApiError apiError = buildAuthenticationError();

            int status = ErrorContainer.AUTHENTICATION_ERROR.getHttpStatus().value();
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            httpResponse.setStatus(status);
            httpResponse.getWriter().write(convertObjectToJson(apiError));
        }

        return false;
    }

    private ApiError buildAuthenticationError() {
        ApiError apiError = new ApiError();

        String message = ErrorContainer.AUTHENTICATION_ERROR.getMessage();
        int code = ErrorContainer.AUTHENTICATION_ERROR.getCode();

        apiError.setErrorCode(code);
        apiError.setMessage(message);

        return apiError;
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
