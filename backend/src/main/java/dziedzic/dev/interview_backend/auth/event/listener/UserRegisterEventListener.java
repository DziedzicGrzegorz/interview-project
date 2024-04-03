package dziedzic.dev.interview_backend.auth.event.listener;

import dziedzic.dev.interview_backend.auth.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserRegisterEventListener implements ApplicationListener<UserRegisterEvent> {

    @Override
    public void onApplicationEvent(UserRegisterEvent event) {
        log.info("user registered: {}", event.getUser().getEmail());
    }
}
