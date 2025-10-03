package io.hexlet.spring.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import static jakarta.persistence.GenerationType.IDENTITY;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.GeneratedValue;

@Entity
@Table(name = "tasks")
// BEGIN (write your solution here)
@EntityListeners(AuditingEntityListener.class)
// END
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private String title;

    private String description;

    // BEGIN (write your solution here)
    @LastModifiedDate
    // END
    private LocalDate updatedAt;

    // BEGIN (write your solution here)
    @CreatedDate
    // END
    private LocalDate createdAt;
}