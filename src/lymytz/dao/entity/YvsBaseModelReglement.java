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
@Table(name = "yvs_base_model_reglement")
@NamedQueries({
    @NamedQuery(name = "YvsBaseModelReglement.findAll", query = "SELECT y FROM YvsBaseModelReglement y "),
    @NamedQuery(name = "YvsBaseModelReglement.findById", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseModelReglement.findByReference", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsBaseModelReglement.findByDescription", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseModelReglement.findByActif", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.actif = :actif AND y.societe=:societe"),
    @NamedQuery(name = "YvsBaseModelReglement.findByType", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.type = :type"),
    @NamedQuery(name = "YvsBaseModelReglement.findByDateUpdate", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseModelReglement.findByDateSave", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.dateSave = :dateSave")})
public class YvsBaseModelReglement implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "reference")
    private String reference;
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "type")
    private Character type;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseModelReglement() {
    }

    public YvsBaseModelReglement(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
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

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsBaseModelReglement)) {
            return false;
        }
        YvsBaseModelReglement other = (YvsBaseModelReglement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseModelReglement_1[ id=" + id + " ]";
    }
    
}
