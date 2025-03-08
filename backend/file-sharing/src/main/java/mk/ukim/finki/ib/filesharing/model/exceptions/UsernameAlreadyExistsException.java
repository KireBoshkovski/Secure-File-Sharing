package mk.ukim.finki.ib.filesharing.model.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("Username is already taken");
    }
}