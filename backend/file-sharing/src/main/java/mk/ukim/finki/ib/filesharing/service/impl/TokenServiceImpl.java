package mk.ukim.finki.ib.filesharing.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.model.Token;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.repository.TokenRepository;
import mk.ukim.finki.ib.filesharing.service.TokenService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public String generateToken(User user) {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);

        String token = String.valueOf(code);
        this.tokenRepository.save(new Token(token, user));
        return token;
    }

    @Override
    public void removeToken(Token token) {
        this.tokenRepository.delete(token);
    }

    @Override
    public Optional<Token> findToken(String token) {
        return this.tokenRepository.findById(token);
    }
}
