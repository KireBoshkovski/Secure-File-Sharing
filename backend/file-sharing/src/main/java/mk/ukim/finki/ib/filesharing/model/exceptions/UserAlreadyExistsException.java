package mk.ukim.finki.ib.filesharing.model.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(String.format(message));
    }
}
