package tb25.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tb25.application.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
