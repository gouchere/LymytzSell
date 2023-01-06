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
import javax.persistence.Transient;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_virement")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findAll", query = "SELECT y FROM YvsComptaCaissePieceVirement y"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findById", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByNumeroPiece", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.numeroPiece = :numeroPiece"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByMontant", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByStatutPiece", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.statutPiece = :statutPiece"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByDatePiece", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.datePiece = :datePiece"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByDatePaiement", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.datePaiement = :datePaiement"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByNote", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.note = :note"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByDatePaimentPrevu", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.datePaimentPrevu = :datePaimentPrevu"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByDateUpdate", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaCaissePieceVirement.findByDateSave", query = "SELECT y FROM YvsComptaCaissePieceVirement y WHERE y.dateSave = :dateSave")})
public class YvsComptaCaissePieceVirement extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero_piece")
    private String numeroPiece;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "statut_piece")
    private Character statutPiece;
    @Column(name = "date_piece")
    @Temporal(TemporalType.DATE)
    private Date datePiece;
    @Column(name = "date_paiement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaiement;
    @Column(name = "note")
    private String note;
    @Column(name = "date_paiment_prevu")
    @Temporal(TemporalType.DATE)
    private Date datePaimentPrevu;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModeReglement model;
    @JoinColumn(name = "source", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse source;
    @JoinColumn(name = "cible", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse cible;
    @JoinColumn(name = "caissier_cible", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers caissierCible;
    @JoinColumn(name = "caissier_source", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers caissierSource;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    @Transient
    private YvsComEnteteDocVente header;

    public YvsComptaCaissePieceVirement() {
    }

    public YvsComptaCaissePieceVirement(Long id) {
        this.id = id;
    }

    public YvsComptaCaissePieceVirement(YvsComptaCaissePieceVirement entity) {
        this(entity.getId());
        this.author = entity.author;
        this.caissierCible = entity.caissierCible;
        this.caissierSource = entity.caissierSource;
        this.cible = entity.cible;
        this.datePaiement = entity.datePaiement;
        this.datePaimentPrevu = entity.datePaimentPrevu;
        this.datePiece = entity.datePiece;
        this.dateSave = entity.dateSave;
        this.dateUpdate = entity.dateUpdate;
        this.id = entity.id;
        this.model = entity.model;
        this.montant = entity.montant;
        this.note = entity.note;
        this.numeroPiece = entity.numeroPiece;
        this.source = entity.source;
        this.statutPiece = entity.statutPiece;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPiece() {
        return numeroPiece;
    }

    public void setNumeroPiece(String numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Character getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
    }

    public Date getDatePiece() {
        return datePiece;
    }

    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDatePaimentPrevu() {
        return datePaimentPrevu;
    }

    public void setDatePaimentPrevu(Date datePaimentPrevu) {
        this.datePaimentPrevu = datePaimentPrevu;
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

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public YvsUsers getCaissierCible() {
        return caissierCible;
    }

    public void setCaissierCible(YvsUsers caissierCible) {
        this.caissierCible = caissierCible;
    }

    public YvsUsers getCaissierSource() {
        return caissierSource;
    }

    public void setCaissierSource(YvsUsers caissierSource) {
        this.caissierSource = caissierSource;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseCaisse getCible() {
        return cible;
    }

    public void setCible(YvsBaseCaisse cible) {
        this.cible = cible;
    }

    public YvsBaseCaisse getSource() {
        return source;
    }

    public void setSource(YvsBaseCaisse source) {
        this.source = source;
    }

    public YvsComEnteteDocVente getHeader() {
        return header;
    }

    public void setHeader(YvsComEnteteDocVente header) {
        this.header = header;
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
        if (!(object instanceof YvsComptaCaissePieceVirement)) {
            return false;
        }
        YvsComptaCaissePieceVirement other = (YvsComptaCaissePieceVirement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComptaCaissePieceVirement[ id=" + id + " ]";
    }

}
