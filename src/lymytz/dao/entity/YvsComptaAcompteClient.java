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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_compta_acompte_client")
@NamedQueries({
    @NamedQuery(name = "YvsComptaAcompteClient.findAll", query = "SELECT y FROM YvsComptaAcompteClient y"),
    @NamedQuery(name = "YvsComptaAcompteClient.findById", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByMontant", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByDateAcompte", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.dateAcompte = :dateAcompte"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByNumRefrence", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.numRefrence = :numRefrence"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByCommentaire", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.commentaire = :commentaire"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByStatut", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByDateUpdate", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByDateSave", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByReferenceExterne", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.referenceExterne = :referenceExterne"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByDatePaiement", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.datePaiement = :datePaiement"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByStatutNotif", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.statutNotif = :statutNotif"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByNature", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.nature = :nature"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByRepartirAutomatique", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.repartirAutomatique = :repartirAutomatique"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByComptabilise", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.comptabilise = :comptabilise")})
public class YvsComptaAcompteClient extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_acompte_client_id_seq", name = "yvs_compta_acompte_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_acompte_client_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_acompte")
    @Temporal(TemporalType.DATE)
    private Date dateAcompte;
    @Column(name = "num_refrence")
    private String numRefrence;
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "date_paiement")
    @Temporal(TemporalType.DATE)
    private Date datePaiement;
    @Column(name = "statut_notif")
    private Character statutNotif;
    @Column(name = "nature")
    private Character nature;
    @Column(name = "repartir_automatique")
    private Boolean repartirAutomatique;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModeReglement model;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private YvsComClient client;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "acompte")
    private List<YvsComptaNotifReglementVente> notifications;

    public YvsComptaAcompteClient() {
    }

    public YvsComptaAcompteClient(Long id) {
        this.id = id;
    }

    public YvsComptaAcompteClient(YvsComptaAcompteClient y) {
        this(y.id);
        adresseServeur = y.adresseServeur;
        author = y.author;
        caisse = y.caisse;
        client = y.client;
        commentaire = y.commentaire;
        comptabilise = y.comptabilise;
        dateAcompte = y.dateAcompte;
        datePaiement = y.datePaiement;
        dateSave = y.dateSave;
        dateUpdate = y.dateUpdate;
        id = y.id;
        idDistant = y.idDistant;
        model = y.model;
        montant = y.montant;
        nature = y.nature;
        numRefrence = y.numRefrence;
        referenceExterne = y.referenceExterne;
        repartirAutomatique = y.repartirAutomatique;
        statut = y.statut;
        statutNotif = y.statutNotif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Date getDateAcompte() {
        return dateAcompte;
    }

    public void setDateAcompte(Date dateAcompte) {
        this.dateAcompte = dateAcompte;
    }

    public String getNumRefrence() {
        return numRefrence;
    }

    public void setNumRefrence(String numRefrence) {
        this.numRefrence = numRefrence;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Character getStatut() {
        return statut;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
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

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Character getStatutNotif() {
        return statutNotif;
    }

    public void setStatutNotif(Character statutNotif) {
        this.statutNotif = statutNotif;
    }

    public Character getNature() {
        return nature;
    }

    public void setNature(Character nature) {
        this.nature = nature;
    }

    public Boolean getRepartirAutomatique() {
        return repartirAutomatique;
    }

    public void setRepartirAutomatique(Boolean repartirAutomatique) {
        this.repartirAutomatique = repartirAutomatique;
    }

    public Boolean getComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public List<YvsComptaNotifReglementVente> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<YvsComptaNotifReglementVente> notifications) {
        this.notifications = notifications;
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
        if (!(object instanceof YvsComptaAcompteClient)) {
            return false;
        }
        YvsComptaAcompteClient other = (YvsComptaAcompteClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComptaAcompteClient[ id=" + id + " ]";
    }

}
