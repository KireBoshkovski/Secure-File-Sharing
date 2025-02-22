package mk.ukim.finki.ib.filesharing.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
        FileAccess access = new FileAccess();
        access.setUploadedFile(uploadedFile);
        access.setUser(uploadedFile.getOwner());
        access.setAccessType(FileAccess.AccessType.BOTH);
        access.setLastAccessed(LocalDateTime.now());

        uploadedFile.getAccessList().add(access);
        fileRepository.save(uploadedFile);
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
        return this.fileRepository.findAllByAccessList_User(user);
    }

    @Override
    @Transactional
    public void shareFile(Long id, String username, User user, FileAccess.AccessType accessType) throws UnauthorizedAccessException {
        UploadedFile file = this.fileRepository.findById(id)
                .orElseThrow(() -> new InvalidFileIdException(id));

        if (!user.getUsername().equals(file.getOwner().getUsername())) {
            throw new UnauthorizedAccessException();
        }

        User targetUser = this.userService.findByUsername(username);
        if (targetUser == null) {
            throw new UserNotFoundException("User " + username + " not found.");
        }

        boolean hasAccess = file.getAccessList().stream()
                .anyMatch(access -> access.getUser().getUsername().equals(username));

        if (hasAccess) {
            System.out.println("User " + username + " already has access to this file.");
            return;
        }

        FileAccess fileAccess = new FileAccess();
        fileAccess.setUploadedFile(file);
        fileAccess.setUser(targetUser);
        fileAccess.setAccessType(accessType);
        fileAccess.setLastAccessed(LocalDateTime.now());

        file.getAccessList().add(fileAccess);
        this.fileRepository.save(file);

        System.out.println("File shared with " + username + " with " + accessType + " access.");
    }
}
