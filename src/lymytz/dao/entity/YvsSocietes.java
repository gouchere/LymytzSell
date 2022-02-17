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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "yvs_societes")
@NamedQueries({
    @NamedQuery(name = "YvsSocietes.findAll", query = "SELECT y FROM YvsSocietes y WHERE y.actif=true"),
    @NamedQuery(name = "YvsSocietes.findById", query = "SELECT y FROM YvsSocietes y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSocietes.findByAdressSiege", query = "SELECT y FROM YvsSocietes y WHERE y.adressSiege = :adressSiege"),
    @NamedQuery(name = "YvsSocietes.findByCodeAbreviation", query = "SELECT y FROM YvsSocietes y WHERE y.codeAbreviation = :codeAbreviation"),
    @NamedQuery(name = "YvsSocietes.findByCodePostal", query = "SELECT y FROM YvsSocietes y WHERE y.codePostal = :codePostal"),
    @NamedQuery(name = "YvsSocietes.findByDevise", query = "SELECT y FROM YvsSocietes y WHERE y.devise = :devise"),
    @NamedQuery(name = "YvsSocietes.findByEmail", query = "SELECT y FROM YvsSocietes y WHERE y.email = :email"),
    @NamedQuery(name = "YvsSocietes.findByFormeJuridique", query = "SELECT y FROM YvsSocietes y WHERE y.formeJuridique = :formeJuridique"),
    @NamedQuery(name = "YvsSocietes.findByGestva", query = "SELECT y FROM YvsSocietes y WHERE y.gestva = :gestva"),
    @NamedQuery(name = "YvsSocietes.findByLastAuthor", query = "SELECT y FROM YvsSocietes y WHERE y.lastAuthor = :lastAuthor"),
    @NamedQuery(name = "YvsSocietes.findByLogo", query = "SELECT y FROM YvsSocietes y WHERE y.logo = :logo"),
    @NamedQuery(name = "YvsSocietes.findByName", query = "SELECT y FROM YvsSocietes y WHERE y.name = :name"),
    @NamedQuery(name = "YvsSocietes.findByNumeroRegistreComerce", query = "SELECT y FROM YvsSocietes y WHERE y.numeroRegistreComerce = :numeroRegistreComerce"),
    @NamedQuery(name = "YvsSocietes.findBySiege", query = "SELECT y FROM YvsSocietes y WHERE y.siege = :siege"),
    @NamedQuery(name = "YvsSocietes.findBySiteWeb", query = "SELECT y FROM YvsSocietes y WHERE y.siteWeb = :siteWeb"),
    @NamedQuery(name = "YvsSocietes.findByTel", query = "SELECT y FROM YvsSocietes y WHERE y.tel = :tel"),
    @NamedQuery(name = "YvsSocietes.findByActif", query = "SELECT y FROM YvsSocietes y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsSocietes.findByRegimeCnps", query = "SELECT y FROM YvsSocietes y WHERE y.regimeCnps = :regimeCnps"),
    @NamedQuery(name = "YvsSocietes.findByDateSave", query = "SELECT y FROM YvsSocietes y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsSocietes.findByDateUpdate", query = "SELECT y FROM YvsSocietes y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsSocietes.findByDescription", query = "SELECT y FROM YvsSocietes y WHERE y.description = :description"),
    @NamedQuery(name = "YvsSocietes.findByEcartDocument", query = "SELECT y FROM YvsSocietes y WHERE y.ecartDocument = :ecartDocument"),
    @NamedQuery(name = "YvsSocietes.findByAPropos", query = "SELECT y FROM YvsSocietes y WHERE y.aPropos = :aPropos")})
public class YvsSocietes implements Serializable {

    @OneToMany(mappedBy = "societe")
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "adress_siege")
    private String adressSiege;
    @Column(name = "code_abreviation")
    private String codeAbreviation;
    @Column(name = "code_postal")
    private String codePostal;
    @Column(name = "devise")
    private String devise;
    @Column(name = "email")
    private String email;
    @Column(name = "forme_juridique")
    private String formeJuridique;
    @Column(name = "gestva")
    private Boolean gestva;
    @Column(name = "last_author")
    private String lastAuthor;
    @Column(name = "logo")
    private String logo;
    @Column(name = "name")
    private String name;
    @Column(name = "numero_registre_comerce")
    private String numeroRegistreComerce;
    @Column(name = "numero_contribuable")
    private String numeroContribuable;
    @Column(name = "siege")
    private String siege;
    @Column(name = "site_web")
    private String siteWeb;
    @Column(name = "tel")
    private String tel;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "regime_cnps")
    private String regimeCnps;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "description")
    private String description;
    @Column(name = "ecart_document")
    private Integer ecartDocument;
    @Column(name = "a_propos")
    private String aPropos;

    public YvsSocietes() {
    }

    public YvsSocietes(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdressSiege() {
        return adressSiege;
    }

    public void setAdressSiege(String adressSiege) {
        this.adressSiege = adressSiege;
    }

    public String getCodeAbreviation() {
        return codeAbreviation;
    }

    public void setCodeAbreviation(String codeAbreviation) {
        this.codeAbreviation = codeAbreviation;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFormeJuridique() {
        return formeJuridique;
    }

    public void setFormeJuridique(String formeJuridique) {
        this.formeJuridique = formeJuridique;
    }

    public Boolean getGestva() {
        return gestva;
    }

    public void setGestva(Boolean gestva) {
        this.gestva = gestva;
    }

    public String getLastAuthor() {
        return lastAuthor;
    }

    public void setLastAuthor(String lastAuthor) {
        this.lastAuthor = lastAuthor;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumeroRegistreComerce() {
        return numeroRegistreComerce;
    }

    public void setNumeroRegistreComerce(String numeroRegistreComerce) {
        this.numeroRegistreComerce = numeroRegistreComerce;
    }

    public String getSiege() {
        return siege;
    }

    public void setSiege(String siege) {
        this.siege = siege;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getRegimeCnps() {
        return regimeCnps;
    }

    public void setRegimeCnps(String regimeCnps) {
        this.regimeCnps = regimeCnps;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEcartDocument() {
        return ecartDocument;
    }

    public void setEcartDocument(Integer ecartDocument) {
        this.ecartDocument = ecartDocument;
    }

    public String getAPropos() {
        return aPropos;
    }

    public void setAPropos(String aPropos) {
        this.aPropos = aPropos;
    }

    public String getNumeroContribuable() {
        return numeroContribuable;
    }

    public void setNumeroContribuable(String numeroContribuable) {
        this.numeroContribuable = numeroContribuable;
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
        if (!(object instanceof YvsSocietes)) {
            return false;
        }
        YvsSocietes other = (YvsSocietes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsSocietes[ id=" + id + " ]";
    }

}
