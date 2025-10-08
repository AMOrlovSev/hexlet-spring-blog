package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.car.CarCreateDTO;
import io.hexlet.spring.dto.car.CarDTO;
import io.hexlet.spring.dto.car.CarUpdateDTO;
import io.hexlet.spring.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class CarMapper {

    public abstract Car map(CarCreateDTO dto);

    public abstract void update(CarUpdateDTO dto, @MappingTarget Car model);

    public abstract CarDTO map(Car model);
}