package dziedzic.dev.interview_backend.cypher.model;

import dziedzic.dev.interview_backend.auth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false)
    @ColumnDefault("gen_random_uuid()")
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private String encryptedMessage;

    @Column
    private String messageIdentifier;

    //column to check if message was decrypted
    @Column
    private boolean decrypted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

}