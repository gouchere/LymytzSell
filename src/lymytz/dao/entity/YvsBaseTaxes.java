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
@Table(name = "yvs_base_taxes")
@NamedQueries({
    @NamedQuery(name = "YvsBaseTaxes.findAll", query = "SELECT y FROM YvsBaseTaxes y"),
    @NamedQuery(name = "YvsBaseTaxes.findById", query = "SELECT y FROM YvsBaseTaxes y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseTaxes.findByCodeTaxe", query = "SELECT y FROM YvsBaseTaxes y WHERE y.codeTaxe = :codeTaxe"),
    @NamedQuery(name = "YvsBaseTaxes.findByTaux", query = "SELECT y FROM YvsBaseTaxes y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsBaseTaxes.findByCodeAppel", query = "SELECT y FROM YvsBaseTaxes y WHERE y.codeAppel = :codeAppel"),
    @NamedQuery(name = "YvsBaseTaxes.findByDesignation", query = "SELECT y FROM YvsBaseTaxes y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseTaxes.findByActif", query = "SELECT y FROM YvsBaseTaxes y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseTaxes.findByDateUpdate", query = "SELECT y FROM YvsBaseTaxes y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseTaxes.findByDateSave", query = "SELECT y FROM YvsBaseTaxes y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseTaxes.findByLibellePrint", query = "SELECT y FROM YvsBaseTaxes y WHERE y.libellePrint = :libellePrint")})
public class YvsBaseTaxes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_taxe")
    private String codeTaxe;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "code_appel")
    private String codeAppel;
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
    @Column(name = "libelle_print")
    private String libellePrint;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseTaxes() {
    }

    public YvsBaseTaxes(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeTaxe() {
        return codeTaxe;
    }

    public void setCodeTaxe(String codeTaxe) {
        this.codeTaxe = codeTaxe;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public String getCodeAppel() {
        return codeAppel;
    }

    public void setCodeAppel(String codeAppel) {
        this.codeAppel = codeAppel;
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

    public String getLibellePrint() {
        return libellePrint;
    }

    public void setLibellePrint(String libellePrint) {
        this.libellePrint = libellePrint;
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
        if (!(object instanceof YvsBaseTaxes)) {
            return false;
        }
        YvsBaseTaxes other = (YvsBaseTaxes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseTaxes[ id=" + id + " ]";
    }

}
