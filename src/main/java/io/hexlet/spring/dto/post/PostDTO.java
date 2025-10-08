package io.hexlet.spring.dto.post;

import io.hexlet.spring.dto.tag.TagDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private Set<TagDTO> tags;
}
