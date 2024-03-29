package dziedzic.dev.interview_backend.cypher.service;

import dziedzic.dev.interview_backend.auth.model.User;
import dziedzic.dev.interview_backend.auth.service.UserService;
import dziedzic.dev.interview_backend.cypher.dto.request.EncryptedMessageRequest;
import dziedzic.dev.interview_backend.cypher.dto.request.ToEncryptMessageRequest;
import dziedzic.dev.interview_backend.cypher.dto.response.DecryptedMessage;
import dziedzic.dev.interview_backend.cypher.dto.response.EncryptedMessage;
import dziedzic.dev.interview_backend.cypher.event.DataDecryptedEvent;
import dziedzic.dev.interview_backend.cypher.model.Message;
import dziedzic.dev.interview_backend.cypher.repository.MessageRepository;
import dziedzic.dev.interview_backend.exception.CustomExceptions.AccessDeniedException;
import dziedzic.dev.interview_backend.exception.CustomExceptions.DecryptionContentMismatchException;
import dziedzic.dev.interview_backend.exception.CustomExceptions.EncryptionIssueException;
import dziedzic.dev.interview_backend.exception.CustomExceptions.MessageNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.crypto.AEADBadTagException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CypherService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;


    public List<Message> getAllMessages(HttpServletRequest request) {
        User user = userService.getUserByRequestOrNull(request);
        return messageRepository.findAllByUserId(user.getId());
    }

    //as above but only one
    @GetMapping()
    public Message getMessageById(UUID id, HttpServletRequest request) {
        User user = userService.getUserByRequestOrNull(request);
        if (user == null) {
            throw new AccessDeniedException("Access denied");
        }
        return messageRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new MessageNotFoundException("Message not found")
        );
    }

    public EncryptedMessage encryptAndSaveMessage(ToEncryptMessageRequest requestBody, HttpServletRequest request) {
        User user = userService.getUserByRequestOrNull(request);

        try {
            String encryptedMessage = CryptoManager.encrypt(requestBody.getMessage(), requestBody.getPassword());

            Message message = Message.builder()
                    .encryptedMessage(encryptedMessage)
                    .user(user)
                    .messageIdentifier(requestBody.getMessageIdentifier())
                    .decrypted(false)
                    .build();

            return new EncryptedMessage(messageRepository.save(message));

        } catch (Exception e) {
            throw new EncryptionIssueException("Encryption failed");
        }
    }

    public DecryptedMessage decryptAndUpdateMessage(EncryptedMessageRequest requestBody) {

        Message message = messageRepository.findById(requestBody.getId()).orElseThrow(
                () -> new MessageNotFoundException("Message not found")
        );

        if (!Objects.equals(message.getEncryptedMessage(), requestBody.getEncryptedMessage())) {
            throw new DecryptionContentMismatchException("Content mismatch");
        }

        try {
            String decryptedMessage = CryptoManager.decrypt(requestBody.getEncryptedMessage(), requestBody.getPassword());

            messageRepository.updateDecryptedToTrueById(requestBody.getId());

            eventPublisher.publishEvent(new DataDecryptedEvent(this, message.getId()));

            return new DecryptedMessage(message, decryptedMessage);

        } catch (AEADBadTagException e) {
            throw new DecryptionContentMismatchException("Wrong Passwords");
        } catch (Exception e) {
            throw new EncryptionIssueException("Decryption failed: " + e.getMessage());
        }
    }
}