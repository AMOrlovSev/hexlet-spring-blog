package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.author.AuthorCreateDTO;
import io.hexlet.spring.dto.author.AuthorDTO;
import io.hexlet.spring.dto.author.AuthorUpdateDTO;
import io.hexlet.spring.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class AuthorMapper {

    public abstract Author map(AuthorCreateDTO dto);

    public abstract AuthorDTO map(Author model);

    public abstract void update(AuthorUpdateDTO dto, @MappingTarget Author model);
}
