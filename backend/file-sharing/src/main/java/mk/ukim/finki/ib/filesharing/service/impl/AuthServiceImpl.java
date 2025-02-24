package mk.ukim.finki.ib.filesharing.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.DTO.LoginRequest;
import mk.ukim.finki.ib.filesharing.DTO.RegisterRequest;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.repository.UserRepository;
import mk.ukim.finki.ib.filesharing.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User authenticate(LoginRequest request) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        return this.userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s is not found!", request.getUsername())));
    }

    @Override
    public User registerUser(RegisterRequest request) {
        return this.userRepository.save(new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()), validateUser()));
    }

    private boolean validateUser() {
        //TODO
        return true;
    }
}
