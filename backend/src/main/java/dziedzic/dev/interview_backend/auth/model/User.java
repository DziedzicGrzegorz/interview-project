package dziedzic.dev.interview_backend.auth.model;

import dziedzic.dev.interview_backend.auth.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    public User(UserDto userDto) {
        this.email = userDto.getEmail();
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
        this.role = Role.USER;
    }

    public User(UserDetails userDetails) {
        this.username = userDetails.getUsername();
        this.email = userDetails.getUsername();
        this.password = userDetails.getPassword();
        this.role = Role.USER;
    }


    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
