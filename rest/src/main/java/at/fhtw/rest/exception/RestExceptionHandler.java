package at.fhtw.rest.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

@ControllerAdvice
public class RestExceptionHandler {

    private ResponseEntity<ErrorDto> buildBody(HttpStatus status, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(ErrorDto.of(status, message), headers, status);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFound(DocumentNotFoundException ex) {
        return buildBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgument(IllegalArgumentException ex) {
        return buildBody(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
    public ResponseEntity<ErrorDto> handleUnsupportedMediaType(UnsupportedMediaTypeStatusException ex) {
        return buildBody(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ExceptionHandler(DocumentMessagingException.class)
    public ResponseEntity<ErrorDto> handleDocumentMessaging(DocumentMessagingException ex) {
        return buildBody(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler(DocumentStorageException.class)
    public ResponseEntity<ErrorDto> handleDocumentStorage(DocumentStorageException ex) {
        return buildBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneric(Exception ex) {
        return buildBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
