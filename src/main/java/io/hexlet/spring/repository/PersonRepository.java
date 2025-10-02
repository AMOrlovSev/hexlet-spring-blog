package io.hexlet.spring.repository;

import io.hexlet.spring.model.Person;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
