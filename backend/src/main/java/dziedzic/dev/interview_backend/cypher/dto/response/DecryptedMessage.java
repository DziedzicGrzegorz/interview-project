package dziedzic.dev.interview_backend.cypher.dto.response;

import dziedzic.dev.interview_backend.cypher.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class DecryptedMessage {
    private UUID id;
    private LocalDateTime createdDateTime;
    private String message;
    private String messageIdentifier;
    private boolean Decrypted;

    public DecryptedMessage(Message messageFromRepository, String decryptedMessage) {
        this.id = messageFromRepository.getId();
        this.createdDateTime = messageFromRepository.getCreatedDateTime();
        this.message = decryptedMessage;
        this.messageIdentifier = messageFromRepository.getMessageIdentifier();
        this.Decrypted = messageFromRepository.isDecrypted();
    }
}