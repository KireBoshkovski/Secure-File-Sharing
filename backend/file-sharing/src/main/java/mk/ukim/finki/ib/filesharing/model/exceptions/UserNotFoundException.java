package mk.ukim.finki.ib.filesharing.model.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String msg) {
        super(String.format("User with %s is not found: ", msg));
    }
}
