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
import javax.persistence.OneToMany;
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
@Table(name = "yvs_base_point_vente")
@NamedQueries({
        @NamedQuery(name = "YvsBasePointVente.findAll", query = "SELECT y FROM YvsBasePointVente y"),
        @NamedQuery(name = "YvsBasePointVente.findById", query = "SELECT y FROM YvsBasePointVente y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBasePointVente.findByCode", query = "SELECT y FROM YvsBasePointVente y WHERE y.code = :code"),
        @NamedQuery(name = "YvsBasePointVente.findByLibelle", query = "SELECT y FROM YvsBasePointVente y WHERE y.libelle = :libelle"),
        @NamedQuery(name = "YvsBasePointVente.findByAdresse", query = "SELECT y FROM YvsBasePointVente y WHERE y.adresse = :adresse"),
        @NamedQuery(name = "YvsBasePointVente.findByReglementAuto", query = "SELECT y FROM YvsBasePointVente y WHERE y.reglementAuto = :reglementAuto"),
        @NamedQuery(name = "YvsBasePointVente.findByActif", query = "SELECT y FROM YvsBasePointVente y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBasePointVente.findByLivraisonOn", query = "SELECT y FROM YvsBasePointVente y WHERE y.livraisonOn = :livraisonOn"),
        @NamedQuery(name = "YvsBasePointVente.findByDateUpdate", query = "SELECT y FROM YvsBasePointVente y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBasePointVente.findByCommissionFor", query = "SELECT y FROM YvsBasePointVente y WHERE y.commissionFor = :commissionFor"),
        @NamedQuery(name = "YvsBasePointVente.findByDateSave", query = "SELECT y FROM YvsBasePointVente y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBasePointVente.findByPrixMinStrict", query = "SELECT y FROM YvsBasePointVente y WHERE y.prixMinStrict = :prixMinStrict"),
        @NamedQuery(name = "YvsBasePointVente.findByVenteOnline", query = "SELECT y FROM YvsBasePointVente y WHERE y.venteOnline = :venteOnline"),
        @NamedQuery(name = "YvsBasePointVente.findByAcceptClientNoName", query = "SELECT y FROM YvsBasePointVente y WHERE y.acceptClientNoName = :acceptClientNoName"),
        @NamedQuery(name = "YvsBasePointVente.findByValidationReglement", query = "SELECT y FROM YvsBasePointVente y WHERE y.validationReglement = :validationReglement"),
        @NamedQuery(name = "YvsBasePointVente.findByTelephone", query = "SELECT y FROM YvsBasePointVente y WHERE y.telephone = :telephone")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBasePointVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "reglement_auto")
    private Boolean reglementAuto;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "livraison_on")
    private Character livraisonOn;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "commission_for")
    private Character commissionFor;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "prix_min_strict")
    private Boolean prixMinStrict;
    @Column(name = "vente_online")
    private Boolean venteOnline;
    @Column(name = "accept_client_no_name")
    private Boolean acceptClientNoName;
    @Column(name = "validation_reglement")
    private Boolean validationReglement;
    @Column(name = "telephone")
    private String telephone;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne
    private YvsAgences agence;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "point")
    private List<YvsComCommercialPoint> commerciaux;

    public YvsBasePointVente(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBasePointVente[ id=" + id + " ]";
    }

}
