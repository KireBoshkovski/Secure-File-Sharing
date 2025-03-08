package mk.ukim.finki.ib.filesharing.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.model.Token;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.repository.TokenRepository;
import mk.ukim.finki.ib.filesharing.service.TokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
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
        this.tokenRepository.save(new Token(token, user, LocalDateTime.now().plusMinutes(30)));
        return token;
    }

    @Transactional
    @Override
    public void removeTokens(User user) {
        this.tokenRepository.deleteByUser(user);
    }

    @Override
    public Optional<Token> findToken(String token) {
        return this.tokenRepository.findById(token);
    }

    @Scheduled(fixedRate = 30 * 60 * 1000) // 30 minutes * 60 seconds * 1000 milliseconds
    private void deleteExpiredTokens() {
        this.tokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
        System.out.println("Expired tokens have been deleted");
    }
}
