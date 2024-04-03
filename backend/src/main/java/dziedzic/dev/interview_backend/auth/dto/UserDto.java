package dziedzic.dev.interview_backend.auth.dto;

import dziedzic.dev.interview_backend.auth.model.Role;
import dziedzic.dev.interview_backend.auth.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserDto {

    public UserDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.createdDateTime = user.getCreatedDateTime();
    }

    @NotBlank(message = "The email is required.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email address.")
    private String email;
    @NotBlank(message = "The username is required.")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,}$", message = "Username must be at least 3 characters long.")
    private String username;
    @NotBlank(message = "The password is required.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,100}$", message = "Password must be 8 to 100 characters long and include a combination of uppercase letters, lowercase letters, numbers, and special characters.")
    private String password;

    private LocalDateTime createdDateTime;

    private Role role;
}
