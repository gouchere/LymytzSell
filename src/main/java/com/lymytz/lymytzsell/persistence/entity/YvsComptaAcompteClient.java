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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_compta_acompte_client")
@NamedQueries({
        @NamedQuery(name = "YvsComptaAcompteClient.findAll", query = "SELECT y FROM YvsComptaAcompteClient y"),
        @NamedQuery(name = "YvsComptaAcompteClient.findById", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByMontant", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.montant = :montant"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByDateAcompte", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.dateAcompte = :dateAcompte"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByNumRefrence", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.numRefrence = :numRefrence"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByCommentaire", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.commentaire = :commentaire"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByStatut", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.statut = :statut"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByDateUpdate", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByDateSave", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByReferenceExterne", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.referenceExterne = :referenceExterne"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByDatePaiement", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.datePaiement = :datePaiement"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByStatutNotif", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.statutNotif = :statutNotif"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByNature", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.nature = :nature"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByRepartirAutomatique", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.repartirAutomatique = :repartirAutomatique"),
        @NamedQuery(name = "YvsComptaAcompteClient.findByComptabilise", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.comptabilise = :comptabilise")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@NoArgsConstructor
public class YvsComptaAcompteClient extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_acompte_client_id_seq", name = "yvs_compta_acompte_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_acompte_client_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_acompte")
    @Temporal(TemporalType.DATE)
    private Date dateAcompte;
    @Column(name = "num_refrence")
    private String numRefrence;
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "date_paiement")
    @Temporal(TemporalType.DATE)
    private Date datePaiement;
    @Column(name = "statut_notif")
    private Character statutNotif;
    @Column(name = "nature")
    private Character nature;
    @Column(name = "repartir_automatique")
    private Boolean repartirAutomatique;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModeReglement model;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private YvsComClient client;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "acompte")
    private List<YvsComptaNotifReglementVente> notifications;

    public YvsComptaAcompteClient(Long id) {
        this.id = id;
    }

    public YvsComptaAcompteClient(YvsComptaAcompteClient y) {
        this(y.id);
        adresseServeur = y.adresseServeur;
        author = y.author;
        caisse = y.caisse;
        client = y.client;
        commentaire = y.commentaire;
        comptabilise = y.comptabilise;
        dateAcompte = y.dateAcompte;
        datePaiement = y.datePaiement;
        dateSave = y.dateSave;
        dateUpdate = y.dateUpdate;
        id = y.id;
        idDistant = y.idDistant;
        model = y.model;
        montant = y.montant;
        nature = y.nature;
        numRefrence = y.numRefrence;
        referenceExterne = y.referenceExterne;
        repartirAutomatique = y.repartirAutomatique;
        statut = y.statut;
        statutNotif = y.statutNotif;
    }


    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComptaAcompteClient[ id=" + id + " ]";
    }

}
