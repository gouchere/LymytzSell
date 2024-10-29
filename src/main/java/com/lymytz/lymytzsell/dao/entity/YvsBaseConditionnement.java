/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.dao.entity;

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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_conditionnement")
@NamedQueries({
        @NamedQuery(name = "YvsBaseConditionnement.findAll", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.article.actif=true"),
        @NamedQuery(name = "YvsBaseConditionnement.findById", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseConditionnement.findByPrix", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.prix = :prix"),
        @NamedQuery(name = "YvsBaseConditionnement.findByPrixMin", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.prixMin = :prixMin"),
        @NamedQuery(name = "YvsBaseConditionnement.findByNaturePrixMin", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.naturePrixMin = :naturePrixMin"),
        @NamedQuery(name = "YvsBaseConditionnement.findByRemise", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.remise = :remise"),
        @NamedQuery(name = "YvsBaseConditionnement.findByCondVente", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.condVente = :condVente"),
        @NamedQuery(name = "YvsBaseConditionnement.findByDateUpdate", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseConditionnement.findByDateSave", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseConditionnement.findByPrixAchat", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.prixAchat = :prixAchat"),
        @NamedQuery(name = "YvsBaseConditionnement.findByPhoto", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.photo = :photo"),
        @NamedQuery(name = "YvsBaseConditionnement.findByByAchat", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.byAchat = :byAchat"),
        @NamedQuery(name = "YvsBaseConditionnement.findByByProd", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.byProd = :byProd"),
        @NamedQuery(name = "YvsBaseConditionnement.findByDefaut", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.defaut = :defaut"),
        @NamedQuery(name = "YvsBaseConditionnement.findByPrixProd", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.prixProd = :prixProd"),
        @NamedQuery(name = "YvsBaseConditionnement.findByMargeMin", query = "SELECT y FROM YvsBaseConditionnement y WHERE y.margeMin = :margeMin")})
@Getter
@Setter
@NoArgsConstructor
public class YvsBaseConditionnement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "prix")
    private Double prix;
    @Column(name = "prix_min")
    private Double prixMin;
    @Column(name = "nature_prix_min")
    private String naturePrixMin;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "cond_vente")
    private Boolean condVente;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "prix_achat")
    private Double prixAchat;
    @Column(name = "photo")
    private String photo;
    @Column(name = "by_achat")
    private Boolean byAchat;
    @Column(name = "by_prod")
    private Boolean byProd;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "prix_prod")
    private Double prixProd;
    @Column(name = "marge_min")
    private Double margeMin;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "unite", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseUniteMesure unite;
    @Transient
    private double stock;

    public YvsBaseConditionnement(Long id) {
        this.id = id;
    }

    public Double getPrix() {
        return prix != null ? prix : 0d;
    }

    public Double getPrixMin() {
        return prixMin != null ? prixMin : 0d;
    }

    public Boolean getActif() {
        return actif != null && actif;
    }

    @XmlTransient
    public double getStock() {
        return stock;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof YvsBaseConditionnement)) {
            return false;
        }
        YvsBaseConditionnement other = (YvsBaseConditionnement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseConditionnement[ id=" + id + " ]";
    }

}
