package io.hexlet.spring.controller;

import io.hexlet.spring.dto.tag.TagCreateDTO;
import io.hexlet.spring.dto.tag.TagDTO;
import io.hexlet.spring.dto.tag.TagUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.TagMapper;
import io.hexlet.spring.repository.TagRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagRepository repository;

    @Autowired
    private TagMapper tagMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDTO> index() {
        var tags = repository.findAll();
        return tags.stream()
                .map(tagMapper::map)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO create(@Valid @RequestBody TagCreateDTO tagData) {
        var tag = tagMapper.map(tagData);
        repository.save(tag);
        return tagMapper.map(tag);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDTO show(@PathVariable Long id) {
        var tag = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag Not Found: " + id));
        return tagMapper.map(tag);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDTO update(@RequestBody @Valid TagUpdateDTO tagData, @PathVariable Long id) {
        var tag = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag Not Found: " + id));
        tagMapper.update(tagData, tag);
        repository.save(tag);
        return tagMapper.map(tag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}