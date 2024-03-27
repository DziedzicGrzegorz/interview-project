package dziedzic.dev.interview_backend.auth.exception.CustomExceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

