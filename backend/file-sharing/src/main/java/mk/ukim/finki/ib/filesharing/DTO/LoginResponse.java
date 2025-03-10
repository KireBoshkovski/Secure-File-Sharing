package mk.ukim.finki.ib.filesharing.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String token;
    private long expiresIn;
}
