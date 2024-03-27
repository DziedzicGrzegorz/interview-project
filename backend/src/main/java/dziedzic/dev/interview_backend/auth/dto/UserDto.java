package dziedzic.dev.interview_backend.auth.dto;

import dziedzic.dev.interview_backend.auth.model.Role;
import dziedzic.dev.interview_backend.auth.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserDto extends BaseDto {

    public UserDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private Role role;
}
