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
@Table(name = "yvs_base_point_livraison")
@NamedQueries({
    @NamedQuery(name = "YvsBasePointLivraison.findAll", query = "SELECT y FROM YvsBasePointLivraison y"),
    @NamedQuery(name = "YvsBasePointLivraison.findById", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePointLivraison.findByLibelle", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsBasePointLivraison.findByDateSave", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBasePointLivraison.findByDateUpdate", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBasePointLivraison.findByExecuteTrigger", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.executeTrigger = :executeTrigger"),
    @NamedQuery(name = "YvsBasePointLivraison.findByTelephone", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsBasePointLivraison.findByLieuDit", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.lieuDit = :lieuDit"),
    @NamedQuery(name = "YvsBasePointLivraison.findByDescription", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.description = :description")})
public class YvsBasePointLivraison implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "lieu_dit")
    private String lieuDit;
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne
    private YvsDictionnaire ville;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBasePointLivraison() {
    }

    public YvsBasePointLivraison(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLieuDit() {
        return lieuDit;
    }

    public void setLieuDit(String lieuDit) {
        this.lieuDit = lieuDit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsDictionnaire getVille() {
        return ville;
    }

    public void setVille(YvsDictionnaire ville) {
        this.ville = ville;
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
        if (!(object instanceof YvsBasePointLivraison)) {
            return false;
        }
        YvsBasePointLivraison other = (YvsBasePointLivraison) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBasePointLivraison[ id=" + id + " ]";
    }
    
}
