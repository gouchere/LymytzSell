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
@Table(name = "yvs_base_unite_mesure")
@NamedQueries({
    @NamedQuery(name = "YvsBaseUniteMesure.findAll", query = "SELECT y FROM YvsBaseUniteMesure y"),
    @NamedQuery(name = "YvsBaseUniteMesure.findById", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByReference", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByLibelle", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByDescription", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByType", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.type = :type"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByDateUpdate", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByDateSave", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByDefaut", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.defaut = :defaut")})
public class YvsBaseUniteMesure implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "reference")
    private String reference;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "description")
    private String description;
    @Column(name = "type")
    private String type;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "defaut")
    private Boolean defaut;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseUniteMesure() {
    }

    public YvsBaseUniteMesure(Long id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public Boolean getDefaut() {
        return defaut;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
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
        if (!(object instanceof YvsBaseUniteMesure)) {
            return false;
        }
        YvsBaseUniteMesure other = (YvsBaseUniteMesure) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseUniteMesure[ id=" + id + " ]";
    }
    
}
