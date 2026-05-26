package tb25.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tb25.application.model.Novel;
import java.util.List;
import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {
    List<Novel> findByUserId(Long userId);
    Optional<Novel> findByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}
