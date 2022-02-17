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
@Table(name = "yvs_com_ristourne")
@NamedQueries({
    @NamedQuery(name = "YvsComRistourne.findAll", query = "SELECT y FROM YvsComRistourne y"),
    @NamedQuery(name = "YvsComRistourne.findById", query = "SELECT y FROM YvsComRistourne y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComRistourne.findByDateDebut", query = "SELECT y FROM YvsComRistourne y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsComRistourne.findByDateFin", query = "SELECT y FROM YvsComRistourne y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsComRistourne.findByPermanent", query = "SELECT y FROM YvsComRistourne y WHERE y.permanent = :permanent"),
    @NamedQuery(name = "YvsComRistourne.findByActif", query = "SELECT y FROM YvsComRistourne y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComRistourne.findByNature", query = "SELECT y FROM YvsComRistourne y WHERE y.nature = :nature"),
    @NamedQuery(name = "YvsComRistourne.findByDateUpdate", query = "SELECT y FROM YvsComRistourne y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComRistourne.findByDateSave", query = "SELECT y FROM YvsComRistourne y WHERE y.dateSave = :dateSave")})
public class YvsComRistourne implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "nature")
    private Character nature;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseFamilleArticle famille;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne
    private YvsComPlanRistourne plan;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComRistourne() {
    }

    public YvsComRistourne(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Character getNature() {
        return nature;
    }

    public void setNature(Character nature) {
        this.nature = nature;
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

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsBaseFamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(YvsBaseFamilleArticle famille) {
        this.famille = famille;
    }

    public YvsComPlanRistourne getPlan() {
        return plan;
    }

    public void setPlan(YvsComPlanRistourne plan) {
        this.plan = plan;
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
        if (!(object instanceof YvsComRistourne)) {
            return false;
        }
        YvsComRistourne other = (YvsComRistourne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComRistourne[ id=" + id + " ]";
    }
    
    
}
