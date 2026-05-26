package tb25.application.service;

import org.springframework.stereotype.Service;
import tb25.application.model.Novel;
import tb25.application.repository.NovelRepository;
import java.util.List;
import java.util.Optional;

@Service
public class NovelService {

    private final NovelRepository repository;

    public NovelService(NovelRepository repository) {
        this.repository = repository;
    }

    public List<Novel> getAll(Long userId) {
        return repository.findByUserId(userId);
    }

    public Novel save(Novel novel) {
        return repository.save(novel);
    }

    public Optional<Novel> update(Long id, Long userId, Novel updated) {
        return repository.findByIdAndUserId(id, userId).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setAuthor(updated.getAuthor());
            existing.setProgressType(updated.getProgressType());
            existing.setProgress(updated.getProgress());
            existing.setCoverUrl(updated.getCoverUrl());
            existing.setTags(updated.getTags());
            existing.setRating(updated.getRating());
            existing.setStatus(updated.getStatus());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id, Long userId) {
        if (!repository.existsByIdAndUserId(id, userId)) return false;
        repository.deleteById(id);
        return true;
    }
}
