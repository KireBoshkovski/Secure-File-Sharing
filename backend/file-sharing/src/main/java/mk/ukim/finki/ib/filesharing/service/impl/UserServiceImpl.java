package mk.ukim.finki.ib.filesharing.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.model.exceptions.UserNotFoundException;
import mk.ukim.finki.ib.filesharing.repository.UserRepository;
import mk.ukim.finki.ib.filesharing.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken!");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use!");
        }

        String hashedPassword = passwordEncoder.encode(password);

        userRepository.save(new User(username, email, hashedPassword, new ArrayList<>(), true));
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("username: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.userRepository.findById(username).orElseThrow(() -> new UserNotFoundException("username: " + username));
    }
}
