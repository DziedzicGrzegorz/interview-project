package dziedzic.dev.interview_backend.exception.CustomExceptions;

public class EncryptionIssueException extends RuntimeException {
    public EncryptionIssueException(String message) {
        super(message);
    }
}
