package io.hexlet.spring.dto.tag;

import org.openapitools.jackson.nullable.JsonNullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TagUpdateDTO {
    @NotNull
    private JsonNullable<String> name;
}