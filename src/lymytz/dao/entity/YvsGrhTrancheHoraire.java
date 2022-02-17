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
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_grh_tranche_horaire")
@NamedQueries({
    @NamedQuery(name = "YvsGrhTrancheHoraire.findAll", query = "SELECT y FROM YvsGrhTrancheHoraire y"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findById", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByTitre", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.titre = :titre"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByHeureDebut", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.heureDebut = :heureDebut"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByHeureFin", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.heureFin = :heureFin"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByTypeJournee", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.typeJournee = :typeJournee"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByActif", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByDateSave", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.dateSave = :dateSave")})
public class YvsGrhTrancheHoraire implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "titre")
    private String titre;
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    @Column(name = "type_journee")
    private String typeJournee;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
   
    public YvsGrhTrancheHoraire() {
    }

    public YvsGrhTrancheHoraire(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public String getTypeJournee() {
        return typeJournee;
    }

    public void setTypeJournee(String typeJournee) {
        this.typeJournee = typeJournee;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsGrhTrancheHoraire)) {
            return false;
        }
        YvsGrhTrancheHoraire other = (YvsGrhTrancheHoraire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsGrhTrancheHoraire[ id=" + id + " ]";
    }
    
}
