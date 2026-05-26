package tb25.application.controller;

import jakarta.servlet.http.HttpSession;
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

    private Long userId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    @GetMapping
    public ResponseEntity<List<Novel>> getAll(HttpSession session) {
        Long uid = userId(session);
        if (uid == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(service.getAll(uid));
    }

    @PostMapping
    public ResponseEntity<Novel> create(@RequestBody Novel novel, HttpSession session) {
        Long uid = userId(session);
        if (uid == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        novel.setUserId(uid);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(novel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Novel> update(@PathVariable Long id, @RequestBody Novel novel, HttpSession session) {
        Long uid = userId(session);
        if (uid == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return service.update(id, uid, novel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session) {
        Long uid = userId(session);
        if (uid == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return service.delete(id, uid)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
