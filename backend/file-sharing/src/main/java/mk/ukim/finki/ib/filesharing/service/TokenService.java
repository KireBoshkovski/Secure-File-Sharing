package mk.ukim.finki.ib.filesharing.service;

import mk.ukim.finki.ib.filesharing.model.Token;
import mk.ukim.finki.ib.filesharing.model.User;

import java.util.Optional;

public interface TokenService {
    String generateToken(User user);

    void removeTokens(User user);

    Optional<Token> findToken(String token);
}
