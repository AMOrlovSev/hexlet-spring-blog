package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.user.UserCreateDTO;
import io.hexlet.spring.dto.user.UserDTO;
import io.hexlet.spring.dto.user.UserUpdateDTO;
import io.hexlet.spring.model.User;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    UserDTO map(User user);
    User map(UserCreateDTO dto);
    void update(UserUpdateDTO dto, @MappingTarget User user);
}