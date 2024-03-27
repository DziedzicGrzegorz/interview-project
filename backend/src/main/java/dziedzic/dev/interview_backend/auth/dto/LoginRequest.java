package dziedzic.dev.interview_backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String username;
    @NotBlank
    private String password;
}
