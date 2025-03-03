package mk.ukim.finki.ib.filesharing.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "file_access")
public class FileAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UploadedFile uploadedFile;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private AccessType accessType;

    private LocalDateTime downloadExpiration;

    private LocalDateTime lastAccessed;

    public FileAccess(UploadedFile file, User user, AccessType accessType, LocalDateTime expiration) {
        this.uploadedFile = file;
        this.user = user;
        this.accessType = accessType;
        this.downloadExpiration = expiration;
        this.lastAccessed = LocalDateTime.now();
    }

    public enum AccessType {
        VIEW,
        DOWNLOAD,
        EDIT
    }
}
