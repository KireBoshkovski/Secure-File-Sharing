package mk.ukim.finki.ib.filesharing.service;

import mk.ukim.finki.ib.filesharing.model.FileAccess;

import java.util.List;

public interface FileAccessService {
    List<FileAccess.AccessType> getAccessTypesForFileAndUser(Long fileId, String userId);
}
