package dziedzic.dev.interview_backend.cypher.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class EncryptedMessageRequest {
    @NotNull(message = "The id is required")
    private UUID id;
    @NotBlank(message = "The encrypted message is required.")
    private String encryptedMessage;
    @NotBlank(message = "The password is required.")
    private String password;
}