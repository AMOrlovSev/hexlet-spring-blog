package io.hexlet.spring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tags")
public class Tag implements BaseEntity{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @ToString.Include
    @Column(unique = true)
    private String name;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Обратная сторона связи многие-ко-многим
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Post> posts = new HashSet<>();

    // Дополнительные методы для удобства
    public void addPost(Post post) {
        this.posts.add(post);
        post.getTags().add(this);
    }

    public void removePost(Post post) {
        this.posts.remove(post);
        post.getTags().remove(this);
    }
}
