package io.hexlet.spring.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TagCreateDTO {
    @NotBlank
    @NotNull
    private String name;
}