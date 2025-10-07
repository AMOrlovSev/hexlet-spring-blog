package io.hexlet.spring.controller;

import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.CommentRepository;
import io.hexlet.spring.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private CommentRepository commentRepository;

    @PostConstruct
    public void init() {
        // Проверяем, есть ли уже данные в базе
        if (postRepository.count() == 0) {
            //initializePosts();
        }
    }

    private void initializePosts() {
        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Мой первый пост в блоге");
        post1.setContent("Сегодня я начал вести свой блог. Это очень волнительно! Буду делиться своими мыслями и идеями.");
        post1.setPublished(false);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Изучение Java");
        post2.setContent("Продолжаю изучать Java. Сегодня разбирался с коллекциями и потоками. Очень мощные инструменты!");
        post2.setPublished(true);

        Post post3 = new Post();
        post3.setId(3L);
        post3.setTitle("Выходные планы");
        post3.setContent("На выходных планирую поход в горы. Надеюсь, погода не подведет и будет возможность сделать красивые фотографии.");
        post3.setPublished(true);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }

//    @GetMapping
//    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10") Integer limit) {
//        List<Post> result = postRepository.findAll().stream().limit(limit).collect(Collectors.toList());
//
//        long totalCount = postRepository.count();
//
//        return ResponseEntity.ok()
//                .header("X-Total-Count", String.valueOf(totalCount))
//                .body(result);
//    }

//    @GetMapping("")
//    public Page<Post> getPublishedPosts(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
//        return postRepository.findByPublishedTrue(pageable);
//    }
//
//    @PostMapping
//    public ResponseEntity<Post> create(@Valid @RequestBody Post post) {
//        Post saved = postRepository.save(post);
//        URI location = URI.create("/api/posts/" + saved.getId());
//        return ResponseEntity.created(location).body(saved);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Post> show(@PathVariable Long id) {
//        Optional<Post> post = postRepository.findById(id);
//
//        return post.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build()); // 200 OK или 404 Not Found
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Post> update(@PathVariable Long id, @Valid @RequestBody Post data) {
//        Optional<Post> maybePost = postRepository.findById(id);
//
//        if (maybePost.isPresent()) {
//            Post post = maybePost.get();
//            post.setTitle(data.getTitle());
//            post.setContent(data.getContent());
//            post.setPublished(data.getPublished());
//            Post updated = postRepository.save(post);
//            return ResponseEntity.ok(updated);
//        }
//
//        return ResponseEntity.notFound().build();
//    }
//
//    @DeleteMapping("/{id}") // Удаление страницы
//    public ResponseEntity<Void> destroy(@PathVariable Long id) {
//        if (!postRepository.existsById(id)) {
//            return ResponseEntity.notFound().build(); // 404
//        }
//
//        postRepository.deleteById(id);
//        return ResponseEntity.noContent().build(); // 204
//    }


    // GET /api/posts — список всех постов
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // GET /api/posts/{id} – просмотр конкретного поста
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    // POST /api/posts – создание нового поста
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(post);
    }

    // PUT /api/posts/{id} – обновление поста
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setPublished(postDetails.getPublished());
        // Добавьте другие поля, которые нужно обновлять

        return postRepository.save(post);
    }

    // DELETE /api/posts/{id} – удаление поста
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        // Сначала удаляем все комментарии этого поста
        commentRepository.deleteByPostId(id);
        // Затем удаляем сам пост
        postRepository.delete(post);
    }
}
