package mk.ukim.finki.ib.filesharing.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.model.FileAccess;
import mk.ukim.finki.ib.filesharing.repository.FileAccessRepository;
import mk.ukim.finki.ib.filesharing.service.FileAccessService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileAccessServiceImpl implements FileAccessService {
    private final FileAccessRepository fileAccessRepository;

    public List<FileAccess.AccessType> getAccessTypesForFileAndUser(Long fileId, String userId) {
        List<FileAccess> fileAccessList = fileAccessRepository.findByUploadedFile_IdAndUser_Username(fileId, userId);

        return fileAccessList.stream()
                .map(FileAccess::getAccessType)
                .distinct()
                .collect(Collectors.toList());
    }

}
