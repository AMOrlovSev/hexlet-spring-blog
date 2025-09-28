package io.hexlet.spring.controler;

import io.hexlet.spring.model.Post;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    private List<Post> posts = new ArrayList<Post>();

    public PostController() {
        initializePosts();
    }

    private void initializePosts() {
        Post post1 = new Post();
        post1.setTitle("Мой первый пост в блоге");
        post1.setContent("Сегодня я начал вести свой блог. Это очень волнительно! Буду делиться своими мыслями и идеями.");
        post1.setAuthor("Алексей");
        post1.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 30));

        Post post2 = new Post();
        post2.setTitle("Изучение Java");
        post2.setContent("Продолжаю изучать Java. Сегодня разбирался с коллекциями и потоками. Очень мощные инструменты!");
        post2.setAuthor("Мария");
        post2.setCreatedAt(LocalDateTime.of(2024, 1, 16, 14, 15));

        Post post3 = new Post();
        post3.setTitle("Выходные планы");
        post3.setContent("На выходных планирую поход в горы. Надеюсь, погода не подведет и будет возможность сделать красивые фотографии.");
        post3.setAuthor("Дмитрий");
        post3.setCreatedAt(LocalDateTime.of(2024, 1, 17, 9, 0));

        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
    }

    @GetMapping("/posts") // Список страниц
    public List<Post> index(@RequestParam(defaultValue = "10") Integer limit) {
        return posts.stream().limit(limit).toList();
    }

    @PostMapping("/posts") // Создание страницы
    public Post create(@RequestBody Post post) {
        posts.add(post);
        return post;
    }

    @GetMapping("/posts/{id}") // Вывод страницы
    public Optional<Post> show(@PathVariable String id) {
        var post = posts.stream()
                .filter(p -> p.getTitle().equals(id))
                .findFirst();
        return post;
    }

    @PutMapping("/posts/{id}") // Обновление страницы
    public Post update(@PathVariable String id, @RequestBody Post data) {
        var maybePost = posts.stream()
                .filter(p -> p.getTitle().equals(id))
                .findFirst();
        if (maybePost.isPresent()) {
            var post = maybePost.get();
            post.setTitle(data.getTitle());
            post.setContent(data.getContent());
            post.setAuthor(data.getAuthor());
            post.setCreatedAt(data.getCreatedAt());
        }
        return data;
    }

    @DeleteMapping("/posts/{id}") // Удаление страницы
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getTitle().equals(id));
    }
}
