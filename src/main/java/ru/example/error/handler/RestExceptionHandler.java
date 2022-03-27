package ru.example.error.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.example.error.ApiError;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiError> handleApiException(ApiException ex, HttpServletRequest request) {
        if (isInternalError(ex)) {
            ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR_MESSAGE, ErrorContainer.OTHER.getCode());
            return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String message = ex.getMessage();
        String className = ex.getClass().getSimpleName();
        HttpStatus status = ex.getError().getHttpStatus();

        log.warn("URI request: {}, HttpStatus: {} {}: {} ", request.getRequestURI(), status, className, message);
        int code = ex.getError().getCode();
        ApiError apiError = new ApiError(message, code);
        return new ResponseEntity<>(apiError, ex.getError().getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable cause = ex.getCause();
        if (this.isBodyNotPresented(ex)) {
            return this.handleEmptyBody();
        } else if (cause instanceof InvalidFormatException) {
            return this.handleInvalidFormat((InvalidFormatException)cause, headers, status, request);
        } else if (cause instanceof MismatchedInputException) {
            return this.handleInvalidFiledType((MismatchedInputException)cause);
        } else {
            return cause instanceof JsonMappingException ? this.handleJsonMappingException((JsonMappingException)cause, headers, status, request) : super.handleHttpMessageNotReadable(ex, headers, status, request);
        }
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> generalHandler(Exception ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR_MESSAGE, ErrorContainer.OTHER.getCode());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ApiError> handlePathVariableNotValid(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage());
        }
        ApiError apiError = new ApiError(strBuilder.toString(), ErrorContainer.BAD_REQUEST.getCode());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError validationError = new ApiError();
        validationError.setMessage(ex.getMessage());
        validationError.setErrorCode(ErrorContainer.BAD_REQUEST.getCode());
        return new ResponseEntity<>(validationError, headers, ErrorContainer.BAD_REQUEST.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError validationError = createValidationError(ex);
        return new ResponseEntity<>(validationError, headers, ErrorContainer.BAD_REQUEST.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError validationError = new ApiError();
        validationError.setMessage(ex.getMostSpecificCause().getMessage());
        validationError.setErrorCode(ErrorContainer.BAD_REQUEST.getCode());
        return new ResponseEntity<>(validationError, headers, ErrorContainer.BAD_REQUEST.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ApiError createValidationError(MethodArgumentNotValidException e) {
        return fromBindingErrors(e.getBindingResult());
    }

    private ApiError fromBindingErrors(Errors errors) {
        StringBuilder builder = new StringBuilder();
        for (ObjectError objectError : errors.getAllErrors()) {
            if (objectError instanceof FieldError) {
                builder
                        .append(((FieldError) objectError).getField())
                        .append(" ")
                        .append(objectError.getDefaultMessage())
                        .append(".");
            }
        }
        return new ApiError(builder.toString(), ErrorContainer.BAD_REQUEST.getCode());
    }

    private boolean isInternalError(ApiException ex) {
        return ex.getError().getHttpStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isBodyNotPresented(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        String message = ex.getMessage();
        if (!Objects.isNull(cause)) {
            return false;
        } else {
            return Objects.nonNull(message) && message.contains("Required request body is missing");
        }
    }

    private ResponseEntity<Object> handleEmptyBody() {
        ApiError error = new ApiError("Required request body is missing", ErrorContainer.BAD_REQUEST.getCode());
        return new ResponseEntity<>(error, (MultiValueMap)null, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleInvalidFiledType(MismatchedInputException ex) {
        List<JsonMappingException.Reference> path = ex.getPath();
        String field = (String) Optional.ofNullable(path.get(path.size() - 1)).map(JsonMappingException.Reference::getFieldName).orElse("");
        String errorMessage = String.format("Invalid type of field '%s'", field);
        ApiError apiError = new ApiError(errorMessage, ErrorContainer.BAD_REQUEST.getCode());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String value = ex.getValue().toString();
        String field = (String)Optional.ofNullable(ex.getPath().get(ex.getPath().size() - 1)).map(JsonMappingException.Reference::getFieldName).orElse("");
        String errorMessage = String.format("Cannot deserialize field:'%s' with value:'%s'", field, value);
        ApiError apiError = new ApiError(errorMessage, ErrorContainer.BAD_REQUEST.getCode());
        return new ResponseEntity<>(apiError, (MultiValueMap)null, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleJsonMappingException(JsonMappingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<JsonMappingException.Reference> path = ex.getPath();
        String field = Optional.ofNullable(path.get(path.size() - 1))
                .map(JsonMappingException.Reference::getFieldName)
                .orElse("");
        String errorMessage = String.format("Bad request, incorrect field: '%s'", field);
        ApiError apiError = new ApiError(errorMessage, ErrorContainer.BAD_REQUEST.getCode());
        return new ResponseEntity<>(apiError, null, HttpStatus.BAD_REQUEST);
    }
}
