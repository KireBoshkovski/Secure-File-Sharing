package mk.ukim.finki.ib.filesharing.service;

import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import mk.ukim.finki.ib.filesharing.model.User;

import java.util.List;
import java.util.Optional;

public interface FileService {
    void save(String fileName, String fileType, byte[] data, User user);

    Optional<UploadedFile> findById(Long id);

    boolean existsById(Long id);

    void removeFile(Long id);

    List<UploadedFile> findByOwner(User user);
    List<UploadedFile> findByAccess(User user);

    void shareFile(Long id, String username, User user);
}
