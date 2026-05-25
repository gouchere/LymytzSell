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
@Table(name = "yvs_compta_parametre")
@NamedQueries({
        @NamedQuery(name = "YvsComptaParametre.findAll", query = "SELECT y FROM YvsComptaParametre y"),
        @NamedQuery(name = "YvsComptaParametre.findById", query = "SELECT y FROM YvsComptaParametre y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComptaParametre.findByTailleCompte", query = "SELECT y FROM YvsComptaParametre y WHERE y.tailleCompte = :tailleCompte"),
        @NamedQuery(name = "YvsComptaParametre.findByDecimalArrondi", query = "SELECT y FROM YvsComptaParametre y WHERE y.decimalArrondi = :decimalArrondi"),
        @NamedQuery(name = "YvsComptaParametre.findByModeArrondi", query = "SELECT y FROM YvsComptaParametre y WHERE y.modeArrondi = :modeArrondi"),
        @NamedQuery(name = "YvsComptaParametre.findByMultipleArrondi", query = "SELECT y FROM YvsComptaParametre y WHERE y.multipleArrondi = :multipleArrondi"),
        @NamedQuery(name = "YvsComptaParametre.findByValeurArrondi", query = "SELECT y FROM YvsComptaParametre y WHERE y.valeurArrondi = :valeurArrondi"),
        @NamedQuery(name = "YvsComptaParametre.findByDateUpdate", query = "SELECT y FROM YvsComptaParametre y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComptaParametre.findByDateSave", query = "SELECT y FROM YvsComptaParametre y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsComptaParametre.findByMajComptaAutoDivers", query = "SELECT y FROM YvsComptaParametre y WHERE y.majComptaAutoDivers = :majComptaAutoDivers"),
        @NamedQuery(name = "YvsComptaParametre.findByMajComptaStatutDivers", query = "SELECT y FROM YvsComptaParametre y WHERE y.majComptaStatutDivers = :majComptaStatutDivers"),
        @NamedQuery(name = "YvsComptaParametre.findByConverter", query = "SELECT y FROM YvsComptaParametre y WHERE y.converter = :converter"),
        @NamedQuery(name = "YvsComptaParametre.findByMontantSeuilDepenseOd", query = "SELECT y FROM YvsComptaParametre y WHERE y.montantSeuilDepenseOd = :montantSeuilDepenseOd"),
        @NamedQuery(name = "YvsComptaParametre.findByValeurLimiteArrondi", query = "SELECT y FROM YvsComptaParametre y WHERE y.valeurLimiteArrondi = :valeurLimiteArrondi"),
        @NamedQuery(name = "YvsComptaParametre.findByMontantSeuilRecetteOd", query = "SELECT y FROM YvsComptaParametre y WHERE y.montantSeuilRecetteOd = :montantSeuilRecetteOd"),
        @NamedQuery(name = "YvsComptaParametre.findByEcartDaySoldeClient", query = "SELECT y FROM YvsComptaParametre y WHERE y.ecartDaySoldeClient = :ecartDaySoldeClient"),
        @NamedQuery(name = "YvsComptaParametre.findByNombreLigneSoldeClient", query = "SELECT y FROM YvsComptaParametre y WHERE y.nombreLigneSoldeClient = :nombreLigneSoldeClient"),
        @NamedQuery(name = "YvsComptaParametre.findByJourAnterieurCancel", query = "SELECT y FROM YvsComptaParametre y WHERE y.jourAnterieurCancel = :jourAnterieurCancel"),
        @NamedQuery(name = "YvsComptaParametre.findByJourAnterieur", query = "SELECT y FROM YvsComptaParametre y WHERE y.jourAnterieur = :jourAnterieur"),
        @NamedQuery(name = "YvsComptaParametre.findByExecuteTrigger", query = "SELECT y FROM YvsComptaParametre y WHERE y.executeTrigger = :executeTrigger")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComptaParametre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "taille_compte")
    private Integer tailleCompte;
    @Column(name = "decimal_arrondi")
    private Boolean decimalArrondi;
    @Column(name = "mode_arrondi")
    private String modeArrondi;
    @Column(name = "multiple_arrondi")
    private Double multipleArrondi;
    @Column(name = "valeur_arrondi")
    private Integer valeurArrondi;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "maj_compta_auto_divers")
    private Boolean majComptaAutoDivers;
    @Column(name = "maj_compta_statut_divers")
    private Character majComptaStatutDivers;
    @Column(name = "converter")
    private Integer converter;
    @Column(name = "montant_seuil_depense_od")
    private Double montantSeuilDepenseOd;
    @Column(name = "valeur_limite_arrondi")
    private Double valeurLimiteArrondi;
    @Column(name = "montant_seuil_recette_od")
    private Double montantSeuilRecetteOd;
    @Column(name = "ecart_day_solde_client")
    private Integer ecartDaySoldeClient;
    @Column(name = "nombre_ligne_solde_client")
    private Integer nombreLigneSoldeClient;
    @Column(name = "jour_anterieur_cancel")
    private Integer jourAnterieurCancel;
    @Column(name = "jour_anterieur")
    private Integer jourAnterieur;
    @Column(name = "execute_trigger")
    private String executeTrigger;

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComptaParametre[ id=" + id + " ]";
    }

}
