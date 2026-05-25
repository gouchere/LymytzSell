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
@Table(name = "yvs_base_depots")
@NamedQueries({
        @NamedQuery(name = "YvsBaseDepots.findAll", query = "SELECT y FROM YvsBaseDepots y"),
        @NamedQuery(name = "YvsBaseDepots.findById", query = "SELECT y FROM YvsBaseDepots y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseDepots.findByAbbreviation", query = "SELECT y FROM YvsBaseDepots y WHERE y.abbreviation = :abbreviation"),
        @NamedQuery(name = "YvsBaseDepots.findByAdresse", query = "SELECT y FROM YvsBaseDepots y WHERE y.adresse = :adresse"),
        @NamedQuery(name = "YvsBaseDepots.findByCode", query = "SELECT y FROM YvsBaseDepots y WHERE y.code = :code"),
        @NamedQuery(name = "YvsBaseDepots.findByControlStock", query = "SELECT y FROM YvsBaseDepots y WHERE y.controlStock = :controlStock"),
        @NamedQuery(name = "YvsBaseDepots.findByCrenau", query = "SELECT y FROM YvsBaseDepots y WHERE y.crenau = :crenau"),
        @NamedQuery(name = "YvsBaseDepots.findByDesignation", query = "SELECT y FROM YvsBaseDepots y WHERE y.designation = :designation"),
        @NamedQuery(name = "YvsBaseDepots.findByOpAchat", query = "SELECT y FROM YvsBaseDepots y WHERE y.opAchat = :opAchat"),
        @NamedQuery(name = "YvsBaseDepots.findByOpProduction", query = "SELECT y FROM YvsBaseDepots y WHERE y.opProduction = :opProduction"),
        @NamedQuery(name = "YvsBaseDepots.findByOpTransit", query = "SELECT y FROM YvsBaseDepots y WHERE y.opTransit = :opTransit"),
        @NamedQuery(name = "YvsBaseDepots.findByOpVente", query = "SELECT y FROM YvsBaseDepots y WHERE y.opVente = :opVente"),
        @NamedQuery(name = "YvsBaseDepots.findByActif", query = "SELECT y FROM YvsBaseDepots y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBaseDepots.findByDescription", query = "SELECT y FROM YvsBaseDepots y WHERE y.description = :description"),
        @NamedQuery(name = "YvsBaseDepots.findByOpTechnique", query = "SELECT y FROM YvsBaseDepots y WHERE y.opTechnique = :opTechnique"),
        @NamedQuery(name = "YvsBaseDepots.findByOpRetour", query = "SELECT y FROM YvsBaseDepots y WHERE y.opRetour = :opRetour"),
        @NamedQuery(name = "YvsBaseDepots.findByOpReserv", query = "SELECT y FROM YvsBaseDepots y WHERE y.opReserv = :opReserv"),
        @NamedQuery(name = "YvsBaseDepots.findByDateUpdate", query = "SELECT y FROM YvsBaseDepots y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseDepots.findByDateSave", query = "SELECT y FROM YvsBaseDepots y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseDepots.findByPrincipal", query = "SELECT y FROM YvsBaseDepots y WHERE y.principal = :principal"),
        @NamedQuery(name = "YvsBaseDepots.findByTypePf", query = "SELECT y FROM YvsBaseDepots y WHERE y.typePf = :typePf"),
        @NamedQuery(name = "YvsBaseDepots.findByTypeNe", query = "SELECT y FROM YvsBaseDepots y WHERE y.typeNe = :typeNe"),
        @NamedQuery(name = "YvsBaseDepots.findByTypePsf", query = "SELECT y FROM YvsBaseDepots y WHERE y.typePsf = :typePsf"),
        @NamedQuery(name = "YvsBaseDepots.findByTypeMp", query = "SELECT y FROM YvsBaseDepots y WHERE y.typeMp = :typeMp"),
        @NamedQuery(name = "YvsBaseDepots.findByVerifyAppro", query = "SELECT y FROM YvsBaseDepots y WHERE y.verifyAppro = :verifyAppro")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseDepots implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "abbreviation")
    private String abbreviation;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "code")
    private String code;
    @Column(name = "control_stock")
    private Boolean controlStock;
    @Column(name = "crenau")
    private Boolean crenau;
    @Column(name = "designation")
    private String designation;
    @Column(name = "op_achat")
    private Boolean opAchat;
    @Column(name = "op_production")
    private Boolean opProduction;
    @Column(name = "op_transit")
    private Boolean opTransit;
    @Column(name = "op_vente")
    private Boolean opVente;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "description")
    private String description;
    @Column(name = "op_technique")
    private Boolean opTechnique;
    @Column(name = "op_retour")
    private Boolean opRetour;
    @Column(name = "op_reserv")
    private Boolean opReserv;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "principal")
    private Boolean principal;
    @Column(name = "type_pf")
    private Boolean typePf;
    @Column(name = "type_ne")
    private Boolean typeNe;
    @Column(name = "type_psf")
    private Boolean typePsf;
    @Column(name = "type_mp")
    private Boolean typeMp;
    @Column(name = "verify_appro")
    private Boolean verifyAppro;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne
    private YvsAgences agence;

    public YvsBaseDepots(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseDepots[ id=" + id + " ]";
    }
}
