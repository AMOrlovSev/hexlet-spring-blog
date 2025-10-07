package io.hexlet.spring.repository;

import io.hexlet.spring.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
