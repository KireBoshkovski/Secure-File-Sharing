package mk.ukim.finki.ib.filesharing.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    long getExpirationTime();

    String extractUsername(String token);
}
