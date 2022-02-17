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
@Table(name = "yvs_base_groupes_article")
@NamedQueries({
    @NamedQuery(name = "YvsBaseGroupesArticle.findAll", query = "SELECT y FROM YvsBaseGroupesArticle y"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findById", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByDescription", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByRefGroupe", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.refGroupe = :refGroupe"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByCodeAppel", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.codeAppel = :codeAppel"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByActif", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByDesignation", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByDateUpdate", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByDateSave", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.dateSave = :dateSave")})
public class YvsBaseGroupesArticle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "description")
    private String description;
    @Column(name = "refgroupe")
    private String refGroupe;
    @Column(name = "code_appel")
    private String codeAppel;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "designation")
    private String designation;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
   @JoinColumn(name = "groupe_parent", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseGroupesArticle groupeParent;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseGroupesArticle() {
    }

    public YvsBaseGroupesArticle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefGroupe() {
        return refGroupe;
    }

    public void setRefGroupe(String refGroupe) {
        this.refGroupe = refGroupe;
    }

    public String getCodeAppel() {
        return codeAppel;
    }

    public void setCodeAppel(String codeAppel) {
        this.codeAppel = codeAppel;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public YvsBaseGroupesArticle getGroupeParent() {
        return groupeParent;
    }

    public void setGroupeParent(YvsBaseGroupesArticle groupeParent) {
        this.groupeParent = groupeParent;
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
        if (!(object instanceof YvsBaseGroupesArticle)) {
            return false;
        }
        YvsBaseGroupesArticle other = (YvsBaseGroupesArticle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseGroupesArticle[ id=" + id + " ]";
    }
    
}
