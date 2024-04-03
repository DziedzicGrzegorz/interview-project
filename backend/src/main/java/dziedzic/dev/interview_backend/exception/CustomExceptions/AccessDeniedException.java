package dziedzic.dev.interview_backend.exception.CustomExceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}