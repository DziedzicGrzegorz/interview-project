package dziedzic.dev.interview_backend.config;

import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BucketConfiguration {

    @Bean
    public Bucket bucket() {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(20).refillGreedy(1, Duration.ofSeconds(1)))
                .build();
    }
}
