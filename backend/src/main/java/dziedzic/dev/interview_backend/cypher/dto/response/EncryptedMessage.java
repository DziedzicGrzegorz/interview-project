package dziedzic.dev.interview_backend.cypher.dto.response;

import dziedzic.dev.interview_backend.cypher.model.Message;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EncryptedMessage {
    private UUID id;
    private LocalDateTime createdDateTime;
    private String encryptedMessage;
    private String messageIdentifier;
    private boolean Decrypted;

    public EncryptedMessage(Message message) {
        this.id = message.getId();
        this.createdDateTime = message.getCreatedDateTime();
        this.encryptedMessage = message.getEncryptedMessage();
        this.messageIdentifier = message.getMessageIdentifier();
        this.Decrypted = message.isDecrypted();
    }
}