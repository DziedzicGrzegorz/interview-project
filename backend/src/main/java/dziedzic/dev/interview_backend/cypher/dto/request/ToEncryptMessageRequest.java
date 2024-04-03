package dziedzic.dev.interview_backend.cypher.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ToEncryptMessageRequest {
    @NotBlank(message = "The message is required.")
    private String message;
    @NotBlank(message = "The password is required.")
    private String password;
    @NotBlank(message = "The message identifier is required.")
    private String messageIdentifier;
}