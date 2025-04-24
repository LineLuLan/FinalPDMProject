package backend.main.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Integer userId;
    private String email;
    private String password;
    private String role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
