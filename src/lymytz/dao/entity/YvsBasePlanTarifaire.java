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
@Table(name = "yvs_base_plan_tarifaire")
@NamedQueries({
    @NamedQuery(name = "YvsBasePlanTarifaire.findAll", query = "SELECT y FROM YvsBasePlanTarifaire y"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findById", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByPuv", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.puv = :puv"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByRemise", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.remise = :remise"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByCoefAugmentation", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.coefAugmentation = :coefAugmentation"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByNatureCoefAugmentation", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.natureCoefAugmentation = :natureCoefAugmentation"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByRistourne", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.ristourne = :ristourne"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByNatureRemise", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.natureRemise = :natureRemise"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByNatureRistourne", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.natureRistourne = :natureRistourne"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByPuvMin", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.puvMin = :puvMin"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByNaturePrixMin", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.naturePrixMin = :naturePrixMin"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByDateUpdate", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByDateSave", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByActif", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.actif = :actif")})
public class YvsBasePlanTarifaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "puv")
    private Double puv;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "coef_augmentation")
    private Double coefAugmentation;
    @Column(name = "nature_coef_augmentation")
    private String natureCoefAugmentation;
    @Column(name = "ristourne")
    private Double ristourne;
    @Column(name = "nature_remise")
    private String natureRemise;
    @Column(name = "nature_ristourne")
    private String natureRistourne;
    @Column(name = "puv_min")
    private Double puvMin;
    @Column(name = "nature_prix_min")
    private String naturePrixMin;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCategorieClient categorie;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseFamilleArticle famille;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBasePlanTarifaire() {
    }

    public YvsBasePlanTarifaire(Long id) {
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

    public Double getRemise() {
        return remise;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public Double getCoefAugmentation() {
        return coefAugmentation;
    }

    public void setCoefAugmentation(Double coefAugmentation) {
        this.coefAugmentation = coefAugmentation;
    }

    public String getNatureCoefAugmentation() {
        return natureCoefAugmentation;
    }

    public void setNatureCoefAugmentation(String natureCoefAugmentation) {
        this.natureCoefAugmentation = natureCoefAugmentation;
    }

    public Double getRistourne() {
        return ristourne;
    }

    public void setRistourne(Double ristourne) {
        this.ristourne = ristourne;
    }

    public String getNatureRemise() {
        return natureRemise;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
    }

    public String getNatureRistourne() {
        return natureRistourne;
    }

    public void setNatureRistourne(String natureRistourne) {
        this.natureRistourne = natureRistourne;
    }

    public Double getPuvMin() {
        return puvMin;
    }

    public void setPuvMin(Double puvMin) {
        this.puvMin = puvMin;
    }

    public String getNaturePrixMin() {
        return naturePrixMin;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsBaseCategorieClient getCategorie() {
        return categorie;
    }

    public void setCategorie(YvsBaseCategorieClient categorie) {
        this.categorie = categorie;
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

    public YvsBaseFamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(YvsBaseFamilleArticle famille) {
        this.famille = famille;
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
        if (!(object instanceof YvsBasePlanTarifaire)) {
            return false;
        }
        YvsBasePlanTarifaire other = (YvsBasePlanTarifaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBasePlanTarifaire[ id=" + id + " ]";
    }

}
