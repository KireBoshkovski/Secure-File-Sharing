package mk.ukim.finki.ib.filesharing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private LocalDateTime lastAccessed;

    public enum AccessType {
        READ,
        WRITE,
        BOTH
    }
}
