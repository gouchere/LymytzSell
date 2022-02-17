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
@Table(name = "yvs_base_categorie_client")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCategorieClient.findAll", query = "SELECT y FROM YvsBaseCategorieClient y"),
    @NamedQuery(name = "YvsBaseCategorieClient.findById", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByCode", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByLibelle", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByDescription", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByLierClient", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.lierClient = :lierClient"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByDefaut", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.defaut = :defaut"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByActif", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByDateUpdate", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByDateSave", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.dateSave = :dateSave")})
public class YvsBaseCategorieClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "description")
    private String description;
    @Column(name = "lier_client")
    private Boolean lierClient;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCategorieClient parent;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseCategorieClient() {
    }

    public YvsBaseCategorieClient(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Boolean getLierClient() {
        return lierClient;
    }

    public void setLierClient(Boolean lierClient) {
        this.lierClient = lierClient;
    }

    public Boolean getDefaut() {
        return defaut;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
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

    public YvsBaseCategorieClient getParent() {
        return parent;
    }

    public void setParent(YvsBaseCategorieClient parent) {
        this.parent = parent;
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
        if (!(object instanceof YvsBaseCategorieClient)) {
            return false;
        }
        YvsBaseCategorieClient other = (YvsBaseCategorieClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseCategorieClient[ id=" + id + " ]";
    }

}
