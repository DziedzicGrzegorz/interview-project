package dziedzic.dev.interview_backend.cypher.controller;

import dziedzic.dev.interview_backend.cypher.dto.request.EncryptedMessageRequest;
import dziedzic.dev.interview_backend.cypher.dto.request.ToEncryptMessageRequest;
import dziedzic.dev.interview_backend.cypher.dto.response.DecryptedMessage;
import dziedzic.dev.interview_backend.cypher.dto.response.EncryptedMessage;
import dziedzic.dev.interview_backend.cypher.model.Message;
import dziedzic.dev.interview_backend.cypher.service.CypherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final CypherService cypherService;

    @GetMapping
    public ResponseEntity<List<EncryptedMessage>> getAllMessages(HttpServletRequest request) {
        List<Message> messages = cypherService.getAllMessages(request);
        List<EncryptedMessage> encryptedMessages = messages.stream().map(EncryptedMessage::new).toList();
        return ResponseEntity.ok(encryptedMessages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncryptedMessage> getMessageById(@PathVariable("id") UUID id, HttpServletRequest request) {
        Message message = cypherService.getMessageById(id, request);
        EncryptedMessage encryptedMessage = new EncryptedMessage(message);
        return ResponseEntity.ok(encryptedMessage);
    }

    @PostMapping("/encrypt")
    public ResponseEntity<EncryptedMessage> encrypt(@Valid @RequestBody ToEncryptMessageRequest requestBody, HttpServletRequest request) {
        EncryptedMessage encryptedMessage = cypherService.encryptAndSaveMessage(requestBody, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(encryptedMessage);
    }

    @PostMapping("/decrypt")
    public ResponseEntity<DecryptedMessage> decrypt(@Valid @RequestBody EncryptedMessageRequest requestBody) {
        DecryptedMessage decryptedMessage = cypherService.decryptAndUpdateMessage(requestBody);
        return ResponseEntity.ok(decryptedMessage);
    }
}