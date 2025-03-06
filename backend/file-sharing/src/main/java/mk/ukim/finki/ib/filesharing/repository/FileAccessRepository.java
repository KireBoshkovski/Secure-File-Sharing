package mk.ukim.finki.ib.filesharing.repository;

import mk.ukim.finki.ib.filesharing.model.FileAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileAccessRepository extends JpaRepository <FileAccess, Long>{
    List<FileAccess> findByUploadedFile_IdAndUser_Username(Long fileId, String userId);
}
