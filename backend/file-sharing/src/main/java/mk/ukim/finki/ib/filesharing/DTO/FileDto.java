package mk.ukim.finki.ib.filesharing.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDto {
    private Long id;
    private String name;
    private String type;
    private String owner;
    private String lastAccess;
}
