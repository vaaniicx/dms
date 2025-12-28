package at.fhtw.rest.exception;

import at.fhtw.rest.core.exception.DocumentMessagingException;
import at.fhtw.rest.core.exception.DocumentNotFoundException;
import at.fhtw.rest.core.exception.DocumentStorageException;
import com.openapi.gen.springboot.dto.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFound(DocumentNotFoundException ex) {
        return buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponseBody(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
    public ResponseEntity<ErrorDto> handleUnsupportedMediaType(UnsupportedMediaTypeStatusException ex) {
        return buildResponseBody(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ExceptionHandler(DocumentMessagingException.class)
    public ResponseEntity<ErrorDto> handleDocumentMessaging(DocumentMessagingException ex) {
        return buildResponseBody(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler({Exception.class, DocumentStorageException.class})
    public ResponseEntity<ErrorDto> handleDocumentStorage(DocumentStorageException ex) {
        return buildResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErrorDto> buildResponseBody(HttpStatus status, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(status.value());
        errorDto.setMessage(message);
        return new ResponseEntity<>(errorDto, headers, status);
    }
}
