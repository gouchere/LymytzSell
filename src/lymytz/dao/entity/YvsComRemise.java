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
import javax.validation.constraints.Size;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_remise")
@NamedQueries({
    @NamedQuery(name = "YvsComRemise.findAll", query = "SELECT y FROM YvsComRemise y"),
    @NamedQuery(name = "YvsComRemise.findById", query = "SELECT y FROM YvsComRemise y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComRemise.findByRefRemise", query = "SELECT y FROM YvsComRemise y WHERE y.refRemise = :refRemise"),
    @NamedQuery(name = "YvsComRemise.findByPermanent", query = "SELECT y FROM YvsComRemise y WHERE y.permanent = :permanent"),
    @NamedQuery(name = "YvsComRemise.findByActif", query = "SELECT y FROM YvsComRemise y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComRemise.findByDateDebut", query = "SELECT y FROM YvsComRemise y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsComRemise.findByDateFin", query = "SELECT y FROM YvsComRemise y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsComRemise.findByDateUpdate", query = "SELECT y FROM YvsComRemise y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComRemise.findByDateSave", query = "SELECT y FROM YvsComRemise y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComRemise.findByDescription", query = "SELECT y FROM YvsComRemise y WHERE y.description = :description")})
public class YvsComRemise implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "ref_remise")
    private String refRemise;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "remise")
    private List<YvsComGrilleRemise> yvsComGrilleRemiseList;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComRemise() {
    }

    public YvsComRemise(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefRemise() {
        return refRemise;
    }

    public void setRefRemise(String refRemise) {
        this.refRemise = refRemise;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<YvsComGrilleRemise> getYvsComGrilleRemiseList() {
        return yvsComGrilleRemiseList;
    }

    public void setYvsComGrilleRemiseList(List<YvsComGrilleRemise> yvsComGrilleRemiseList) {
        this.yvsComGrilleRemiseList = yvsComGrilleRemiseList;
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
        if (!(object instanceof YvsComRemise)) {
            return false;
        }
        YvsComRemise other = (YvsComRemise) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComRemise[ id=" + id + " ]";
    }
    
}
