package mk.ukim.finki.ib.filesharing.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.model.FileAccess;
import mk.ukim.finki.ib.filesharing.repository.FileAccessRepository;
import mk.ukim.finki.ib.filesharing.repository.FileRepository;
import mk.ukim.finki.ib.filesharing.service.FileAccessService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileAccessServiceImpl implements FileAccessService {
    private final FileAccessRepository fileAccessRepository;

    public List<FileAccess.AccessType> getAccessTypesForFileAndUser(Long fileId, String userId) {
        // Fetch FileAccess records for the specific file and user
        List<FileAccess> fileAccessList = fileAccessRepository.findByUploadedFile_IdAndUser_Username(fileId, userId);

        // Map them to the AccessType enum values and return as a list
        return fileAccessList.stream()
                .map(FileAccess::getAccessType)  // Extract the AccessType for each FileAccess record
                .distinct() // Ensure no duplicate access types are returned
                .collect(Collectors.toList());
    }

}
