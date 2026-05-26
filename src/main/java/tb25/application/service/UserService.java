package tb25.application.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tb25.application.model.User;
import tb25.application.repository.UserRepository;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> register(String username, String password) {
        if (repository.findByUsername(username).isPresent()) return Optional.empty();
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        return Optional.of(repository.save(user));
    }

    public Optional<User> login(String username, String password) {
        return repository.findByUsername(username)
                .filter(u -> encoder.matches(password, u.getPassword()));
    }
}
