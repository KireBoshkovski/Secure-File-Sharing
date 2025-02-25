package mk.ukim.finki.ib.filesharing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Lob
    private byte[] data;

    @Lob
    private byte[] iv;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "uploadedFile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileAccess> accessList = new ArrayList<>();

    private LocalDateTime lastModified;

    public UploadedFile(String fileName, String fileType, byte[] data, User owner, byte[] iv) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.owner = owner;
        this.iv = iv;
        this.lastModified = LocalDateTime.now();
    }
}
