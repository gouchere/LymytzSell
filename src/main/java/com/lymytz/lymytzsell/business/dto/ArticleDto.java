package com.lymytz.lymytzsell.business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private String refArticle;
    private String designation;
}
