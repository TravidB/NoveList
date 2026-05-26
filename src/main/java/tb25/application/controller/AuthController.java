package tb25.application.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tb25.application.model.User;
import tb25.application.service.UserService;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.getOrDefault("username", "").trim();
        String password = body.getOrDefault("password", "");
        if (username.isEmpty() || password.isEmpty()) return ResponseEntity.badRequest().build();
        Optional<User> result = userService.register(username, password);
        if (result.isEmpty()) return ResponseEntity.status(409).build();
        User u = result.get();
        session.setAttribute("userId", u.getId());
        session.setAttribute("username", u.getUsername());
        return ResponseEntity.ok(Map.of("username", u.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.getOrDefault("username", "").trim();
        String password = body.getOrDefault("password", "");
        Optional<User> result = userService.login(username, password);
        if (result.isEmpty()) return ResponseEntity.status(401).build();
        User u = result.get();
        session.setAttribute("userId", u.getId());
        session.setAttribute("username", u.getUsername());
        return ResponseEntity.ok(Map.of("username", u.getUsername()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(Map.of("username", username));
    }
}
