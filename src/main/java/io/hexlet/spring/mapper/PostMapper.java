package io.hexlet.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import io.hexlet.spring.dto.post.PostCreateDTO;
import io.hexlet.spring.dto.post.PostDTO;
import io.hexlet.spring.dto.post.PostUpdateDTO;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PostMapper {

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "tagIdsToTags")
    public abstract Post map(PostCreateDTO dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagsToTagDTOs")
    public abstract PostDTO map(Post model);

    @Mapping(target = "user", source = "authorId")
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "tagIdsToTags")
    public abstract void update(PostUpdateDTO dto, @MappingTarget Post model);
}