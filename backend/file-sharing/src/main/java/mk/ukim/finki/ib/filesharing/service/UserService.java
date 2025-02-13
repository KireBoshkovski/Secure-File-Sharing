package mk.ukim.finki.ib.filesharing.service;

import mk.ukim.finki.ib.filesharing.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    void registerUser(String username, String email, String password) throws Exception;

    User findByUsername(String username);
    void save(User user);
}
