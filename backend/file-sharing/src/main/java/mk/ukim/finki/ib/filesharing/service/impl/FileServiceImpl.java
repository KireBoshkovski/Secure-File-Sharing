package mk.ukim.finki.ib.filesharing.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.model.exceptions.InvalidFileIdException;
import mk.ukim.finki.ib.filesharing.repository.FileRepository;
import mk.ukim.finki.ib.filesharing.service.FileService;
import mk.ukim.finki.ib.filesharing.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final UserService userService;
    @Override
    @Transactional
    public void save(String fileName, String fileType, byte[] data, User user) {
        UploadedFile file = new UploadedFile(fileName, fileType, data, user);

        file.getUsers().add(user);

        this.fileRepository.save(file);
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
        return this.fileRepository.findAllByUsersContaining(user);
    }

    @Override
    public void shareFile(Long id, String username, User user) {
        UploadedFile f = this.fileRepository.findById(id).orElseThrow(() -> new InvalidFileIdException(id));
        if (!user.getUsername().equals(f.getOwner().getUsername())) {
            System.err.println("You don't own this file [head scratch]");
        }
        f.getUsers().add(this.userService.findByUsername(username));
        this.fileRepository.save(f);
    }
}
