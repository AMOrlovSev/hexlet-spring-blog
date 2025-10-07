package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.post.PostCreateDTO;
import io.hexlet.spring.dto.post.PostDTO;
import io.hexlet.spring.dto.post.PostUpdateDTO;
import io.hexlet.spring.model.Post;
import org.mapstruct.*;

@Mapper(
        // Подключение JsonNullableMapper
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PostMapper {

    PostDTO toDTO(Post post);

    Post toEntity(PostCreateDTO dto);

    void updateEntityFromDTO(PostUpdateDTO dto, @MappingTarget Post post);
}