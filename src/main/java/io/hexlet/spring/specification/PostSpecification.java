package io.hexlet.spring.specification;

import java.time.LocalDate;

import io.hexlet.spring.dto.post.PostParamsDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import io.hexlet.spring.model.Post;

@Component
public class PostSpecification {
    public Specification<Post> build(PostParamsDTO params) {
        return withTitleCont(params.getTitleCont())
                .and(withAuthorId(params.getAuthorId()))
                .and(withContentCont(params.getContentCont()))
                .and(withCreatedAtGt(params.getCreatedAtGt()))
                .and(withCreatedAtLt(params.getCreatedAtLt()));
    }

    private Specification<Post> withTitleCont(String titleCont) {
        return (root, query, cb) -> titleCont == null || titleCont.isEmpty()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("title")), "%" + titleCont.toLowerCase() + "%");
    }

    private Specification<Post> withAuthorId(Long authorId) {
        return (root, query, cb) -> authorId == null
                ? cb.conjunction()
                : cb.equal(root.get("author").get("id"), authorId);
    }

    private Specification<Post> withContentCont(String contentCont) {
        return (root, query, cb) -> contentCont == null || contentCont.isEmpty()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("content")), "%" + contentCont.toLowerCase() + "%");
    }

    private Specification<Post> withCreatedAtGt(LocalDate date) {
        return (root, query, cb) -> date == null
                ? cb.conjunction()
                : cb.greaterThan(root.get("createdAt"), date);
    }

    private Specification<Post> withCreatedAtLt(LocalDate date) {
        return (root, query, cb) -> date == null
                ? cb.conjunction()
                : cb.lessThan(root.get("createdAt"), date);
    }
}