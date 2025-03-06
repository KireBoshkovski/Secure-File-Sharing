package mk.ukim.finki.ib.filesharing.repository;

import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import mk.ukim.finki.ib.filesharing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileRepository extends JpaRepository<UploadedFile, Long> {
    List<UploadedFile> findAllByOwner(User owner);

    List<UploadedFile> findAllByAccessList_User(User user);
    @Query("SELECT f.uploadedFile FROM FileAccess f WHERE f.user = :user")
    List<UploadedFile> findAllSharedWithUser(@Param("user") User user);
}

