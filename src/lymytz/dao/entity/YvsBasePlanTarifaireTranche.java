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
@Table(name = "yvs_base_plan_tarifaire_tranche")
@NamedQueries({
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findAll", query = "SELECT y FROM YvsBasePlanTarifaireTranche y"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findById", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByBase", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.base = :base"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByValeurMin", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.valeurMin = :valeurMin"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByValeurMax", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.valeurMax = :valeurMax"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByRemise", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.remise = :remise"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByNatureRemise", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.natureRemise = :natureRemise"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByPuv", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.puv = :puv"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByDateUpdate", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByDateSave", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.dateSave = :dateSave")})
public class YvsBasePlanTarifaireTranche implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "base")
    private String base;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur_min")
    private Double valeurMin;
    @Column(name = "valeur_max")
    private Double valeurMax;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "nature_remise")
    private String natureRemise;
    @Column(name = "puv")
    private Double puv;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePlanTarifaire plan;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBasePlanTarifaireTranche() {
    }

    public YvsBasePlanTarifaireTranche(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Double getValeurMin() {
        return valeurMin;
    }

    public void setValeurMin(Double valeurMin) {
        this.valeurMin = valeurMin;
    }

    public Double getValeurMax() {
        return valeurMax;
    }

    public void setValeurMax(Double valeurMax) {
        this.valeurMax = valeurMax;
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

    public Double getPuv() {
        return puv;
    }

    public void setPuv(Double puv) {
        this.puv = puv;
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

    public YvsBasePlanTarifaire getPlan() {
        return plan;
    }

    public void setPlan(YvsBasePlanTarifaire plan) {
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
        if (!(object instanceof YvsBasePlanTarifaireTranche)) {
            return false;
        }
        YvsBasePlanTarifaireTranche other = (YvsBasePlanTarifaireTranche) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBasePlanTarifaireTranche[ id=" + id + " ]";
    }
    
}
