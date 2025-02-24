package mk.ukim.finki.ib.filesharing.DTO;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private long expiresIn;
}
