package io.hexlet.spring.controller;

import io.hexlet.spring.dto.author.AuthorCreateDTO;
import io.hexlet.spring.dto.author.AuthorDTO;
import io.hexlet.spring.dto.author.AuthorUpdateDTO;
import io.hexlet.spring.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorsController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("")
    List<AuthorDTO> index() {
        return authorService.getAllAuthors();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    AuthorDTO create(@Valid @RequestBody AuthorCreateDTO authorData) {
        return authorService.createAuthor(authorData);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    AuthorDTO show(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    AuthorDTO update(@RequestBody @Valid AuthorUpdateDTO authorData, @PathVariable Long id) {
        return authorService.updateAuthor(authorData, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }
}