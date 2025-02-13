package mk.ukim.finki.ib.filesharing.repository;

import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    List<UploadedFile> findAllByOwnerUsername(String username);

    @Query("select u.hasAccess from User u where u.username = :username")
    List<UploadedFile> findAllByAccessGranted(String username);
}
