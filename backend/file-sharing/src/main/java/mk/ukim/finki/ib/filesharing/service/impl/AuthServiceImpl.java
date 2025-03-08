package mk.ukim.finki.ib.filesharing.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.DTO.LoginRequest;
import mk.ukim.finki.ib.filesharing.DTO.RegisterRequest;
import mk.ukim.finki.ib.filesharing.model.Token;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.model.exceptions.EmailAlreadyExistsException;
import mk.ukim.finki.ib.filesharing.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.ib.filesharing.repository.UserRepository;
import mk.ukim.finki.ib.filesharing.service.AuthService;
import mk.ukim.finki.ib.filesharing.service.EmailService;
import mk.ukim.finki.ib.filesharing.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        return (User) this.authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()))
                .getPrincipal();
    }

    @Override
    public boolean sendOTP(LoginRequest request) {
        Optional<User> userOptional = this.userRepository.findByUsername(request.getUsername());
        if (userOptional.isPresent()) {
            String token = this.tokenService.generateToken(userOptional.get());

            this.emailService.send2FACode(userOptional.get().getEmail(), token);
        }
        return false;
    }

    @Override
    public Optional<User> registerUser(String token) {
        Optional<Token> tokenOptional = this.tokenService.findToken(token);
        if (tokenOptional.isPresent()) {
            User user = tokenOptional.get().getUser();
            user.setVerified(true);
            this.tokenService.removeTokens(user);
            return Optional.of(this.userRepository.save(user));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> sendConfirmationEmail(RegisterRequest request) {
        validateUser(request);

        User user = this.userRepository.save(new User(
                request.getUsername(),
                request.getEmail(),
                this.passwordEncoder.encode(request.getPassword()))
        );

        String token = this.tokenService.generateToken(user);
        this.emailService.sendConfirmationCode(request.getEmail(), token);

        return Optional.of(user);
    }

    private void validateUser(RegisterRequest request) {
        Optional<User> existingUserByUsername = this.userRepository.findByUsername(request.getUsername());
        if (existingUserByUsername.isPresent() && existingUserByUsername.get().isVerified()) {
            throw new UsernameAlreadyExistsException();
        }

        Optional<User> existingUserByEmail = this.userRepository.findByEmail(request.getEmail());
        if (existingUserByEmail.isPresent() && existingUserByEmail.get().isVerified()) {
            throw new EmailAlreadyExistsException();
        }
    }
}
