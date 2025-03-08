package mk.ukim.finki.ib.filesharing.repository;

import mk.ukim.finki.ib.filesharing.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
}
