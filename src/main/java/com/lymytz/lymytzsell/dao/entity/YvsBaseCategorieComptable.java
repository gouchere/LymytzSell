/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lymytz.lymytzsell.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
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
 *
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_categorie_comptable")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCategorieComptable.findAll", query = "SELECT y FROM YvsBaseCategorieComptable y"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findById", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByCodeAppel", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.codeAppel = :codeAppel"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByCode", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByNature", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.nature = :nature"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByDesignation", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByActif", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByDateUpdate", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByDateSave", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class YvsBaseCategorieComptable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_appel")
    private String codeAppel;
    @Column(name = "code")
    private String code;
    @Column(name = "nature")
    private String nature;
    @Column(name = "designation")
    private String designation;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;  

    public YvsBaseCategorieComptable() {
    }

    public YvsBaseCategorieComptable(Long id) {
        this.id = id;
    }
    public YvsBaseCategorieComptable(YvsBaseCategorieComptable y) {
        this(y.getId());
        codeAppel=y.codeAppel;
        code=y.code;
        nature=y.nature;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseCategorieComptable[ id=" + id + " ]";
    }
    
}
