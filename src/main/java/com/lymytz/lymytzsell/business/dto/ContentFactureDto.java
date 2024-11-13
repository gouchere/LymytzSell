package com.lymytz.lymytzsell.business.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContentFactureDto {
    private Double quantite;
    private Double prix;
    private Double remise;
    private Double taxe;
    private Double ristourne;
    private Double comission;
    private String numSerie;
    private Double pr;
    private Double puvMin;
    private Double quantiteBonus;
    private Double prixTotal;
    private Double tauxRemise;
    private ArticleDto article;
    private ConditionnementDto conditionnement;
}
