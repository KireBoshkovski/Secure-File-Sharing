package mk.ukim.finki.ib.filesharing.service;

import mk.ukim.finki.ib.filesharing.model.UploadedFile;

import java.util.List;
import java.util.Optional;

public interface UploadedFileService {
    void save(String fileName, String fileType, byte[] data, String username) throws Exception;

    Optional<UploadedFile> findById(Long id) throws Exception;

    boolean existsById(Long id);

    void deleteById(Long id);

    List<UploadedFile> findByOwner(String username);

    List<UploadedFile> findByAccess(String username);
}
