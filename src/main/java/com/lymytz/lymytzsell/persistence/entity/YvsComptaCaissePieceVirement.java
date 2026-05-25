/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
import java.io.Serializable;
import java.util.Date;

/**
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
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
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

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComptaCaissePieceVirement[ id=" + id + " ]";
    }

}
