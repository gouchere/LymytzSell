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
@Table(name = "yvs_base_conditionnement_point")
@NamedQueries({
    @NamedQuery(name = "YvsBaseConditionnementPoint.findAll", query = "SELECT y FROM YvsBaseConditionnementPoint y"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findById", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByPuv", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.puv = :puv"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByPrixMin", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.prixMin = :prixMin"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByNaturePrixMin", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.naturePrixMin = :naturePrixMin"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByRemise", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.remise = :remise"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByNatureRemise", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.natureRemise = :natureRemise"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByDateUpdate", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByDateSave", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByAvanceCommance", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.avanceCommance = :avanceCommance")})
public class YvsBaseConditionnementPoint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "puv")
    private Double puv;
    @Column(name = "prix_min")
    private Double prixMin;
    @Column(name = "nature_prix_min")
    private String naturePrixMin;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "nature_remise")
    private String natureRemise;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "change_prix")
    private Boolean changePrix;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "avance_commance")
    private Double avanceCommance;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticlePoint article;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseConditionnementPoint() {
    }

    public YvsBaseConditionnementPoint(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPuv() {
        return puv;
    }

    public void setPuv(Double puv) {
        this.puv = puv;
    }

    public Double getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(Double prixMin) {
        this.prixMin = prixMin;
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

    public Double getAvanceCommance() {
        return avanceCommance;
    }

    public void setAvanceCommance(Double avanceCommance) {
        this.avanceCommance = avanceCommance;
    }

    public YvsBaseArticlePoint getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticlePoint article) {
        this.article = article;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean isChangePrix() {
        return changePrix != null ? changePrix : false;
    }

    public void setChangePrix(Boolean changePrix) {
        this.changePrix = changePrix;
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
        if (!(object instanceof YvsBaseConditionnementPoint)) {
            return false;
        }
        YvsBaseConditionnementPoint other = (YvsBaseConditionnementPoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseConditionnementPoint[ id=" + id + " ]";
    }

}
