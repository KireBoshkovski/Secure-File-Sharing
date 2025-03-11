package mk.ukim.finki.ib.filesharing.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.encryption.AES;
import mk.ukim.finki.ib.filesharing.encryption.EncryptionUtils;
import mk.ukim.finki.ib.filesharing.model.FileAccess;
import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.model.exceptions.InvalidFileIdException;
import mk.ukim.finki.ib.filesharing.model.exceptions.UnauthorizedAccessException;
import mk.ukim.finki.ib.filesharing.model.exceptions.UserNotFoundException;
import mk.ukim.finki.ib.filesharing.repository.FileRepository;
import mk.ukim.finki.ib.filesharing.service.FileService;
import mk.ukim.finki.ib.filesharing.service.UserService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void save(UploadedFile uploadedFile) {
        this.fileRepository.save(uploadedFile);
    }

    @Override
    public Optional<UploadedFile> findById(Long id) {
        return this.fileRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return this.fileRepository.existsById(id);
    }

    @Override
    public void removeFile(Long id) {
        this.fileRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<UploadedFile> findByOwner(User user) {
        return this.fileRepository.findAllByOwner(user);
    }

    @Override
    @Transactional
    public List<UploadedFile> findByAccess(User user) {
        return fileRepository.findAllByAccessList_User(user);
    }

    public boolean hasAccess(Long fileId, User user, FileAccess.AccessType accessType) {
        return fileRepository.findById(fileId)
                .map(file -> file.getAccessList().stream()
                        .anyMatch(access -> access.getUser().equals(user) && access.getAccessType() == accessType))
                .orElse(false);
    }

    public boolean canDownload(Long fileId, User user) {
        return fileRepository.findById(fileId)
                .map(file -> file
                        .getOwner().equals(user) || file.getAccessList()
                        .stream()
                        .anyMatch(access -> access.getUser().equals(user) &&
                                access.getAccessType() == FileAccess.AccessType.DOWNLOAD &&
                                (access.getDownloadExpiration() == null || LocalDateTime.now().isBefore(access.getDownloadExpiration()))
                        )
                )
                .orElse(false);
    }

    public boolean canEdit(Long fileId, User user) {
        return hasAccess(fileId, user, FileAccess.AccessType.EDIT);
    }

    @Override
    @Transactional
    public void shareFile(Long id, String username, User owner, FileAccess.AccessType accessType) throws UnauthorizedAccessException {
        UploadedFile file = fileRepository.findById(id)
                .orElseThrow(() -> new InvalidFileIdException(id));

        if (!owner.equals(file.getOwner())) {
            throw new UnauthorizedAccessException();
        }

        User targetUser = userService.findByUsername(username);
        if (targetUser == null) {
            throw new UserNotFoundException("User " + username + " not found.");
        }

        boolean hasAccess = file.getAccessList().stream()
                .anyMatch(access -> access.getUser().equals(targetUser) && access.getAccessType() == accessType);

        if (hasAccess) {
            System.out.println("User " + username + " already has " + accessType + " access.");
            return;
        }

        LocalDateTime expiration = (accessType == FileAccess.AccessType.DOWNLOAD) ? LocalDateTime.now().plusHours(24) : null;
        FileAccess fileAccess = new FileAccess(file, targetUser, accessType, expiration);
        file.getAccessList().add(fileAccess);
        fileRepository.save(file);

        System.out.println("File shared with " + username + " with " + accessType + " access.");
    }

    @Override
    @Transactional
    public void editFile(Long id, byte[] newData, User user) throws UnauthorizedAccessException {
        UploadedFile file = this.fileRepository.findById(id)
                .orElseThrow(() -> new InvalidFileIdException(id));

        if (!file.getOwner().equals(user)) {
            boolean canEdit = file.getAccessList().stream()
                    .anyMatch(access -> access.getUser().equals(user) &&
                            (access.getAccessType() == FileAccess.AccessType.EDIT));

            if (!canEdit) {
                throw new UnauthorizedAccessException();
            }
        }

        try {
            SecretKey secretKey = EncryptionUtils.getSecretKey();
            byte[] iv = AES.generateIV();
            byte[] encryptedData = AES.encryptAES(newData, secretKey, iv);

            file.setLastModified(LocalDateTime.now());
            file.setData(encryptedData);
            file.setIv(iv);
            this.fileRepository.save(file);
        } catch (Exception e) {
            throw new RuntimeException("File update failed.");
        }
    }

    public UploadedFile getFileById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    public void saveFileAsPdf(Long fileId, byte[] pdfData, User user) throws UnauthorizedAccessException {
        UploadedFile uploadedFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!uploadedFile.getOwner().equals(user)) {
            throw new UnauthorizedAccessException();
        }

        uploadedFile.setData(pdfData);
        uploadedFile.setFileType("application/pdf");
        uploadedFile.setLastModified(LocalDateTime.now());

        fileRepository.save(uploadedFile);
    }
}
