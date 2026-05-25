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
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_ristourne")
@NamedQueries({
        @NamedQuery(name = "YvsComRistourne.findAll", query = "SELECT y FROM YvsComRistourne y"),
        @NamedQuery(name = "YvsComRistourne.findById", query = "SELECT y FROM YvsComRistourne y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComRistourne.findByDateDebut", query = "SELECT y FROM YvsComRistourne y WHERE y.dateDebut = :dateDebut"),
        @NamedQuery(name = "YvsComRistourne.findByDateFin", query = "SELECT y FROM YvsComRistourne y WHERE y.dateFin = :dateFin"),
        @NamedQuery(name = "YvsComRistourne.findByPermanent", query = "SELECT y FROM YvsComRistourne y WHERE y.permanent = :permanent"),
        @NamedQuery(name = "YvsComRistourne.findByActif", query = "SELECT y FROM YvsComRistourne y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsComRistourne.findByNature", query = "SELECT y FROM YvsComRistourne y WHERE y.nature = :nature"),
        @NamedQuery(name = "YvsComRistourne.findByDateUpdate", query = "SELECT y FROM YvsComRistourne y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComRistourne.findByDateSave", query = "SELECT y FROM YvsComRistourne y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComRistourne implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "nature")
    private Character nature;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseFamilleArticle famille;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne
    private YvsComPlanRistourne plan;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComRistourne(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComRistourne[ id=" + id + " ]";
    }


}
