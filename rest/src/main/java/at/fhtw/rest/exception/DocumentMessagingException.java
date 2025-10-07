package at.fhtw.rest.exception;

/**
 * Signals failures while interacting with the messaging infrastructure.
 */
public class DocumentMessagingException extends RuntimeException {

    public DocumentMessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}
