/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity;

//import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
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
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
//import lymytz.dao.DateTimeAdapter;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_entete_doc_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComEnteteDocVente.findAll", query = "SELECT y FROM YvsComEnteteDocVente y"),
    @NamedQuery(name = "YvsComEnteteDocVente.findById", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComEnteteDocVente.findOneFiche", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.dateEntete = :date AND y.creneau=:creno"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByDateEntete", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.dateEntete = :dateEntete"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByEtat", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.etat = :etat"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByCloturer", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.cloturer = :cloturer"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByDateCloturer", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.dateCloturer = :dateCloturer"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByStatutLivre", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.statutLivre = :statutLivre"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByStatutRegle", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.statutRegle = :statutRegle"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByDateValider", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.dateValider = :dateValider"),
    @NamedQuery(name = "YvsComEnteteDocVente.findEncourByUsers_", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.etat NOT IN :etats AND y.dateEntete>=:date AND y.cloturer=false ORDER BY y.dateEntete DESC"),
    @NamedQuery(name = "YvsComEnteteDocVente.countFicheOpenByUsers", query = "SELECT COUNT(y) FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.cloturer=false"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByUsersStatut_", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.cloturer=FALSE ORDER BY y.dateEntete DESC"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByDateUpdate", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByDateSave", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.dateSave = :dateSave")})
public class YvsComEnteteDocVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_entete")
    @Temporal(TemporalType.DATE)
    private Date dateEntete;
    @Column(name = "etat")
    private String etat;
    @Column(name = "cloturer")
    private Boolean cloturer;
    @Column(name = "date_cloturer")
    @Temporal(TemporalType.DATE)
    private Date dateCloturer;
    @Column(name = "statut_livre")
    private String statutLivre;
    @Column(name = "statut_regle")
    private String statutRegle;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne
    private YvsAgences agence;
    @JoinColumn(name = "creneau", referencedColumnName = "id")
    @ManyToOne
    private YvsComCreneauHoraireUsers creneau;
    @JoinColumn(name = "cloturer_by", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers cloturerBy;
    @JoinColumn(name = "valider_by", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers validerBy;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    @Transient
    private Boolean synchroniser;

    public YvsComEnteteDocVente() {
    }

    public YvsComEnteteDocVente(Long id) {
        this.id = id;
    }
    public YvsComEnteteDocVente(YvsComEnteteDocVente e) {
        this.id = e.id;
        this.dateEntete = e.dateEntete;
        this.cloturer = e.cloturer;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public Date getDateEntete() {
        return dateEntete!=null?dateEntete:new Date();
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void setDateEntete(Date dateEntete) {
        this.dateEntete = dateEntete;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public Date getDateCloturer() {
        return dateCloturer!=null?dateCloturer:new Date();
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void setDateCloturer(Date dateCloturer) {
        this.dateCloturer = dateCloturer;
    }

    public String getStatutLivre() {
        return statutLivre;
    }

    public void setStatutLivre(String statutLivre) {
        this.statutLivre = statutLivre;
    }

    public String getStatutRegle() {
        return statutRegle;
    }

    public void setStatutRegle(String statutRegle) {
        this.statutRegle = statutRegle;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public Date getDateValider() {
        return dateValider!=null?dateValider:new Date();
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsComCreneauHoraireUsers getCreneau() {
        return creneau;
    }

    public void setCreneau(YvsComCreneauHoraireUsers creneau) {
        this.creneau = creneau;
    }

    public YvsUsers getCloturerBy() {
        return cloturerBy;
    }

    public void setCloturerBy(YvsUsers cloturerBy) {
        this.cloturerBy = cloturerBy;
    }

    public YvsUsers getValiderBy() {
        return validerBy;
    }

    public void setValiderBy(YvsUsers validerBy) {
        this.validerBy = validerBy;
    }

    public Boolean isSynchroniser() {
        return synchroniser;
    }

    public void setSynchroniser(Boolean synchroniser) {
        this.synchroniser = synchroniser;
    }

    @Override
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsComEnteteDocVente)) {
            return false;
        }
        YvsComEnteteDocVente other = (YvsComEnteteDocVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComEnteteDocVente[ id=" + id + " ]";
    }

}
