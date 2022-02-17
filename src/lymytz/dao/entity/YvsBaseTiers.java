/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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

/**
 *
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

    public YvsBaseTiers() {
    }

    public YvsBaseTiers(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Boolean getClient() {
        return client;
    }

    public void setClient(Boolean client) {
        this.client = client;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public String getCodeTiers() {
        return codeTiers;
    }

    public void setCodeTiers(String codeTiers) {
        this.codeTiers = codeTiers;
    }

    public String getCompte() {
        return compte;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Boolean fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPointDeVente() {
        return pointDeVente;
    }

    public void setPointDeVente(String pointDeVente) {
        this.pointDeVente = pointDeVente;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Boolean getRepresentant() {
        return representant;
    }

    public void setRepresentant(Boolean representant) {
        this.representant = representant;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Boolean getAlwaysVisible() {
        return alwaysVisible;
    }

    public void setAlwaysVisible(Boolean alwaysVisible) {
        this.alwaysVisible = alwaysVisible;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Boolean getStSociete() {
        return stSociete;
    }

    public void setStSociete(Boolean stSociete) {
        this.stSociete = stSociete;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Boolean getEmploye() {
        return employe;
    }

    public void setEmploye(Boolean employe) {
        this.employe = employe;
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

    public Boolean getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Boolean personnel) {
        this.personnel = personnel;
    }

    public YvsDictionnaire getSecteur() {
        return secteur;
    }

    public void setSecteur(YvsDictionnaire secteur) {
        this.secteur = secteur;
    }

    public YvsDictionnaire getVille() {
        return ville;
    }

    public void setVille(YvsDictionnaire ville) {
        this.ville = ville;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public List<YvsComClient> getClients() {
        return clients;
    }

    public void setClients(List<YvsComClient> clients) {
        this.clients = clients;
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
        if (!(object instanceof YvsBaseTiers)) {
            return false;
        }
        YvsBaseTiers other = (YvsBaseTiers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseTiers[ id=" + id + " ]";
    }

}
