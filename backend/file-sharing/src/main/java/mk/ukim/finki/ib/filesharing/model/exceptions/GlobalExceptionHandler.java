package mk.ukim.finki.ib.filesharing.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<List<String>> handleUsernameExists(UsernameAlreadyExistsException ex) {
        List<String> errorResponse = new ArrayList<>();
        errorResponse.add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<List<String>> handleEmailExists(EmailAlreadyExistsException ex) {
        List<String> errorResponse = new ArrayList<>();
        errorResponse.add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<List<String>> handleAuthentication(AuthenticationException ex) {
        List<String> errorResponse = new ArrayList<>();
        errorResponse.add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<List<String>> handleWeakPassword(WeakPasswordException ex) {
        List<String> errorResponse = new ArrayList<>();
        errorResponse.add(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
