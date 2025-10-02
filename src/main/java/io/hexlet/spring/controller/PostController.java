package io.hexlet.spring.controller;

import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @PostConstruct
    public void init() {
        // Проверяем, есть ли уже данные в базе
        if (postRepository.count() == 0) {
            initializePosts();
        }
    }

    private void initializePosts() {
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Мой первый пост в блоге");
        post1.setContent("Сегодня я начал вести свой блог. Это очень волнительно! Буду делиться своими мыслями и идеями.");
        post1.setPublished("Алексей");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Изучение Java");
        post2.setContent("Продолжаю изучать Java. Сегодня разбирался с коллекциями и потоками. Очень мощные инструменты!");
        post2.setPublished("Мария");

        Post post3 = new Post();
        post3.setId(3L);
        post3.setTitle("Выходные планы");
        post3.setContent("На выходных планирую поход в горы. Надеюсь, погода не подведет и будет возможность сделать красивые фотографии.");
        post3.setPublished("Дмитрий");

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }

    @GetMapping
    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10") Integer limit) {
        List<Post> result = postRepository.findAll().stream().limit(limit).collect(Collectors.toList());

        long totalCount = postRepository.count();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(totalCount))
                .body(result);
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        postRepository.save(post);
        URI location = URI.create("/posts/" + post.getId());
        return ResponseEntity.created(location).body(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id) {
        Optional<Post> post = postRepository.findById(id);

        return post.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 200 OK или 404 Not Found
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post data) {
        var maybePost = postRepository.findById(id);
        var status = HttpStatus.NOT_FOUND;
        if (maybePost.isPresent()) {
            Post post = maybePost.get();
            post.setTitle(data.getTitle());
            post.setContent(data.getContent());
            post.setPublished(data.getPublished());
            status = HttpStatus.OK;
            postRepository.save(post);
            return ResponseEntity.status(status).body(post);
        }
        return ResponseEntity.status(status).body(data);
    }

    @DeleteMapping("/{id}") // Удаление страницы
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        if (!postRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404
        }

        postRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
