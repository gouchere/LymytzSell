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
@Table(name = "yvs_base_famille_article")
@NamedQueries({
        @NamedQuery(name = "YvsBaseFamilleArticle.findAll", query = "SELECT y FROM YvsBaseFamilleArticle y"),
        @NamedQuery(name = "YvsBaseFamilleArticle.findById", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseFamilleArticle.findByReferenceFamille", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.referenceFamille = :referenceFamille"),
        @NamedQuery(name = "YvsBaseFamilleArticle.findByDesignation", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.designation = :designation"),
        @NamedQuery(name = "YvsBaseFamilleArticle.findByDescription", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.description = :description"),
        @NamedQuery(name = "YvsBaseFamilleArticle.findByActif", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBaseFamilleArticle.findByDateUpdate", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseFamilleArticle.findByDateSave", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseFamilleArticle.findByPrefixe", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.prefixe = :prefixe")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseFamilleArticle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "reference_famille")
    private String referenceFamille;
    @Column(name = "designation")
    private String designation;
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "prefixe")
    private String prefixe;
    @JoinColumn(name = "famille_parent", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseFamilleArticle familleParent;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseFamilleArticle(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseFamilleArticle[ id=" + id + " ]";
    }

}
