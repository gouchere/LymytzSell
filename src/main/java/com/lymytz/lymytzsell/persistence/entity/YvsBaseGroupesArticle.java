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
@Table(name = "yvs_base_groupes_article")
@NamedQueries({
        @NamedQuery(name = "YvsBaseGroupesArticle.findAll", query = "SELECT y FROM YvsBaseGroupesArticle y"),
        @NamedQuery(name = "YvsBaseGroupesArticle.findById", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseGroupesArticle.findByDescription", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.description = :description"),
        @NamedQuery(name = "YvsBaseGroupesArticle.findByRefGroupe", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.refGroupe = :refGroupe"),
        @NamedQuery(name = "YvsBaseGroupesArticle.findByCodeAppel", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.codeAppel = :codeAppel"),
        @NamedQuery(name = "YvsBaseGroupesArticle.findByActif", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBaseGroupesArticle.findByDesignation", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.designation = :designation"),
        @NamedQuery(name = "YvsBaseGroupesArticle.findByDateUpdate", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseGroupesArticle.findByDateSave", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseGroupesArticle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "description")
    private String description;
    @Column(name = "refgroupe")
    private String refGroupe;
    @Column(name = "code_appel")
    private String codeAppel;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "designation")
    private String designation;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "groupe_parent", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseGroupesArticle groupeParent;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseGroupesArticle(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseGroupesArticle[ id=" + id + " ]";
    }

}
