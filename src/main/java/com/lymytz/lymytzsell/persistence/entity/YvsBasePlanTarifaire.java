/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_plan_tarifaire")
@NamedQueries({
        @NamedQuery(name = "YvsBasePlanTarifaire.findAll", query = "SELECT y FROM YvsBasePlanTarifaire y"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findById", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByPuv", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.puv = :puv"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByRemise", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.remise = :remise"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByCoefAugmentation", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.coefAugmentation = :coefAugmentation"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByNatureCoefAugmentation", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.natureCoefAugmentation = :natureCoefAugmentation"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByRistourne", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.ristourne = :ristourne"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByNatureRemise", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.natureRemise = :natureRemise"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByNatureRistourne", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.natureRistourne = :natureRistourne"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByPuvMin", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.puvMin = :puvMin"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByNaturePrixMin", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.naturePrixMin = :naturePrixMin"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByDateUpdate", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByDateSave", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBasePlanTarifaire.findByActif", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.actif = :actif")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBasePlanTarifaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "puv")
    private Double puv;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "coef_augmentation")
    private Double coefAugmentation;
    @Column(name = "nature_coef_augmentation")
    private String natureCoefAugmentation;
    @Column(name = "ristourne")
    private Double ristourne;
    @Column(name = "nature_remise")
    private String natureRemise;
    @Column(name = "nature_ristourne")
    private String natureRistourne;
    @Column(name = "puv_min")
    private Double puvMin;
    @Column(name = "nature_prix_min")
    private String naturePrixMin;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCategorieClient categorie;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseFamilleArticle famille;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBasePlanTarifaire(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBasePlanTarifaire[ id=" + id + " ]";
    }

}
