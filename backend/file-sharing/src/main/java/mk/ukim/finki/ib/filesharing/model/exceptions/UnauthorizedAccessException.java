package mk.ukim.finki.ib.filesharing.model.exceptions;

public class UnauthorizedAccessException extends Throwable {
    public UnauthorizedAccessException() {
        super(String.format("You don't have permission to access this resource."));
    }
}
