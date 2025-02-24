package mk.ukim.finki.ib.filesharing.service;

import mk.ukim.finki.ib.filesharing.DTO.LoginRequest;
import mk.ukim.finki.ib.filesharing.DTO.RegisterRequest;
import mk.ukim.finki.ib.filesharing.model.User;

public interface AuthService {
    User registerUser(RegisterRequest request);

    User authenticate(LoginRequest request);
}
