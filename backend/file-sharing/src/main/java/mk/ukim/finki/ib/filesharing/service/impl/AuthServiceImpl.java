package mk.ukim.finki.ib.filesharing.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.DTO.LoginRequest;
import mk.ukim.finki.ib.filesharing.DTO.RegisterRequest;
import mk.ukim.finki.ib.filesharing.model.Token;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.repository.UserRepository;
import mk.ukim.finki.ib.filesharing.service.AuthService;
import mk.ukim.finki.ib.filesharing.service.EmailService;
import mk.ukim.finki.ib.filesharing.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;

    @Override
    public User authenticate(LoginRequest request) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        return this.userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s is not found!", request.getUsername())));
    }

    @Override
    public Optional<User> registerUser(String token) {
        Optional<Token> tokenOptional = this.tokenService.findToken(token);
        if (tokenOptional.isPresent()) {
            User user = tokenOptional.get().getUser();
            user.setVerified(true);
            this.tokenService.removeToken(tokenOptional.get());
            return Optional.of(this.userRepository.save(user));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> sendConfirmationEmail(RegisterRequest request) {
        if (validateUser(request)) {
            User user = this.userRepository.save(new User(request.getUsername(), request.getEmail(),
                    this.passwordEncoder.encode(request.getPassword())));

            String token = this.tokenService.generateToken(user);

            this.emailService.sendConfirmationCode(request.getEmail(), token);

            return Optional.of(user);
        }
        return Optional.empty();
    }

    private boolean validateUser(RegisterRequest request) {
        return (request.getUsername() != null && !request.getUsername().isEmpty() && request.getPassword() != null && !request.getPassword().isEmpty())
                && (this.userRepository.findByUsername(request.getUsername()).isEmpty() || !this.userRepository.findByUsername(request.getUsername()).get().isVerified())
                && (this.userRepository.findByEmail(request.getEmail()).isEmpty() || !this.userRepository.findByEmail(request.getEmail()).get().isVerified());
    }
}
