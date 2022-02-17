/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_article_categorie_comptable")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findAll", query = "SELECT y FROM YvsBaseArticleCategorieComptable y"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByCategorieArticle", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.categorie = :categorie AND y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findById", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByActif", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByDateUpdate", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByDateSave", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.dateSave = :dateSave")})
public class YvsBaseArticleCategorieComptable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @OneToMany(mappedBy = "articleCategorie")
    private List<YvsBaseArticleCategorieComptableTaxe> taxes;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCategorieComptable categorie;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseArticleCategorieComptable() {
    }

    public YvsBaseArticleCategorieComptable(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<YvsBaseArticleCategorieComptableTaxe> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsBaseArticleCategorieComptableTaxe> taxes) {
        this.taxes = taxes;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsBaseCategorieComptable getCategorie() {
        return categorie;
    }

    public void setCategorie(YvsBaseCategorieComptable categorie) {
        this.categorie = categorie;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsBaseArticleCategorieComptable)) {
            return false;
        }
        YvsBaseArticleCategorieComptable other = (YvsBaseArticleCategorieComptable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticleCategorieComptable[ id=" + id + " ]";
    }

}
