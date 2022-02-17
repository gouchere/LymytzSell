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
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_base_article_point")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticlePoint.findAll", query = "SELECT y FROM YvsBaseArticlePoint y"),
    @NamedQuery(name = "YvsBaseArticlePoint.findById", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByChangePrix", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.changePrix = :changePrix"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByActif", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByPrioritaire", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.prioritaire = :prioritaire"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByNaturePrixMin", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.naturePrixMin = :naturePrixMin"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByRemise", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.remise = :remise"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByNatureRemise", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.natureRemise = :natureRemise"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByDateUpdate", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByDateSave", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.dateSave = :dateSave")})
public class YvsBaseArticlePoint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "change_prix")
    private Boolean changePrix;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "prioritaire")
    private Boolean prioritaire;
    @Column(name = "nature_prix_min")
    private String naturePrixMin;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "remise")
    private Double remise;
    @Column(name = "nature_remise")
    private String natureRemise;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "point", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePointVente point;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseArticlePoint() {
    }

    public YvsBaseArticlePoint(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getChangePrix() {
        return changePrix;
    }

    public void setChangePrix(Boolean changePrix) {
        this.changePrix = changePrix;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getPrioritaire() {
        return prioritaire;
    }

    public void setPrioritaire(Boolean prioritaire) {
        this.prioritaire = prioritaire;
    }

    public String getNaturePrixMin() {
        return naturePrixMin;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public Double getRemise() {
        return remise;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public String getNatureRemise() {
        return natureRemise;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
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

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsBasePointVente getPoint() {
        return point;
    }

    public void setPoint(YvsBasePointVente point) {
        this.point = point;
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
        if (!(object instanceof YvsBaseArticlePoint)) {
            return false;
        }
        YvsBaseArticlePoint other = (YvsBaseArticlePoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticlePoint[ id=" + id + " ]";
    }

}
