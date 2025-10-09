package io.hexlet.spring.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PostParamsDTO {
    private String titleCont; //titleCont выбирает посты по вхождению в название поста (здесь Cont обозначает contain — «содержать»)
    private Long authorId;
    private String contentCont;
    private LocalDate createdAtGt;
    private LocalDate createdAtLt;
}