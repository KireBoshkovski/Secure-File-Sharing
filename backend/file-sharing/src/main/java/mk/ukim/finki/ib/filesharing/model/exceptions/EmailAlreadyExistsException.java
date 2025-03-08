package mk.ukim.finki.ib.filesharing.model.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email is already taken");
    }
}