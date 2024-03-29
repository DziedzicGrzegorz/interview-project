package dziedzic.dev.interview_backend.exception.CustomExceptions;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String message) {
        super(message);
    }
}
