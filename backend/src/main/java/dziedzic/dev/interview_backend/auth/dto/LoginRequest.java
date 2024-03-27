package dziedzic.dev.interview_backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email address.")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9]{3,}$", message = "Username must be at least 3 characters long.")
    private String username;
    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,100}$", message = "Password must be 8 to 100 characters long and include a combination of uppercase letters, lowercase letters, numbers, and special characters.")
    private String password;
}
