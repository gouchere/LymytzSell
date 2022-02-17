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
@Table(name = "yvs_base_categorie_comptable")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCategorieComptable.findAll", query = "SELECT y FROM YvsBaseCategorieComptable y"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findById", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByCodeAppel", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.codeAppel = :codeAppel"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByCode", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByNature", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.nature = :nature"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByDesignation", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByActif", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByDateUpdate", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseCategorieComptable.findByDateSave", query = "SELECT y FROM YvsBaseCategorieComptable y WHERE y.dateSave = :dateSave")})
public class YvsBaseCategorieComptable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_appel")
    private String codeAppel;
    @Column(name = "code")
    private String code;
    @Column(name = "nature")
    private String nature;
    @Column(name = "designation")
    private String designation;
    @Column(name = "actif")
    private Boolean actif;
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

    public YvsBaseCategorieComptable() {
    }

    public YvsBaseCategorieComptable(Long id) {
        this.id = id;
    }
    public YvsBaseCategorieComptable(YvsBaseCategorieComptable y) {
        this(y.getId());
        codeAppel=y.codeAppel;
        code=y.code;
        nature=y.nature;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAppel() {
        return codeAppel;
    }

    public void setCodeAppel(String codeAppel) {
        this.codeAppel = codeAppel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
        if (!(object instanceof YvsBaseCategorieComptable)) {
            return false;
        }
        YvsBaseCategorieComptable other = (YvsBaseCategorieComptable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseCategorieComptable[ id=" + id + " ]";
    }
    
}
