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
@Table(name = "yvs_base_famille_article")
@NamedQueries({
    @NamedQuery(name = "YvsBaseFamilleArticle.findAll", query = "SELECT y FROM YvsBaseFamilleArticle y"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findById", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByReferenceFamille", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.referenceFamille = :referenceFamille"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByDesignation", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByDescription", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByActif", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByDateUpdate", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByDateSave", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByPrefixe", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.prefixe = :prefixe")})
public class YvsBaseFamilleArticle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "reference_famille")
    private String referenceFamille;
    @Column(name = "designation")
    private String designation;
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "prefixe")
    private String prefixe;
    @JoinColumn(name = "famille_parent", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseFamilleArticle familleParent;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseFamilleArticle() {
    }

    public YvsBaseFamilleArticle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceFamille() {
        return referenceFamille;
    }

    public void setReferenceFamille(String referenceFamille) {
        this.referenceFamille = referenceFamille;
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

    public String getPrefixe() {
        return prefixe;
    }

    public void setPrefixe(String prefixe) {
        this.prefixe = prefixe;
    }  

    public YvsBaseFamilleArticle getFamilleParent() {
        return familleParent;
    }

    public void setFamilleParent(YvsBaseFamilleArticle familleParent) {
        this.familleParent = familleParent;
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
        if (!(object instanceof YvsBaseFamilleArticle)) {
            return false;
        }
        YvsBaseFamilleArticle other = (YvsBaseFamilleArticle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseFamilleArticle[ id=" + id + " ]";
    }
    
}
