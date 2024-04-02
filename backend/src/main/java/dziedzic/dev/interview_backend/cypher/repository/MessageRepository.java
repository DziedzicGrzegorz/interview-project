package dziedzic.dev.interview_backend.cypher.repository;

import dziedzic.dev.interview_backend.cypher.model.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findAllByUserId(UUID userId);

    Optional<Message> findByIdAndUserId(UUID messageId, UUID userId);

    void deleteAllByUserId(UUID userId);

    void deleteMessageById(UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.decrypted = true WHERE m.id = :id")
    void updateDecryptedToTrueById(UUID id);
}
