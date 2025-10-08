package io.hexlet.spring.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class PostUpdateDTO {
    @NotBlank
    @Size(min = 3, max = 100)
    private JsonNullable<String> title;

    @NotBlank
    @Size(min = 10)
    private JsonNullable<String> content;

    @NotNull
    private JsonNullable<Set<Long>> tagIds;
}