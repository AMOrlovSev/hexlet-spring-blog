package io.hexlet.spring.controler;

import io.hexlet.spring.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private List<Post> posts = new ArrayList<Post>();

    public PostController() {
        initializePosts();
    }

    private void initializePosts() {
        Post post1 = new Post();
        post1.setId("1");
        post1.setTitle("Мой первый пост в блоге");
        post1.setContent("Сегодня я начал вести свой блог. Это очень волнительно! Буду делиться своими мыслями и идеями.");
        post1.setAuthor("Алексей");
        post1.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 30));

        Post post2 = new Post();
        post2.setId("2");
        post2.setTitle("Изучение Java");
        post2.setContent("Продолжаю изучать Java. Сегодня разбирался с коллекциями и потоками. Очень мощные инструменты!");
        post2.setAuthor("Мария");
        post2.setCreatedAt(LocalDateTime.of(2024, 1, 16, 14, 15));

        Post post3 = new Post();
        post3.setId("3");
        post3.setTitle("Выходные планы");
        post3.setContent("На выходных планирую поход в горы. Надеюсь, погода не подведет и будет возможность сделать красивые фотографии.");
        post3.setAuthor("Дмитрий");
        post3.setCreatedAt(LocalDateTime.of(2024, 1, 17, 9, 0));

        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
    }

    @GetMapping
    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10") Integer limit) {
        var result = posts.stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(result);
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        posts.add(post);
        URI location = URI.create("/posts/" + post.getId());
        return ResponseEntity.created(location).body(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable String id) {
        Optional<Post> post = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post data) {
        var maybePost = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        var status = HttpStatus.NOT_FOUND;
        if (maybePost.isPresent()) {
            Post post = maybePost.get();
            post.setTitle(data.getTitle());
            post.setContent(data.getContent());
            post.setAuthor(data.getAuthor());
            post.setCreatedAt(data.getCreatedAt());
            status = HttpStatus.OK;
        }
        return ResponseEntity.status(status).body(data);
    }

    @DeleteMapping("/{id}") // Удаление страницы
    public ResponseEntity<Void> destroy(@PathVariable String id) {
        boolean removed = posts.removeIf(p -> p.getId().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build();  // 204 No Content
        } else {
            return ResponseEntity.notFound().build();   // 404 Not Found
        }
    }
}
