package tb25.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tb25.application.model.Novel;
import tb25.application.service.NovelService;

import java.util.List;

@RestController
@RequestMapping("/api/novels")
public class NovelController {

    private final NovelService service;

    public NovelController(NovelService service) {
        this.service = service;
    }

    @GetMapping
    public List<Novel> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<Novel> create(@RequestBody Novel novel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(novel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Novel> update(@PathVariable Long id, @RequestBody Novel novel) {
        return service.update(id, novel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
