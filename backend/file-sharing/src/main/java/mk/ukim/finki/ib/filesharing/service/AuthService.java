package mk.ukim.finki.ib.filesharing.service;

import mk.ukim.finki.ib.filesharing.DTO.LoginRequest;
import mk.ukim.finki.ib.filesharing.DTO.RegisterRequest;
import mk.ukim.finki.ib.filesharing.model.User;

import java.util.Optional;

public interface AuthService {
    Optional<User> registerUser(String token);

    Optional<User> sendConfirmationEmail(RegisterRequest request);

    void authenticate(LoginRequest request);

    void sendOTP(LoginRequest request);

    User findUserByToken(String token);
}
