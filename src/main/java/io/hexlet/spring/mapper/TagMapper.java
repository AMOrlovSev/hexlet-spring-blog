package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.tag.TagCreateDTO;
import io.hexlet.spring.dto.tag.TagDTO;
import io.hexlet.spring.dto.tag.TagUpdateDTO;
import io.hexlet.spring.model.Tag;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TagMapper {
    public abstract Tag map(TagCreateDTO dto);
    public abstract TagDTO map(Tag model);
    public abstract void update(TagUpdateDTO dto, @MappingTarget Tag model);
}
