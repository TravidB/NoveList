package tb25.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tb25.application.model.Novel;

public interface NovelRepository extends JpaRepository<Novel, Long> {}
