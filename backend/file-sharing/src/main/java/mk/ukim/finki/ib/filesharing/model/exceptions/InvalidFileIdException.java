package mk.ukim.finki.ib.filesharing.model.exceptions;

public class InvalidFileIdException extends RuntimeException{
    public InvalidFileIdException(Long msg) {
        super(String.format("File with id: %s is not found: ", msg));
    }
}
