/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lymytz.dao.entity;

import java.io.Serializable;
import java.util.Date;
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

/**
 *
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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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

    public YvsComptaParametre() {
    }

    public YvsComptaParametre(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTailleCompte() {
        return tailleCompte;
    }

    public void setTailleCompte(Integer tailleCompte) {
        this.tailleCompte = tailleCompte;
    }

    public Boolean getDecimalArrondi() {
        return decimalArrondi;
    }

    public void setDecimalArrondi(Boolean decimalArrondi) {
        this.decimalArrondi = decimalArrondi;
    }

    public String getModeArrondi() {
        return modeArrondi;
    }

    public void setModeArrondi(String modeArrondi) {
        this.modeArrondi = modeArrondi;
    }

    public Double getMultipleArrondi() {
        return multipleArrondi;
    }

    public void setMultipleArrondi(Double multipleArrondi) {
        this.multipleArrondi = multipleArrondi;
    }

    public Integer getValeurArrondi() {
        return valeurArrondi;
    }

    public void setValeurArrondi(Integer valeurArrondi) {
        this.valeurArrondi = valeurArrondi;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Boolean getMajComptaAutoDivers() {
        return majComptaAutoDivers;
    }

    public void setMajComptaAutoDivers(Boolean majComptaAutoDivers) {
        this.majComptaAutoDivers = majComptaAutoDivers;
    }

    public Character getMajComptaStatutDivers() {
        return majComptaStatutDivers;
    }

    public void setMajComptaStatutDivers(Character majComptaStatutDivers) {
        this.majComptaStatutDivers = majComptaStatutDivers;
    }

    public Integer getConverter() {
        return converter;
    }

    public void setConverter(Integer converter) {
        this.converter = converter;
    }

    public Double getMontantSeuilDepenseOd() {
        return montantSeuilDepenseOd;
    }

    public void setMontantSeuilDepenseOd(Double montantSeuilDepenseOd) {
        this.montantSeuilDepenseOd = montantSeuilDepenseOd;
    }

    public Double getValeurLimiteArrondi() {
        return valeurLimiteArrondi;
    }

    public void setValeurLimiteArrondi(Double valeurLimiteArrondi) {
        this.valeurLimiteArrondi = valeurLimiteArrondi;
    }

    public Double getMontantSeuilRecetteOd() {
        return montantSeuilRecetteOd;
    }

    public void setMontantSeuilRecetteOd(Double montantSeuilRecetteOd) {
        this.montantSeuilRecetteOd = montantSeuilRecetteOd;
    }

    public Integer getEcartDaySoldeClient() {
        return ecartDaySoldeClient;
    }

    public void setEcartDaySoldeClient(Integer ecartDaySoldeClient) {
        this.ecartDaySoldeClient = ecartDaySoldeClient;
    }

    public Integer getNombreLigneSoldeClient() {
        return nombreLigneSoldeClient;
    }

    public void setNombreLigneSoldeClient(Integer nombreLigneSoldeClient) {
        this.nombreLigneSoldeClient = nombreLigneSoldeClient;
    }

    public Integer getJourAnterieurCancel() {
        return jourAnterieurCancel;
    }

    public void setJourAnterieurCancel(Integer jourAnterieurCancel) {
        this.jourAnterieurCancel = jourAnterieurCancel;
    }

    public Integer getJourAnterieur() {
        return jourAnterieur;
    }

    public void setJourAnterieur(Integer jourAnterieur) {
        this.jourAnterieur = jourAnterieur;
    }

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComptaParametre)) {
            return false;
        }
        YvsComptaParametre other = (YvsComptaParametre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComptaParametre[ id=" + id + " ]";
    }
    
}
