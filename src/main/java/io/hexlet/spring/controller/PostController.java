package io.hexlet.spring.controller;

import io.hexlet.spring.dto.post.*;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.CommentRepository;
import io.hexlet.spring.repository.PostRepository;
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
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostSpecification postSpecification;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PostDTO> listPosts(PostParamsDTO params,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        var spec = postSpecification.build(params);
        var pageable = PageRequest.of(page, size);
        var posts = postRepository.findAll(spec, pageable);
        return posts.map(postMapper::map);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(post -> ResponseEntity.ok(postMapper.map(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostCreateDTO dto) {
        var post = postMapper.map(dto);
        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.map(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> update(@PathVariable Long id,
                                          @Valid @RequestBody PostUpdateDTO dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        postMapper.update(dto, post);
        postRepository.save(post);

        return ResponseEntity.ok(postMapper.map(post));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        commentRepository.deleteByPostId(id);
        postRepository.delete(post);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostDTO> patchPost(@PathVariable Long id,
                                             @RequestBody PostPatchDTO dto) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        dto.getTitle().ifPresent(post::setTitle);
        dto.getContent().ifPresent(post::setContent);
        dto.getPublished().ifPresent(post::setPublished);

        postRepository.save(post);
        return ResponseEntity.ok(postMapper.map(post));
    }
}