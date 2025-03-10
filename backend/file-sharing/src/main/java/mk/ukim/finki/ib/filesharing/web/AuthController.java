package mk.ukim.finki.ib.filesharing.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.filesharing.DTO.LoginRequest;
import mk.ukim.finki.ib.filesharing.DTO.LoginResponse;
import mk.ukim.finki.ib.filesharing.DTO.RegisterRequest;
import mk.ukim.finki.ib.filesharing.model.User;
import mk.ukim.finki.ib.filesharing.service.AuthService;
import mk.ukim.finki.ib.filesharing.service.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JWTService jwtService;

    @PostMapping("/verify")
    public ResponseEntity<User> verify(@RequestBody RegisterRequest request) {
        Optional<User> registeredUser = this.authService.sendConfirmationEmail(request);
        return registeredUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestParam String token) {
        Optional<User> registeredUser = this.authService.registerUser(token);
        return registeredUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequest request) {
        this.authService.authenticate(request);
        this.authService.sendOTP(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponse> verifyOTP(@RequestParam String otp, @RequestParam String username) {
        User user = this.authService.findUserByToken(otp);

        if (!user.getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String jwtToken = jwtService.generateToken(user);

        LoginResponse loginResponse = new LoginResponse(user.getUsername(), jwtToken, this.jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }
}