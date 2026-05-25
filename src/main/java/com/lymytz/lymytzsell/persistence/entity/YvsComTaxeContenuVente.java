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
@Table(name = "yvs_com_taxe_contenu_vente")
@NamedQueries({
        @NamedQuery(name = "YvsComTaxeContenuVente.findAll", query = "SELECT y FROM YvsComTaxeContenuVente y"),
        @NamedQuery(name = "YvsComTaxeContenuVente.findById", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComTaxeContenuVente.findByMontant", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.montant = :montant"),
        @NamedQuery(name = "YvsComTaxeContenuVente.findByDateUpdate", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComTaxeContenuVente.findByDateSave", query = "SELECT y FROM YvsComTaxeContenuVente y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComTaxeContenuVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "taxe", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseTaxes taxe;
    @JoinColumn(name = "contenu", referencedColumnName = "id")
    @ManyToOne
    private YvsComContenuDocVente contenu;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComTaxeContenuVente(Long id) {
        this.id = id;
    }

    public YvsComTaxeContenuVente(YvsComTaxeContenuVente t) {
        this(t.id);
        adresseServeur = t.adresseServeur;
        author = t.author;
        contenu = t.contenu;
        dateSave = t.dateSave;
        dateUpdate = t.dateUpdate;
        montant = t.montant;
        taxe = t.taxe;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComTaxeContenuVente[ id=" + id + " ]";
    }

}
