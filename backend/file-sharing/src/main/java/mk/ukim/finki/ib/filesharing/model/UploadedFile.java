package mk.ukim.finki.ib.filesharing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Lob // Large object for storing binary data
    private byte[] data;
    @ManyToOne
    private User owner;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "file_user",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    public UploadedFile(String fileName, String fileType, byte[] data, User owner) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.owner = owner;
        this.users = new ArrayList<>();
    }
}
