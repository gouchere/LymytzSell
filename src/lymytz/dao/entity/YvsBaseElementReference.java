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
@Table(name = "yvs_base_element_reference")
@NamedQueries({
    @NamedQuery(name = "YvsBaseElementReference.findAll", query = "SELECT y FROM YvsBaseElementReference y"),
    @NamedQuery(name = "YvsBaseElementReference.findById", query = "SELECT y FROM YvsBaseElementReference y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseElementReference.findByDesignation", query = "SELECT y FROM YvsBaseElementReference y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseElementReference.findByModule", query = "SELECT y FROM YvsBaseElementReference y WHERE y.module = :module"),
    @NamedQuery(name = "YvsBaseElementReference.findByDateUpdate", query = "SELECT y FROM YvsBaseElementReference y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseElementReference.findByDateSave", query = "SELECT y FROM YvsBaseElementReference y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseElementReference.findByModelCourant", query = "SELECT y FROM YvsBaseElementReference y WHERE y.modelCourant = :modelCourant"),
    @NamedQuery(name = "YvsBaseElementReference.findByDefaultPrefix", query = "SELECT y FROM YvsBaseElementReference y WHERE y.defaultPrefix = :defaultPrefix")})
public class YvsBaseElementReference implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "designation")
    private String designation;
    @Column(name = "module")
    private String module;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "model_courant")
    private Boolean modelCourant;
    @Column(name = "default_prefix")
    private String defaultPrefix;

    public YvsBaseElementReference() {
    }

    public YvsBaseElementReference(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    public Boolean getModelCourant() {
        return modelCourant;
    }

    public void setModelCourant(Boolean modelCourant) {
        this.modelCourant = modelCourant;
    }

    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    public void setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
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
        if (!(object instanceof YvsBaseElementReference)) {
            return false;
        }
        YvsBaseElementReference other = (YvsBaseElementReference) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseElementReference[ id=" + id + " ]";
    }

}
