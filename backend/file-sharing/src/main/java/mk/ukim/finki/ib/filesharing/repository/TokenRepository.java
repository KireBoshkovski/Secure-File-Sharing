package mk.ukim.finki.ib.filesharing.repository;

import mk.ukim.finki.ib.filesharing.model.Token;
import mk.ukim.finki.ib.filesharing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    void deleteByUser(User user);
    void deleteByExpiresAtBefore(LocalDateTime date);
}
