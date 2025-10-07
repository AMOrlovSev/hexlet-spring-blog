package io.hexlet.spring.repository;

import io.hexlet.spring.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByPhoneNumber(String phoneNumber);
}
