package dziedzic.dev.interview_backend.auth;

import dziedzic.dev.interview_backend.InterviewBackendApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(InterviewBackendApplication.class);
    }
}
