package mk.ukim.finki.ib.filesharing.model.exceptions;

public class WeakPasswordException extends Throwable {
    public WeakPasswordException() {
        super(String.format("Password must be at least 8 characters long, contain one uppercase letter, one lowercase letter, one number, and one special character."));
    }
}
