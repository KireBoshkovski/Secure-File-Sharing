package mk.ukim.finki.ib.filesharing.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import mk.ukim.finki.ib.filesharing.model.FileAccess;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SharedFileDto {
    private Long id;
    private String name;
    private String type;
    private String owner;
    private List<FileAccess.AccessType> accessTypes;
}
