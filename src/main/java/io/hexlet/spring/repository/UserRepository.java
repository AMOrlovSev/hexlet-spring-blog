package io.hexlet.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.hexlet.spring.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}