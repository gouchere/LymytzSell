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
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_comerciale")
@NamedQueries({
    @NamedQuery(name = "YvsComComerciale.findAll", query = "SELECT y FROM YvsComComerciale y"),
    @NamedQuery(name = "YvsComComerciale.findByUser", query = "SELECT y FROM YvsComComerciale y WHERE y.utilisateur = :user"),
    @NamedQuery(name = "YvsComComerciale.findById", query = "SELECT y FROM YvsComComerciale y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComComerciale.findByCodeRef", query = "SELECT y FROM YvsComComerciale y WHERE y.codeRef = :codeRef"),
    @NamedQuery(name = "YvsComComerciale.findByNom", query = "SELECT y FROM YvsComComerciale y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsComComerciale.findByPrenom", query = "SELECT y FROM YvsComComerciale y WHERE y.prenom = :prenom"),
    @NamedQuery(name = "YvsComComerciale.findByTelephone", query = "SELECT y FROM YvsComComerciale y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsComComerciale.findByDateSave", query = "SELECT y FROM YvsComComerciale y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComComerciale.findByDateUpdate", query = "SELECT y FROM YvsComComerciale y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComComerciale.findByActif", query = "SELECT y FROM YvsComComerciale y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComComerciale.findByDefaut", query = "SELECT y FROM YvsComComerciale y WHERE y.defaut = :defaut")})
public class YvsComComerciale implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_ref")
    private String codeRef;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "defaut")
    private Boolean defaut;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne
    private YvsAgences agence;
    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseTiers tiers;
    @JoinColumn(name = "utilisateur", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers utilisateur;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComComerciale() {
    }

    public YvsComComerciale(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getDefaut() {
        return defaut;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsBaseTiers getTiers() {
        return tiers;
    }

    public void setTiers(YvsBaseTiers tiers) {
        this.tiers = tiers;
    }

    public YvsUsers getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(YvsUsers utilisateur) {
        this.utilisateur = utilisateur;
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
        if (!(object instanceof YvsComComerciale)) {
            return false;
        }
        YvsComComerciale other = (YvsComComerciale) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComComerciale[ id=" + id + " ]";
    }

}
