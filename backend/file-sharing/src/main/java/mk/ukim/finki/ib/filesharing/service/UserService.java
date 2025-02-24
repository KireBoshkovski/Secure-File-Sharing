package mk.ukim.finki.ib.filesharing.service;

import mk.ukim.finki.ib.filesharing.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    User findByUsername(String username);
}
