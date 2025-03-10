package mk.ukim.finki.ib.filesharing.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.DTO.SharedFileDto;
import mk.ukim.finki.ib.filesharing.encryption.AES;
import mk.ukim.finki.ib.filesharing.encryption.EncryptionUtils;
import mk.ukim.finki.ib.filesharing.model.FileAccess;
import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.model.exceptions.UnauthorizedAccessException;
import mk.ukim.finki.ib.filesharing.service.FileAccessService;
import mk.ukim.finki.ib.filesharing.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;
    private final FileAccessService fileAccessService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @AuthenticationPrincipal User user) {
        try {
            SecretKey secretKey = EncryptionUtils.getSecretKey();
            byte[] iv = AES.generateIV();
            byte[] encryptedData = AES.encryptAES(file.getBytes(), secretKey, iv);

            UploadedFile uploadedFile = new UploadedFile(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    encryptedData,
                    user,
                    iv
            );

            this.fileService.save(uploadedFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
        }
        return ResponseEntity.ok("File uploaded successfully.");
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable Long id, @RequestParam FileAccess.AccessType accessRequest, @AuthenticationPrincipal User user) {
        if (!fileService.getFileById(id).getOwner().equals(user)) {
            if ((accessRequest.equals(FileAccess.AccessType.DOWNLOAD) && !fileService.canDownload(id, user))
                    || (!accessRequest.equals(FileAccess.AccessType.DOWNLOAD) && !fileService.hasAccess(id, user, accessRequest))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ByteArrayResource(new byte[0]));
            }
        }
        SecretKey secretKey = EncryptionUtils.getSecretKey();
        return fileService.findById(id)
                .map(file -> {
                    try {
                        byte[] decryptedData = AES.decryptAES(file.getData(), secretKey, file.getIv());
                        ByteArrayResource resource = new ByteArrayResource(decryptedData);

                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(file.getFileType()))
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                                .body(resource);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ByteArrayResource(new byte[0]));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ByteArrayResource(new byte[0])));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (this.fileService.existsById(id)) {
            this.fileService.removeFile(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/access")
    public ResponseEntity<List<SharedFileDto>> showAllAccessibleFilesByUsername(@AuthenticationPrincipal User user) {
        List<SharedFileDto> response = this.fileService.findByAccess(user)
                .stream()
                .map(file -> new SharedFileDto(
                        file.getId(),
                        file.getFileName(),
                        file.getFileType(),
                        file.getOwner().getUsername(),
                        file.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        this.fileAccessService.getAccessTypesForFileAndUser(file.getId(), user.getUsername())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/created")
    public ResponseEntity<List<SharedFileDto>> showCreatedByUser(@AuthenticationPrincipal User user) {
        List<SharedFileDto> response = this.fileService.findByOwner(user)
                .stream()
                .map(file -> new SharedFileDto(
                        file.getId(), file.getFileName(), file.getFileType(),
                        file.getOwner().getUsername(),
                        file.getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        this.fileAccessService.getAccessTypesForFileAndUser(file.getId(), user.getUsername())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/share/{id}")
    public ResponseEntity<String> shareFile(@PathVariable Long id,
                                            @RequestParam String username,
                                            @RequestParam String accessType,
                                            @AuthenticationPrincipal User user) {
        FileAccess.AccessType type;
        try {
            type = FileAccess.AccessType.valueOf(accessType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid access type. Use VIEW, DOWNLOAD OR EDIT.");
        }

        try {
            this.fileService.shareFile(id, username, user, type);
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        return ResponseEntity.ok("File shared successfully with " + username + " with " + type + " access.");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editFile(@PathVariable Long id,
                                           @RequestParam String content,
                                           @AuthenticationPrincipal User user) {
        try {
            fileService.editFile(id, content.getBytes(), user);
            return ResponseEntity.ok("File updated successfully.");
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to edit this file.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File update failed.");
        }
    }
}
