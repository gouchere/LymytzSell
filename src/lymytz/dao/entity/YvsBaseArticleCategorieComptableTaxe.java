/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lymytz.dao.entity;

import java.io.Serializable;
import java.util.Date;
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

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_article_categorie_comptable_taxe")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findAll", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findById", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByAppRemise", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.appRemise = :appRemise"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByActif", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByDateUpdate", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByDateSave", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.dateSave = :dateSave")})
public class YvsBaseArticleCategorieComptableTaxe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "app_remise")
    private Boolean appRemise;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "article_categorie", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticleCategorieComptable articleCategorie;
    @JoinColumn(name = "taxe", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseTaxes taxe;
//    @JoinColumn(name = "author", referencedColumnName = "id")
//    @ManyToOne
//    private YvsUsersAgence author;

    public YvsBaseArticleCategorieComptableTaxe() {
    }

    public YvsBaseArticleCategorieComptableTaxe(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAppRemise() {
        return appRemise;
    }

    public void setAppRemise(Boolean appRemise) {
        this.appRemise = appRemise;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsBaseArticleCategorieComptable getArticleCategorie() {
        return articleCategorie;
    }

    public void setArticleCategorie(YvsBaseArticleCategorieComptable articleCategorie) {
        this.articleCategorie = articleCategorie;
    }

    public YvsBaseTaxes getTaxe() {
        return taxe;
    }

    public void setTaxe(YvsBaseTaxes taxe) {
        this.taxe = taxe;
    }

//    public YvsUsersAgence getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(YvsUsersAgence author) {
//        this.author = author;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsBaseArticleCategorieComptableTaxe)) {
            return false;
        }
        YvsBaseArticleCategorieComptableTaxe other = (YvsBaseArticleCategorieComptableTaxe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticleCategorieComptableTaxe[ id=" + id + " ]";
    }
    
}
