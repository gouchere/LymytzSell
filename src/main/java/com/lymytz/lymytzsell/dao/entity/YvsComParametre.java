/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lymytz.lymytzsell.dao.entity;

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
@Table(name = "yvs_com_parametre")
@NamedQueries({
        @NamedQuery(name = "YvsComParametre.findAll", query = "SELECT y FROM YvsComParametre y"),
        @NamedQuery(name = "YvsComParametre.findById", query = "SELECT y FROM YvsComParametre y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComParametre.findByReglementAuto", query = "SELECT y FROM YvsComParametre y WHERE y.reglementAuto = :reglementAuto"),
        @NamedQuery(name = "YvsComParametre.findByDocumentMouvAchat", query = "SELECT y FROM YvsComParametre y WHERE y.documentMouvAchat = :documentMouvAchat"),
        @NamedQuery(name = "YvsComParametre.findByDocumentMouvVente", query = "SELECT y FROM YvsComParametre y WHERE y.documentMouvVente = :documentMouvVente"),
        @NamedQuery(name = "YvsComParametre.findByModeInventaire", query = "SELECT y FROM YvsComParametre y WHERE y.modeInventaire = :modeInventaire"),
        @NamedQuery(name = "YvsComParametre.findBySeuilFsseur", query = "SELECT y FROM YvsComParametre y WHERE y.seuilFsseur = :seuilFsseur"),
        @NamedQuery(name = "YvsComParametre.findBySeuilClient", query = "SELECT y FROM YvsComParametre y WHERE y.seuilClient = :seuilClient"),
        @NamedQuery(name = "YvsComParametre.findByDureeInactiv", query = "SELECT y FROM YvsComParametre y WHERE y.dureeInactiv = :dureeInactiv"),
        @NamedQuery(name = "YvsComParametre.findByDateUpdate", query = "SELECT y FROM YvsComParametre y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComParametre.findByDateSave", query = "SELECT y FROM YvsComParametre y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsComParametre.findByConverter", query = "SELECT y FROM YvsComParametre y WHERE y.converter = :converter"),
        @NamedQuery(name = "YvsComParametre.findByJourUsine", query = "SELECT y FROM YvsComParametre y WHERE y.jourUsine = :jourUsine"),
        @NamedQuery(name = "YvsComParametre.findByConverterCs", query = "SELECT y FROM YvsComParametre y WHERE y.converterCs = :converterCs"),
        @NamedQuery(name = "YvsComParametre.findByJourDebutMois", query = "SELECT y FROM YvsComParametre y WHERE y.jourDebutMois = :jourDebutMois"),
        @NamedQuery(name = "YvsComParametre.findByFactureOutsideSeuil", query = "SELECT y FROM YvsComParametre y WHERE y.factureOutsideSeuil = :factureOutsideSeuil"),
        @NamedQuery(name = "YvsComParametre.findByDocumentGenererFromEcart", query = "SELECT y FROM YvsComParametre y WHERE y.documentGenererFromEcart = :documentGenererFromEcart")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComParametre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "reglement_auto")
    private Boolean reglementAuto;
    @Column(name = "document_mouv_achat")
    private String documentMouvAchat;
    @Column(name = "document_mouv_vente")
    private String documentMouvVente;
    @Column(name = "mode_inventaire")
    private String modeInventaire;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "seuil_fsseur")
    private Double seuilFsseur;
    @Column(name = "seuil_client")
    private Double seuilClient;
    @Column(name = "duree_inactiv")
    private Integer dureeInactiv;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "converter")
    private Integer converter;
    @Column(name = "jour_usine")
    private Integer jourUsine;
    @Column(name = "converter_cs")
    private Integer converterCs;
    @Column(name = "jour_debut_mois")
    private Integer jourDebutMois;
    @Column(name = "facture_outside_seuil")
    private Boolean factureOutsideSeuil;
    @Column(name = "document_generer_from_ecart")
    private String documentGenererFromEcart;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComParametre(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComParametre[ id=" + id + " ]";
    }

}
