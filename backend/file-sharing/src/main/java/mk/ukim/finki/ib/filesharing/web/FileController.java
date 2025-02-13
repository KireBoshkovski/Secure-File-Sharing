package mk.ukim.finki.ib.filesharing.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.model.UploadedFile;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.service.UploadedFileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
public class FileController {
    private final UploadedFileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @AuthenticationPrincipal User user) {
        try {
            System.out.println("FILE CONTENT: " + Arrays.toString(file.getBytes()));
            this.fileService.save(file.getOriginalFilename(), file.getContentType(), file.getBytes(), user.getUsername());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id) {
        try {
            return this.fileService
                    .findById(id)
                    .map(file -> ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getFileType()))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                            .body(new ByteArrayResource(file.getData())))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (this.fileService.existsById(id)) {
            this.fileService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/access")
    public ResponseEntity<List<UploadedFile>> showAllAccessibleFilesByUsername(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(this.fileService.findByAccess(user.getUsername()));
    }

    @GetMapping("/created")
    public ResponseEntity<List<UploadedFile>> showCreatedByUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(this.fileService.findByOwner(user.getUsername()));
    }
}
