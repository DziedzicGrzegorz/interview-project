package dziedzic.dev.interview_backend.exception.CustomExceptions;

public class DecryptionContentMismatchException extends RuntimeException {
    public DecryptionContentMismatchException(String message) {
        super(message);
    }
}