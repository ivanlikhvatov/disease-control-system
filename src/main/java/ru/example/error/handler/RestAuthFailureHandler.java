//package ru.example.error.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//import ru.example.error.ApiError;
//import ru.example.error.ErrorContainer;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@Component
//public class RestAuthFailureHandler implements AuthenticationFailureHandler {
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        ErrorContainer errorContainer = ErrorContainer.AUTHENTICATION_ERROR;
////        if (exception instanceof AuthenticationException) {
////            errorContainer = ErrorContainer.PAGE_NOT_FOUND;
////        }
//        ApiError apiError = new ApiError(errorContainer.getMessage(), errorContainer.getCode());
//
//        PrintWriter writer = response.getWriter();
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(errorContainer.getHttpStatus().value());
//        writer.write(objectMapper.writeValueAsString(apiError));
//    }
//}
