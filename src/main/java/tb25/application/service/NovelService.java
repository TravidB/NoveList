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

    public List<Novel> getAll() {
        return repository.findAll();
    }

    public Novel save(Novel novel) {
        return repository.save(novel);
    }

    public Optional<Novel> update(Long id, Novel updated) {
        return repository.findById(id).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setAuthor(updated.getAuthor());
            existing.setProgressType(updated.getProgressType());
            existing.setProgress(updated.getProgress());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}
