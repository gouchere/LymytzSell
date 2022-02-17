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
@Table(name = "yvs_base_mode_reglement")
@NamedQueries({
    @NamedQuery(name = "YvsBaseModeReglement.findAll", query = "SELECT y FROM YvsBaseModeReglement y"),
    @NamedQuery(name = "YvsBaseModeReglement.findById", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseModeReglement.findByDesignation", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseModeReglement.findByDescription", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseModeReglement.findByActif", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.actif = :actif AND y.societe=:societe"),
    @NamedQuery(name = "YvsBaseModeReglement.findByTypeReglement", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.typeReglement = :typeReglement"),
    @NamedQuery(name = "YvsBaseModeReglement.findByDefault", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.typeReglement = :type AND y.actif = :actif AND y.defaultMode = :defaut ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseModeReglement.findByDefaultMode", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.defaultMode = :defaultMode"),
    @NamedQuery(name = "YvsBaseModeReglement.findByDateUpdate", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseModeReglement.findByDateSave", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseModeReglement.findByNumeroMarchand", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.numeroMarchand = :numeroMarchand"),
    @NamedQuery(name = "YvsBaseModeReglement.findByCodePaiement", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.codePaiement = :codePaiement")})
public class YvsBaseModeReglement implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "designation")
    private String designation;
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "type_reglement")
    private String typeReglement;
    @Column(name = "default_mode")
    private Boolean defaultMode;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "numero_marchand")
    private String numeroMarchand;
    @Column(name = "code_paiement")
    private String codePaiement;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseModeReglement() {
    }

    public YvsBaseModeReglement(Long id) {
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

    public String getTypeReglement() {
        return typeReglement;
    }

    public void setTypeReglement(String typeReglement) {
        this.typeReglement = typeReglement;
    }

    public Boolean getDefaultMode() {
        return defaultMode;
    }

    public void setDefaultMode(Boolean defaultMode) {
        this.defaultMode = defaultMode;
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

    public String getNumeroMarchand() {
        return numeroMarchand;
    }

    public void setNumeroMarchand(String numeroMarchand) {
        this.numeroMarchand = numeroMarchand;
    }

    public String getCodePaiement() {
        return codePaiement;
    }

    public void setCodePaiement(String codePaiement) {
        this.codePaiement = codePaiement;
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
        if (!(object instanceof YvsBaseModeReglement)) {
            return false;
        }
        YvsBaseModeReglement other = (YvsBaseModeReglement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseModeReglement[ id=" + id + " ]";
    } 
}
