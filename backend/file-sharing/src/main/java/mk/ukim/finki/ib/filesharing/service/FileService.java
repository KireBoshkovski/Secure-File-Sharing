package mk.ukim.finki.ib.filesharing.service;

import mk.ukim.finki.ib.filesharing.model.FileAccess;
import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.model.exceptions.UnauthorizedAccessException;

import java.util.List;
import java.util.Optional;

public interface FileService {
    void save(UploadedFile uploadedFile);

    Optional<UploadedFile> findById(Long id);

    boolean existsById(Long id);

    void removeFile(Long id);

    List<UploadedFile> findByOwner(User user);
    List<UploadedFile> findByAccess(User user);

    void shareFile(Long id, String username, User user, FileAccess.AccessType accessType) throws UnauthorizedAccessException;
}
