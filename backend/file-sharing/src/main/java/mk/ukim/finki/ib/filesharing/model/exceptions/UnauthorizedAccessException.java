package mk.ukim.finki.ib.filesharing.model.exceptions;

public class UnauthorizedAccessException extends Throwable {
    public UnauthorizedAccessException() {
        super("You don't have permission to access this resource.");
    }
}
