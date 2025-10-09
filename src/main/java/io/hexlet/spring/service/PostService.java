package io.hexlet.spring.service;

import java.util.List;
import java.util.Optional;

import io.hexlet.spring.dto.post.PostCreateDTO;
import io.hexlet.spring.dto.post.PostParamsDTO;
import io.hexlet.spring.dto.post.PostPatchDTO;
import io.hexlet.spring.dto.post.PostUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.CommentRepository;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.specification.PostSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostSpecification postSpecification;

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Page<Post> getPosts(PostParamsDTO params, Pageable pageable) {
        var spec = postSpecification.build(params);
        return postRepository.findAll(spec, pageable);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public Post create(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post update(Long id, Post post) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        post.setId(id);
        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        commentRepository.deleteByPostId(id);
        postRepository.delete(post);
    }

    @Transactional
    public Post patch(Long id, Post postUpdates) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        if (postUpdates.getTitle() != null) {
            existingPost.setTitle(postUpdates.getTitle());
        }
        if (postUpdates.getContent() != null) {
            existingPost.setContent(postUpdates.getContent());
        }
        if (postUpdates.getPublished() != null) {
            existingPost.setPublished(postUpdates.getPublished());
        }

        return postRepository.save(existingPost);
    }
}