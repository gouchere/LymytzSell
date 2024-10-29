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
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_modele_reference")
@NamedQueries({
        @NamedQuery(name = "YvsBaseModeleReference.findAll", query = "SELECT y FROM YvsBaseModeleReference y"),
        @NamedQuery(name = "YvsBaseModeleReference.findByElement", query = "SELECT y FROM YvsBaseModeleReference y WHERE LOWER(y.element.designation) = LOWER(:designation) AND y.societe=:societe"),
        @NamedQuery(name = "YvsBaseModeleReference.findById", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseModeleReference.findByPrefix", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.prefix = :prefix"),
        @NamedQuery(name = "YvsBaseModeleReference.findByJour", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.jour = :jour"),
        @NamedQuery(name = "YvsBaseModeleReference.findByMois", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.mois = :mois"),
        @NamedQuery(name = "YvsBaseModeleReference.findByAnnee", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.annee = :annee"),
        @NamedQuery(name = "YvsBaseModeleReference.findByTaille", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.taille = :taille"),
        @NamedQuery(name = "YvsBaseModeleReference.findBySeparateur", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.separateur = :separateur"),
        @NamedQuery(name = "YvsBaseModeleReference.findByModule", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.module = :module"),
        @NamedQuery(name = "YvsBaseModeleReference.findByCodePoint", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.codePoint = :codePoint"),
        @NamedQuery(name = "YvsBaseModeleReference.findByLongueurCodePoint", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.longueurCodePoint = :longueurCodePoint"),
        @NamedQuery(name = "YvsBaseModeleReference.findByDateUpdate", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseModeleReference.findByDateSave", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseModeleReference.findByElementCode", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.elementCode = :elementCode")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseModeleReference implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "jour")
    private Boolean jour;
    @Column(name = "mois")
    private Boolean mois;
    @Column(name = "annee")
    private Boolean annee;
    @Column(name = "taille")
    private Integer taille;
    @Column(name = "separateur")
    private Character separateur;
    @Column(name = "module")
    private String module;
    @Column(name = "code_point")
    private Boolean codePoint;
    @Column(name = "longueur_code_point")
    private Integer longueurCodePoint;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "element_code")
    private String elementCode;
    @JoinColumn(name = "element", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseElementReference element;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseModeleReference(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseModeleReference[ id=" + id + " ]";
    }

}
