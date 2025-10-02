package io.hexlet.spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "people")
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
}
