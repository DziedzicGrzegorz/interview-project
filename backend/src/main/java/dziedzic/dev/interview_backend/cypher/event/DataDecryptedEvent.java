package dziedzic.dev.interview_backend.cypher.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

/**
 * This class represents the event of data being decrypted.
 * It is used to notify the application that the data has been decrypted.
 */
@Getter
@Setter
public class DataDecryptedEvent extends ApplicationEvent {
    private UUID id;

    public DataDecryptedEvent(Object source, UUID id) {
        super(source);
        this.id = id;
    }
}