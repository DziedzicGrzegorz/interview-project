package dziedzic.dev.interview_backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class Health {

    @GetMapping("/health")
    public Map<String, Integer> get() {
        Map<String, Integer> response = new HashMap<>();
        response.put("status", 200);
        return response;
    }
}
