package dziedzic.dev.interview_backend.cypher.event.listener;

import dziedzic.dev.interview_backend.cypher.event.DataDecryptedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataDecryptedEventListener implements ApplicationListener<DataDecryptedEvent> {

    @Override
    public void onApplicationEvent(DataDecryptedEvent event) {
        log.info("Message decrypted: {}", event.getId());
    }

}