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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_vente")
@NamedQueries({
        @NamedQuery(name = "YvsComptaCaissePieceVente.findAll", query = "SELECT y FROM YvsComptaCaissePieceVente y"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findById", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findDocById", query = "SELECT y.vente FROM YvsComptaCaissePieceVente y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findMontantRecuByFacture", query = "SELECT y.montantRecu FROM YvsComptaCaissePieceVente y WHERE y.vente = :vente"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByDocVente", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente = :docVente"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByMontant", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.montant = :montant"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByFacture", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByFactureStatutS", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture AND y.statutPiece = :statut AND (COALESCE(y.mouvement, 'R')) = 'R'"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByFactureStatutSDiff", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.vente = :facture AND y.statutPiece != :statut AND (COALESCE(y.mouvement, 'R')) = 'R'"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByStatutPiece", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.statutPiece = :statutPiece"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByDatePiece", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.datePiece = :datePiece"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByDatePaiement", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.datePaiement = :datePaiement"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByNumeroPiece", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.vente.enteteDoc.agence.societe=:societe AND y.numeroPiece LIKE :numeroPiece ORDER BY y.numeroPiece DESC"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByNote", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.note = :note"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByDatePaimentPrevu", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.datePaimentPrevu = :datePaimentPrevu"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByReferenceExterne", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.referenceExterne = :referenceExterne"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByDateValide", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.dateValide = :dateValide"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByDateUpdate", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByDateSave", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByMontantRecu", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.montantRecu = :montantRecu"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByMouvement", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.mouvement = :mouvement"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByComptabilise", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.comptabilise = :comptabilise"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findSumByCaissier", query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceVente y WHERE y.caissier = :caissier AND y.statutPiece = 'P' AND COALESCE(y.mouvement, 'R') = 'R' AND y.datePaiement=:date AND y.vente.typeDoc='BCV'"),
        @NamedQuery(name = "YvsComptaCaissePieceVente.findByVerouille", query = "SELECT y FROM YvsComptaCaissePieceVente y WHERE y.verouille = :verouille")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@NoArgsConstructor
public class YvsComptaCaissePieceVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_piece_vente_id_seq", name = "yvs_compta_caisse_piece_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_piece_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Temporal(TemporalType.DATE)
    private Date datePaiement;
    @Column(name = "note")
    private String note;
    @Column(name = "date_paiment_prevu")
    @Temporal(TemporalType.DATE)
    private Date datePaimentPrevu;
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "date_valide")
    @Temporal(TemporalType.DATE)
    private Date dateValide;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "montant_recu")
    private Double montantRecu;
    @Column(name = "mouvement")
    private Character mouvement;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "verouille")
    private Boolean verouille;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModeReglement model;
    @JoinColumn(name = "vente", referencedColumnName = "id")
    @ManyToOne
    private YvsComDocVentes vente;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaCaissePieceVente parent;
    @JoinColumn(name = "valide_by", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers valideBy;
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers caissier;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComptaCaissePieceVente(Long id) {
        this.id = id;
    }

    public YvsComptaCaissePieceVente(YvsComptaCaissePieceVente p) {
        this(p.id);
        adresseServeur = p.adresseServeur;
        author = p.author;
        caisse = p.caisse;
        caissier = p.caissier;
        comptabilise = p.comptabilise;
        datePaiement = p.datePaiement;
        datePaimentPrevu = p.datePaimentPrevu;
        datePiece = p.datePiece;
        dateSave = p.dateSave;
        dateUpdate = p.dateUpdate;
        dateValide = p.dateValide;
        idDistant = p.idDistant;
        model = p.model;
        montant = p.montant;
        montantRecu = p.montantRecu;
        mouvement = p.mouvement;
        note = p.note;
        numeroPiece = p.numeroPiece;
        parent = p.parent;
        referenceExterne = p.referenceExterne;
        statutPiece = p.statutPiece;
        valideBy = p.valideBy;
        vente = p.vente;
        verouille = p.verouille;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComptaCaissePieceVente[ id=" + id + " ]";
    }

}
