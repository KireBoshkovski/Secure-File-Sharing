package mk.ukim.finki.ib.filesharing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String fileType;
    @Lob // Large object for storing binary data
    private byte[] data;
    @ManyToOne
    private User owner;

    public UploadedFile(String fileName, String fileType, byte[] data, User owner) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.owner = owner;
    }
}
