package ru.example.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.example.error.ApiError;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final static String CONTENT_TYPE = "application/json";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e
            ) throws IOException, ServletException
    {
        ApiError apiError = buildAuthenticationError();
        createResponse(response, apiError);

        log.warn(
                "URI request: {}, HttpStatus: {} {}: {} ",
                request.getRequestURI(),
                response.getStatus(),
                ApiException.class.getSimpleName(),
                apiError.getMessage()
        );
    }

    private void createResponse(HttpServletResponse response, ApiError apiError) throws IOException {
        int status = ErrorContainer.AUTHENTICATION_ERROR.getHttpStatus().value();

        response.setStatus(status);
        response.setContentType(CONTENT_TYPE);

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, apiError);
        out.flush();
    }

    private ApiError buildAuthenticationError() {
        ApiError apiError = new ApiError();

        String message = ErrorContainer.AUTHENTICATION_ERROR.getMessage();
        int code = ErrorContainer.AUTHENTICATION_ERROR.getCode();

        apiError.setErrorCode(code);
        apiError.setMessage(message);

        return apiError;
    }
}