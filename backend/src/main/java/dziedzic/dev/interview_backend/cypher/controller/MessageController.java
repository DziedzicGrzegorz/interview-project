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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final CypherService cypherService;

    @GetMapping
    public List<EncryptedMessage> getAllMessages(HttpServletRequest request) {
        List<Message> temp = cypherService.getAllMessages(request);
        return temp.stream().map(EncryptedMessage::new).toList();
    }

    @GetMapping("/{id}")
    public EncryptedMessage getMessageById(@PathVariable("id") UUID id, HttpServletRequest request) {
        return new EncryptedMessage(cypherService.getMessageById(id, request));
    }

    @PostMapping("/encrypt")
    public EncryptedMessage encrypt(@Valid @RequestBody ToEncryptMessageRequest requestBody, HttpServletRequest request) {
        return cypherService.encryptAndSaveMessage(requestBody, request);
    }

    @PostMapping("/decrypt")
    public DecryptedMessage decrypt(@Valid @RequestBody EncryptedMessageRequest requestBody) {
        return cypherService.decryptAndUpdateMessage(requestBody);
    }
}