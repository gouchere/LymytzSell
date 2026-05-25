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
import java.io.Serializable;
import java.util.Date;

/**
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_compta_notif_reglement_vente")
@NamedQueries({
        @NamedQuery(name = "YvsComptaNotifReglementVente.findAll", query = "SELECT y FROM YvsComptaNotifReglementVente y"),
        @NamedQuery(name = "YvsComptaNotifReglementVente.findById", query = "SELECT y FROM YvsComptaNotifReglementVente y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComptaNotifReglementVente.findByDateUpdate", query = "SELECT y FROM YvsComptaNotifReglementVente y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComptaNotifReglementVente.findByDateSave", query = "SELECT y FROM YvsComptaNotifReglementVente y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
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
    public String toString() {
        return "lymytz.dao.entity.YvsComptaNotifReglementVente[ id=" + id + " ]";
    }

}
