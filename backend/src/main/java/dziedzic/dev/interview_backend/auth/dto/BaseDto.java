package dziedzic.dev.interview_backend.auth.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
public class BaseDto {


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdDateTime;

}
