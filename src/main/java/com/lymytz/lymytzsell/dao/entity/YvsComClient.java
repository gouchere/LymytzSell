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
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_client")
@NamedQueries({
        @NamedQuery(name = "YvsComClient.findAll", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe ORDER BY y.tiers.nom, y.tiers.prenom"),
        @NamedQuery(name = "YvsComClient.findById", query = "SELECT y FROM YvsComClient y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComClient.findByDefaut", query = "SELECT y FROM YvsComClient y WHERE y.defaut = :defaut"),
        @NamedQuery(name = "YvsComClient.findByCodeClient", query = "SELECT y FROM YvsComClient y WHERE y.codeClient = :codeClient"),
        @NamedQuery(name = "YvsComClient.findByActif", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe AND y.actif = true ORDER BY y.nom, y.prenom"),
        @NamedQuery(name = "YvsComClient.findByNom", query = "SELECT y FROM YvsComClient y WHERE y.nom = :nom"),
        @NamedQuery(name = "YvsComClient.findByPrenom", query = "SELECT y FROM YvsComClient y WHERE y.prenom = :prenom"),
        @NamedQuery(name = "YvsComClient.findBySuiviComptable", query = "SELECT y FROM YvsComClient y WHERE y.suiviComptable = :suiviComptable"),
        @NamedQuery(name = "YvsComClient.findBySeuilSolde", query = "SELECT y FROM YvsComClient y WHERE y.seuilSolde = :seuilSolde"),
        @NamedQuery(name = "YvsComClient.findByDateCreation", query = "SELECT y FROM YvsComClient y WHERE y.dateCreation = :dateCreation"),
        @NamedQuery(name = "YvsComClient.findByDateUpdate", query = "SELECT y FROM YvsComClient y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComClient.findByConfirmer", query = "SELECT y FROM YvsComClient y WHERE y.confirmer = :confirmer")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComClient extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "code_client")
    private String codeClient;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "suivi_comptable")
    private Boolean suiviComptable;
    @Column(name = "seuil_solde")
    private Double seuilSolde;
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "confirmer")
    private Boolean confirmer;
    @JoinColumn(name = "categorie_comptable", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCategorieComptable categorieComptable;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModelReglement model;
    @JoinColumn(name = "ligne", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePointLivraison ligne;
    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseTiers tiers;
    @JoinColumn(name = "plan_ristourne", referencedColumnName = "id")
    @ManyToOne
    private YvsComPlanRistourne planRistourne;
    @JoinColumn(name = "create_by", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers createBy;
    @Transient
    private String nom_prenom;

    public YvsComClient(Long id) {
        this.id = id;
    }

    public YvsComClient(YvsComClient c) {
        this.id = c.id;
    }

    public String getNom_prenom() {
        nom_prenom = "";
        if (!(getNom() == null || getNom().trim().isEmpty())) {
            nom_prenom = getNom();
        }
        if (!(getPrenom() == null || getPrenom().trim().isEmpty())) {
            if (nom_prenom == null || nom_prenom.trim().isEmpty()) {
                nom_prenom = getPrenom();
            } else {
                nom_prenom += " " + getPrenom();
            }
        }
        return nom_prenom;
    }

    @Override
    public YvsUsersAgence getAuthor() {
        return null;
    }

    public String getTextClient() {
        return getNom_prenom().concat("->".concat(getCodeClient()));
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComClient_1[ id=" + id + " ]";
    }
}
