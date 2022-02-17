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
@Table(name = "yvs_compta_notif_reglement_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComptaNotifReglementVente.findAll", query = "SELECT y FROM YvsComptaNotifReglementVente y"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findById", query = "SELECT y FROM YvsComptaNotifReglementVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findByDateUpdate", query = "SELECT y FROM YvsComptaNotifReglementVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findByDateSave", query = "SELECT y FROM YvsComptaNotifReglementVente y WHERE y.dateSave = :dateSave")})
public class YvsComptaNotifReglementVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "acompte", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaAcompteClient acompte;
    @JoinColumn(name = "piece_vente", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaCaissePieceVente pieceVente;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComptaNotifReglementVente() {
    }

    public YvsComptaNotifReglementVente(Long id) {
        this.id = id;
    }

    public YvsComptaNotifReglementVente(YvsComptaNotifReglementVente y) {
        this(y.id);
        acompte = y.acompte;
        adresseServeur = y.adresseServeur;
        author = y.author;
        dateSave = y.dateSave;
        dateUpdate = y.dateUpdate;
        idDistant = y.idDistant;
        pieceVente = y.pieceVente;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public YvsComptaAcompteClient getAcompte() {
        return acompte;
    }

    public void setAcompte(YvsComptaAcompteClient acompte) {
        this.acompte = acompte;
    }

    public YvsComptaCaissePieceVente getPieceVente() {
        return pieceVente;
    }

    public void setPieceVente(YvsComptaCaissePieceVente pieceVente) {
        this.pieceVente = pieceVente;
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
        if (!(object instanceof YvsComptaNotifReglementVente)) {
            return false;
        }
        YvsComptaNotifReglementVente other = (YvsComptaNotifReglementVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComptaNotifReglementVente[ id=" + id + " ]";
    }

}
