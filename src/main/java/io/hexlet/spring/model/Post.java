package io.hexlet.spring.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Post {
    private String id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
