package io.hexlet.spring.controller;

import io.hexlet.spring.dto.post.*;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.CommentRepository;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.service.PostService;
import io.hexlet.spring.specification.PostSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private io.hexlet.spring.mapper.PostMapper postMapper;

    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PostDTO>> index() {
        var posts = postService.getAll();
        var postDTOs = posts.stream()
                .map(postMapper::map)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(postDTOs.size()))
                .body(postDTOs);
    }

    @GetMapping("/posts/paged")
    public ResponseEntity<Page<PostDTO>> getPostsPaged(PostParamsDTO params,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        Page<Post> posts = postService.getPosts(params, pageable);
        Page<PostDTO> postDTOs = posts.map(postMapper::map);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.getTotalElements()))
                .body(postDTOs);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDTO> show(@PathVariable Long id) {
        return postService.findById(id)
                .map(post -> ResponseEntity.ok(postMapper.map(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostCreateDTO dto) {
        Post post = postMapper.map(dto);
        Post createdPost = postService.create(post);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postMapper.map(createdPost));
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostDTO> update(@PathVariable Long id,
                                          @Valid @RequestBody PostUpdateDTO dto) {
        try {
            // Используем новый метод map для PostUpdateDTO
            Post post = postMapper.map(dto);
            post.setId(id);
            Post updatedPost = postService.update(id, post);
            return ResponseEntity.ok(postMapper.map(updatedPost));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<PostDTO> patch(@PathVariable Long id,
                                         @RequestBody PostPatchDTO dto) {
        try {
            Post postUpdates = new Post();
            dto.getTitle().ifPresent(postUpdates::setTitle);
            dto.getContent().ifPresent(postUpdates::setContent);
            dto.getPublished().ifPresent(postUpdates::setPublished);

            Post updatedPost = postService.patch(id, postUpdates);
            return ResponseEntity.ok(postMapper.map(updatedPost));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}