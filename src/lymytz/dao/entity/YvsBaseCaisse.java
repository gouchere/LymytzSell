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
import javax.persistence.FetchType;
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
@Table(name = "yvs_base_caisse")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCaisse.findAll", query = "SELECT y FROM YvsBaseCaisse y"),
    @NamedQuery(name = "YvsBaseCaisse.findById", query = "SELECT y FROM YvsBaseCaisse y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCaisse.findByIntitule", query = "SELECT y FROM YvsBaseCaisse y WHERE y.intitule = :intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByAdresse", query = "SELECT y FROM YvsBaseCaisse y WHERE y.adresse = :adresse"),
    @NamedQuery(name = "YvsBaseCaisse.findByActif", query = "SELECT y FROM YvsBaseCaisse y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseCaisse.findByCanNegative", query = "SELECT y FROM YvsBaseCaisse y WHERE y.canNegative = :canNegative"),
    @NamedQuery(name = "YvsBaseCaisse.findByTypeCaisse", query = "SELECT y FROM YvsBaseCaisse y WHERE y.typeCaisse = :typeCaisse"),
    @NamedQuery(name = "YvsBaseCaisse.findByDefaultCaisse", query = "SELECT y FROM YvsBaseCaisse y WHERE y.defaultCaisse = :defaultCaisse"),
    @NamedQuery(name = "YvsBaseCaisse.findByGiveBilletage", query = "SELECT y FROM YvsBaseCaisse y WHERE y.giveBilletage = :giveBilletage"),
    @NamedQuery(name = "YvsBaseCaisse.findByCode", query = "SELECT y FROM YvsBaseCaisse y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseCaisse.findByPrincipal", query = "SELECT y FROM YvsBaseCaisse y WHERE y.principal = :principal"),
    @NamedQuery(name = "YvsBaseCaisse.findByCaissier", query = "SELECT y FROM YvsBaseCaisse y WHERE y.caissier = :caissier ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseCaisse.findByExecuteTrigger", query = "SELECT y FROM YvsBaseCaisse y WHERE y.executeTrigger = :executeTrigger")})
public class YvsBaseCaisse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "intitule")
    private String intitule;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "can_negative")
    private Boolean canNegative;
    @Column(name = "type_caisse")
    private String typeCaisse;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "default_caisse")
    private Boolean defaultCaisse;
    @Column(name = "give_billetage")
    private Boolean giveBilletage;
    @Column(name = "code")
    private String code;
    @Column(name = "principal")
    private Boolean principal;
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse parent;
    @JoinColumn(name = "code_acces", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCodeAcces codeAcces;
    @JoinColumn(name = "mode_reg_defaut", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModeReglement modeRegDefaut;
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers caissier;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "caisseSource", fetch = FetchType.LAZY)
    private List<YvsBaseLiaisonCaisse> caissesLiees;

    public YvsBaseCaisse() {
    }
    public YvsBaseCaisse(YvsBaseCaisse c) {
        super();
        this.id=c.id;
        this.intitule=c.intitule;
        this.actif=c.actif;
        this.adresse=c.adresse;
        this.code=c.code;
        this.codeAcces=c.codeAcces;
        this.giveBilletage=c.giveBilletage;
        this.modeRegDefaut=c.modeRegDefaut;
        this.typeCaisse=c.typeCaisse;
    }

    public YvsBaseCaisse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getCanNegative() {
        return canNegative;
    }

    public void setCanNegative(Boolean canNegative) {
        this.canNegative = canNegative;
    }

    public String getTypeCaisse() {
        return typeCaisse;
    }

    public void setTypeCaisse(String typeCaisse) {
        this.typeCaisse = typeCaisse;
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

    public Boolean getDefaultCaisse() {
        return defaultCaisse;
    }

    public void setDefaultCaisse(Boolean defaultCaisse) {
        this.defaultCaisse = defaultCaisse;
    }

    public Boolean getGiveBilletage() {
        return giveBilletage;
    }

    public void setGiveBilletage(Boolean giveBilletage) {
        this.giveBilletage = giveBilletage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public YvsBaseCaisse getParent() {
        return parent;
    }

    public void setParent(YvsBaseCaisse parent) {
        this.parent = parent;
    }

    public YvsBaseCodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(YvsBaseCodeAcces codeAcces) {
        this.codeAcces = codeAcces;
    }

    public YvsBaseModeReglement getModeRegDefaut() {
        return modeRegDefaut;
    }

    public void setModeRegDefaut(YvsBaseModeReglement modeRegDefaut) {
        this.modeRegDefaut = modeRegDefaut;
    }

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public List<YvsBaseLiaisonCaisse> getCaissesLiees() {
        return caissesLiees;
    }

    public void setCaissesLiees(List<YvsBaseLiaisonCaisse> caissesLiees) {
        this.caissesLiees = caissesLiees;
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
        if (!(object instanceof YvsBaseCaisse)) {
            return false;
        }
        YvsBaseCaisse other = (YvsBaseCaisse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseCaisse[ id=" + id + " ]";
    }
}
