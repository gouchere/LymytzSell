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
@Table(name = "yvs_base_tiers")
@NamedQueries({
        @NamedQuery(name = "YvsBaseTiers.findAll", query = "SELECT y FROM YvsBaseTiers y"),
        @NamedQuery(name = "YvsBaseTiers.findById", query = "SELECT y FROM YvsBaseTiers y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseTiers.findByAdresse", query = "SELECT y FROM YvsBaseTiers y WHERE y.adresse = :adresse"),
        @NamedQuery(name = "YvsBaseTiers.findByBp", query = "SELECT y FROM YvsBaseTiers y WHERE y.bp = :bp"),
        @NamedQuery(name = "YvsBaseTiers.findByCivilite", query = "SELECT y FROM YvsBaseTiers y WHERE y.civilite = :civilite"),
        @NamedQuery(name = "YvsBaseTiers.findByClasse", query = "SELECT y FROM YvsBaseTiers y WHERE y.classe = :classe"),
        @NamedQuery(name = "YvsBaseTiers.findByClient", query = "SELECT y FROM YvsBaseTiers y WHERE y.client = :client"),
        @NamedQuery(name = "YvsBaseTiers.findByCodeBarre", query = "SELECT y FROM YvsBaseTiers y WHERE y.codeBarre = :codeBarre"),
        @NamedQuery(name = "YvsBaseTiers.findByCodeTiers", query = "SELECT y FROM YvsBaseTiers y WHERE y.codeTiers = :codeTiers"),
        @NamedQuery(name = "YvsBaseTiers.findByCompte", query = "SELECT y FROM YvsBaseTiers y WHERE y.compte = :compte"),
        @NamedQuery(name = "YvsBaseTiers.findByEmail", query = "SELECT y FROM YvsBaseTiers y WHERE y.email = :email"),
        @NamedQuery(name = "YvsBaseTiers.findByFournisseur", query = "SELECT y FROM YvsBaseTiers y WHERE y.fournisseur = :fournisseur"),
        @NamedQuery(name = "YvsBaseTiers.findByLogo", query = "SELECT y FROM YvsBaseTiers y WHERE y.logo = :logo"),
        @NamedQuery(name = "YvsBaseTiers.findByNom", query = "SELECT y FROM YvsBaseTiers y WHERE y.nom = :nom"),
        @NamedQuery(name = "YvsBaseTiers.findByPointDeVente", query = "SELECT y FROM YvsBaseTiers y WHERE y.pointDeVente = :pointDeVente"),
        @NamedQuery(name = "YvsBaseTiers.findByPrenom", query = "SELECT y FROM YvsBaseTiers y WHERE y.prenom = :prenom"),
        @NamedQuery(name = "YvsBaseTiers.findByRepresentant", query = "SELECT y FROM YvsBaseTiers y WHERE y.representant = :representant"),
        @NamedQuery(name = "YvsBaseTiers.findByTel", query = "SELECT y FROM YvsBaseTiers y WHERE y.tel = :tel"),
        @NamedQuery(name = "YvsBaseTiers.findByCodePostal", query = "SELECT y FROM YvsBaseTiers y WHERE y.codePostal = :codePostal"),
        @NamedQuery(name = "YvsBaseTiers.findByStatut", query = "SELECT y FROM YvsBaseTiers y WHERE y.statut = :statut"),
        @NamedQuery(name = "YvsBaseTiers.findByAlwaysVisible", query = "SELECT y FROM YvsBaseTiers y WHERE y.alwaysVisible = :alwaysVisible"),
        @NamedQuery(name = "YvsBaseTiers.findByActif", query = "SELECT y FROM YvsBaseTiers y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBaseTiers.findBySite", query = "SELECT y FROM YvsBaseTiers y WHERE y.site = :site"),
        @NamedQuery(name = "YvsBaseTiers.findByStSociete", query = "SELECT y FROM YvsBaseTiers y WHERE y.stSociete = :stSociete"),
        @NamedQuery(name = "YvsBaseTiers.findByResponsable", query = "SELECT y FROM YvsBaseTiers y WHERE y.responsable = :responsable"),
        @NamedQuery(name = "YvsBaseTiers.findByEmploye", query = "SELECT y FROM YvsBaseTiers y WHERE y.employe = :employe"),
        @NamedQuery(name = "YvsBaseTiers.findByDateUpdate", query = "SELECT y FROM YvsBaseTiers y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseTiers.findByDateSave", query = "SELECT y FROM YvsBaseTiers y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseTiers.findByPersonnel", query = "SELECT y FROM YvsBaseTiers y WHERE y.personnel = :personnel")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseTiers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "bp")
    private String bp;
    @Column(name = "civilite")
    private String civilite;
    @Column(name = "classe")
    private String classe;
    @Column(name = "client")
    private Boolean client;
    @Column(name = "code_barre")
    private String codeBarre;
    @Column(name = "code_tiers")
    private String codeTiers;
    @Column(name = "compte")
    private String compte;
    @Column(name = "email")
    private String email;
    @Column(name = "fournisseur")
    private Boolean fournisseur;
    @Column(name = "logo")
    private String logo;
    @Column(name = "nom")
    private String nom;
    @Column(name = "point_de_vente")
    private String pointDeVente;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "representant")
    private Boolean representant;
    @Column(name = "tel")
    private String tel;
    @Column(name = "code_postal")
    private String codePostal;
    @Column(name = "statut")
    private String statut;
    @Column(name = "always_visible")
    private Boolean alwaysVisible;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "site")
    private String site;
    @Column(name = "st_societe")
    private Boolean stSociete;
    @Column(name = "responsable")
    private String responsable;
    @Column(name = "employe")
    private Boolean employe;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "personnel")
    private Boolean personnel;
    @JoinColumn(name = "secteur", referencedColumnName = "id")
    @ManyToOne
    private YvsDictionnaire secteur;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne
    private YvsDictionnaire ville;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @OneToMany(mappedBy = "tiers")
    private List<YvsComClient> clients;

    public YvsBaseTiers(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseTiers[ id=" + id + " ]";
    }

}
