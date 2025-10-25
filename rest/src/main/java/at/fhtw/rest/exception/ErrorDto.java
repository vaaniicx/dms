package at.fhtw.rest.exception;

import java.time.Instant;

public record ErrorDto(String timestamp, int status, String error, String message) {
    public static ErrorDto of(org.springframework.http.HttpStatus status, String message) {
        return new ErrorDto(Instant.now().toString(), status.value(), status.getReasonPhrase(), message);
    }
}
