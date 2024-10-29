/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.dao.entity;

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
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_base_conditionnement_point")
@NamedQueries({
        @NamedQuery(name = "YvsBaseConditionnementPoint.findAll", query = "SELECT y FROM YvsBaseConditionnementPoint y"),
        @NamedQuery(name = "YvsBaseConditionnementPoint.findById", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseConditionnementPoint.findByPuv", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.puv = :puv"),
        @NamedQuery(name = "YvsBaseConditionnementPoint.findByPrixMin", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.prixMin = :prixMin"),
        @NamedQuery(name = "YvsBaseConditionnementPoint.findByNaturePrixMin", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.naturePrixMin = :naturePrixMin"),
        @NamedQuery(name = "YvsBaseConditionnementPoint.findByRemise", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.remise = :remise"),
        @NamedQuery(name = "YvsBaseConditionnementPoint.findByNatureRemise", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.natureRemise = :natureRemise"),
        @NamedQuery(name = "YvsBaseConditionnementPoint.findByDateUpdate", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseConditionnementPoint.findByDateSave", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseConditionnementPoint.findByAvanceCommance", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.avanceCommance = :avanceCommance")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseConditionnementPoint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "puv")
    private Double puv;
    @Column(name = "prix_min")
    private Double prixMin;
    @Column(name = "nature_prix_min")
    private String naturePrixMin;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "nature_remise")
    private String natureRemise;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "change_prix")
    private Boolean changePrix;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "avance_commance")
    private Double avanceCommance;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticlePoint article;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseConditionnementPoint(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseConditionnementPoint[ id=" + id + " ]";
    }

}
