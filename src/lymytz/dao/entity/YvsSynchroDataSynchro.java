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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "yvs_synchro_data_synchro")
@NamedQueries({
    @NamedQuery(name = "YvsSynchroDataSynchro.findAll", query = "SELECT y FROM YvsSynchroDataSynchro y"),
    @NamedQuery(name = "YvsSynchroDataSynchro.findById", query = "SELECT y FROM YvsSynchroDataSynchro y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSynchroDataSynchro.findOne", query = "SELECT y FROM YvsSynchroDataSynchro y WHERE y.idListen = :listen AND y.idDistant = :distant AND y.serveur = :serveur"),
    @NamedQuery(name = "YvsSynchroDataSynchro.findByDateSave", query = "SELECT y FROM YvsSynchroDataSynchro y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsSynchroDataSynchro.findIdDistant", query = "SELECT y.idListen.idSource FROM YvsSynchroDataSynchro y WHERE y.idListen.nameTable=:table AND y.idDistant=:idDistant"),
    @NamedQuery(name = "YvsSynchroDataSynchro.findByIdDistant", query = "SELECT y FROM YvsSynchroDataSynchro y WHERE y.idDistant = :idDistant")})
public class YvsSynchroDataSynchro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_synchro_data_synchro_id_seq", name = "yvs_synchro_data_synchro_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_synchro_data_synchro_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "id_distant")
    private Long idDistant;
    @JoinColumn(name = "id_listen", referencedColumnName = "id")
    @ManyToOne
    private YvsSynchroListenTable idListen;
    @JoinColumn(name = "serveur", referencedColumnName = "id")
    @ManyToOne
    private YvsSynchroServeurs serveur;

    public YvsSynchroDataSynchro() {
    }

    public YvsSynchroDataSynchro(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(Long idDistant) {
        this.idDistant = idDistant;
    }

    public YvsSynchroListenTable getIdListen() {
        return idListen;
    }

    public void setIdListen(YvsSynchroListenTable idListen) {
        this.idListen = idListen;
    }

    public YvsSynchroServeurs getServeur() {
        return serveur;
    }

    public void setServeur(YvsSynchroServeurs serveur) {
        this.serveur = serveur;
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
        if (!(object instanceof YvsSynchroDataSynchro)) {
            return false;
        }
        YvsSynchroDataSynchro other = (YvsSynchroDataSynchro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsSynchroDataSynchro[ id=" + id + " ]";
    }

}
