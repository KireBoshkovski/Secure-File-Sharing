package mk.ukim.finki.ib.filesharing.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.model.exceptions.UserNotFoundException;
import mk.ukim.finki.ib.filesharing.repository.UploadedFileRepository;
import mk.ukim.finki.ib.filesharing.repository.UserRepository;
import mk.ukim.finki.ib.filesharing.service.UploadedFileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UploadedFileServiceImpl implements UploadedFileService {
    private final UploadedFileRepository fileRepository;
    private final UserRepository userRepository;

    @Override
    public void save(String fileName, String fileType, byte[] data, String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("username: " + username));
        UploadedFile file = new UploadedFile(fileName, fileType, data, user);
        this.fileRepository.save(file);

        user.getHasAccess().add(file);
        this.userRepository.save(user);
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
    public void deleteById(Long id) {
        this.fileRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<UploadedFile> findByOwner(String username) {
        return this.fileRepository.findAllByOwnerUsername(username);
    }

    @Override
    @Transactional
    public List<UploadedFile> findByAccess(String username) {
        return this.fileRepository.findAllByAccessGranted(username);
    }
}
