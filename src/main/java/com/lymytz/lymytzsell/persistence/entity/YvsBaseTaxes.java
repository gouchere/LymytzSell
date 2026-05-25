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
@Table(name = "yvs_base_taxes")
@NamedQueries({
        @NamedQuery(name = "YvsBaseTaxes.findAll", query = "SELECT y FROM YvsBaseTaxes y"),
        @NamedQuery(name = "YvsBaseTaxes.findById", query = "SELECT y FROM YvsBaseTaxes y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseTaxes.findByCodeTaxe", query = "SELECT y FROM YvsBaseTaxes y WHERE y.codeTaxe = :codeTaxe"),
        @NamedQuery(name = "YvsBaseTaxes.findByTaux", query = "SELECT y FROM YvsBaseTaxes y WHERE y.taux = :taux"),
        @NamedQuery(name = "YvsBaseTaxes.findByCodeAppel", query = "SELECT y FROM YvsBaseTaxes y WHERE y.codeAppel = :codeAppel"),
        @NamedQuery(name = "YvsBaseTaxes.findByDesignation", query = "SELECT y FROM YvsBaseTaxes y WHERE y.designation = :designation"),
        @NamedQuery(name = "YvsBaseTaxes.findByActif", query = "SELECT y FROM YvsBaseTaxes y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBaseTaxes.findByDateUpdate", query = "SELECT y FROM YvsBaseTaxes y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseTaxes.findByDateSave", query = "SELECT y FROM YvsBaseTaxes y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseTaxes.findByLibellePrint", query = "SELECT y FROM YvsBaseTaxes y WHERE y.libellePrint = :libellePrint")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseTaxes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_taxe")
    private String codeTaxe;
    @Column(name = "taux")
    private Double taux;
    @Column(name = "code_appel")
    private String codeAppel;
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
    @Column(name = "libelle_print")
    private String libellePrint;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseTaxes(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseTaxes[ id=" + id + " ]";
    }

}
