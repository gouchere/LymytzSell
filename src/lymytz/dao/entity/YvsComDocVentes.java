/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity;

//import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import lymytz.service.utils.Constantes;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_doc_ventes")
@NamedQueries({
    @NamedQuery(name = "YvsComDocVentes.findAll", query = "SELECT y FROM YvsComDocVentes y"),
    @NamedQuery(name = "YvsComDocVentes.findById", query = "SELECT y FROM YvsComDocVentes y LEFT JOIN FETCH y.contenus WHERE y.id = :id"),
    @NamedQuery(name = "YvsComDocVentes.findByReference", query = "SELECT y FROM YvsComDocVentes y WHERE y.numeroExterne LIKE :numDoc ORDER by y.numeroExterne DESC"),
    @NamedQuery(name = "YvsComDocVentes.countDocByHeaderAndType", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE (y.typeDoc =:type1 OR y.typeDoc=:type2) AND y.enteteDoc=:header"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeader", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE  (y.typeDoc = :typesDoc OR y.typeDoc = :typesDoc1)  AND y.enteteDoc=:header AND y.statut='V'"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeaderNLivre", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc=:header AND y.statutLivre!=:statutLivre AND y.typeDoc IN :typesDocs"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeader1", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE  y.typeDoc = :typesDoc AND y.enteteDoc=:header AND y.statut='V'"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeaderL", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE  y.typeDoc = :typesDoc AND y.enteteDoc=:header AND y.statut='V' AND y.statutLivre=:statutL"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeaderR", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE  y.typeDoc = :typesDoc AND y.enteteDoc=:header AND y.statut='V' AND y.statutRegle=:statutR"),
    @NamedQuery(name = "YvsComDocVentes.findByNumPiece", query = "SELECT y FROM YvsComDocVentes y WHERE y.numPiece = :numPiece"),
    @NamedQuery(name = "YvsComDocVentes.findByTypeDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocVentes.findByStatut", query = "SELECT y FROM YvsComDocVentes y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsComDocVentes.findByNomClient", query = "SELECT y FROM YvsComDocVentes y WHERE y.nomClient = :nomClient"),
    @NamedQuery(name = "YvsComDocVentes.findByNumDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.numDoc = :numDoc"),
    @NamedQuery(name = "YvsComDocVentes.findByHeureDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.heureDoc = :heureDoc"),
    @NamedQuery(name = "YvsComDocVentes.findByMontantAvance", query = "SELECT y FROM YvsComDocVentes y WHERE y.montantAvance = :montantAvance"),
    @NamedQuery(name = "YvsComDocVentes.findByDateSave", query = "SELECT y FROM YvsComDocVentes y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComDocVentes.findByMouvStock", query = "SELECT y FROM YvsComDocVentes y WHERE y.mouvStock = :mouvStock"),
    @NamedQuery(name = "YvsComDocVentes.findByImpression", query = "SELECT y FROM YvsComDocVentes y WHERE y.impression = :impression"),
    @NamedQuery(name = "YvsComDocVentes.findByDateSolder", query = "SELECT y FROM YvsComDocVentes y WHERE y.dateSolder = :dateSolder"),
    @NamedQuery(name = "YvsComDocVentes.findByDateLivraison", query = "SELECT y FROM YvsComDocVentes y WHERE y.dateLivraison = :dateLivraison"),
    @NamedQuery(name = "YvsComDocVentes.findByCloturer", query = "SELECT y FROM YvsComDocVentes y WHERE y.cloturer = :cloturer"),
    @NamedQuery(name = "YvsComDocVentes.findByDateCloturer", query = "SELECT y FROM YvsComDocVentes y WHERE y.dateCloturer = :dateCloturer"),
    @NamedQuery(name = "YvsComDocVentes.findByDateValider", query = "SELECT y FROM YvsComDocVentes y WHERE y.dateValider = :dateValider"),
    @NamedQuery(name = "YvsComDocVentes.findByDateAnnuler", query = "SELECT y FROM YvsComDocVentes y WHERE y.dateAnnuler = :dateAnnuler"),
    @NamedQuery(name = "YvsComDocVentes.findByDescription", query = "SELECT y FROM YvsComDocVentes y WHERE y.description = :description"),
    @NamedQuery(name = "YvsComDocVentes.findByStatutLivre", query = "SELECT y FROM YvsComDocVentes y WHERE y.statutLivre = :statutLivre"),
    @NamedQuery(name = "YvsComDocVentes.findByStatutRegle", query = "SELECT y FROM YvsComDocVentes y WHERE y.statutRegle = :statutRegle"),
    @NamedQuery(name = "YvsComDocVentes.findByDateLivraisonPrevu", query = "SELECT y FROM YvsComDocVentes y WHERE y.dateLivraisonPrevu = :dateLivraisonPrevu"),
    @NamedQuery(name = "YvsComDocVentes.countFactureNonLivreOrNonPayeByHeader", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE (y.statut != 'A' AND (y.statut != :statut OR y.statutLivre != :statutLivre OR y.statutRegle != :statutRegle)) AND y.typeDoc = :typeDoc AND y.enteteDoc = :header"),
    @NamedQuery(name = "YvsComDocVentes.findByLivraisonAuto", query = "SELECT y FROM YvsComDocVentes y WHERE y.livraisonAuto = :livraisonAuto"),
    @NamedQuery(name = "YvsComDocVentes.findByDateUpdate", query = "SELECT y FROM YvsComDocVentes y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComDocVentes.findByCommision", query = "SELECT y FROM YvsComDocVentes y WHERE y.commision = :commision"),
    @NamedQuery(name = "YvsComDocVentes.findByEtapeTotal", query = "SELECT y FROM YvsComDocVentes y WHERE y.etapeTotal = :etapeTotal"),
    @NamedQuery(name = "YvsComDocVentes.findByEtapeValide", query = "SELECT y FROM YvsComDocVentes y WHERE y.etapeValide = :etapeValide"),
    @NamedQuery(name = "YvsComDocVentes.findByNumeroExterne", query = "SELECT y FROM YvsComDocVentes y WHERE y.numeroExterne = :numeroExterne"),
    @NamedQuery(name = "YvsComDocVentes.findByTelephone", query = "SELECT y FROM YvsComDocVentes y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsComDocVentes.findByComptabilise", query = "SELECT y FROM YvsComDocVentes y WHERE y.comptabilise = :comptabilise"),
    @NamedQuery(name = "YvsComDocVentes.findByNature", query = "SELECT y FROM YvsComDocVentes y WHERE y.nature = :nature"),
    @NamedQuery(name = "YvsComDocVentes.findByNotes", query = "SELECT y FROM YvsComDocVentes y WHERE y.notes = :notes")})
public class YvsComDocVentes extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_doc_ventes_id_seq", name = "yvs_doc_ventes_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_doc_ventes_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_piece")
    private String numPiece;
    @Column(name = "type_doc")
    private String typeDoc;
    @Column(name = "statut")
    private String statut;
    @Column(name = "nom_client")
    private String nomClient;
    @Column(name = "num_doc")
    private String numDoc;
    @Column(name = "heure_doc")
    @Temporal(TemporalType.TIME)
    private Date heureDoc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_avance")
    private Double montantAvance;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "mouv_stock")
    private Boolean mouvStock;
    @Column(name = "impression")
    private Integer impression;
    @Column(name = "date_solder")
    @Temporal(TemporalType.DATE)
    private Date dateSolder;
    @Column(name = "date_livraison")
    @Temporal(TemporalType.DATE)
    private Date dateLivraison;
    @Column(name = "cloturer")
    private Boolean cloturer;
    @Column(name = "date_cloturer")
    @Temporal(TemporalType.DATE)
    private Date dateCloturer;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @Column(name = "date_annuler")
    @Temporal(TemporalType.DATE)
    private Date dateAnnuler;
    @Column(name = "description")
    private String description;
    @Column(name = "statut_livre")
    private String statutLivre;
    @Column(name = "statut_regle")
    private String statutRegle;
    @Column(name = "date_livraison_prevu")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLivraisonPrevu;
    @Column(name = "livraison_auto")
    private Boolean livraisonAuto;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "commision")
    private Double commision;
    @Column(name = "etape_total")
    private Integer etapeTotal;
    @Column(name = "etape_valide")
    private Integer etapeValide;
    @Column(name = "numero_externe")
    private String numeroExterne;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "nature")
    private String nature;
    @Column(name = "notes")
    private String notes;
    @JoinColumn(name = "categorie_comptable", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCategorieComptable categorieComptable;
    @JoinColumn(name = "depot_livrer", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepots depotLivrer;
    @JoinColumn(name = "annuler_by", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers annulerBy;
    @JoinColumn(name = "model_reglement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModelReglement modelReglement;
    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne
    private YvsComClient tiers;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private YvsComClient client;
    @JoinColumn(name = "valider_by", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers validerBy;
    @JoinColumn(name = "document_lie", referencedColumnName = "id")
    @ManyToOne
    private YvsComDocVentes documentLie;
    @JoinColumn(name = "entete_doc", referencedColumnName = "id")
    @ManyToOne
    private YvsComEnteteDocVente enteteDoc;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "adresse", referencedColumnName = "id")
    @ManyToOne
    private YvsDictionnaire adresse;
    @JoinColumn(name = "tranche_livrer", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhTrancheHoraire trancheLivrer;
    @JoinColumn(name = "operateur", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers operateur;
    @JoinColumn(name = "livreur", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers livreur;
    @JoinColumn(name = "cloturer_by", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers cloturerBy;

    @OneToMany(mappedBy = "docVente", fetch = FetchType.LAZY)
    private List<YvsComContenuDocVente> contenus;
    @OneToMany(mappedBy = "vente", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceVente> reglements;
    @OneToMany(mappedBy = "facture", fetch = FetchType.LAZY)
    private List<YvsComCommercialVente> commerciaux;
    @OneToMany(mappedBy = "documentLie", fetch = FetchType.LAZY)
    private List<YvsComDocVentes> documents;

    @Transient
    private Boolean synchroniser;
    @Transient
    private double montantHT, montantTaxe, montantTTC, montantRemise, montantRemises, montantTotal, montantRistourne, montantCommission, montantCS,
            montantResteApayer, montantTaxeR, montantPlanifier, montantNetAPayer, montantAvoir, montantAvanceAvoir;
    @Transient
    private boolean livrer = true; //Utilisé sur l'app de caisse
    @Transient
    private String nom_client;

    @Transient
    private boolean facture;
    @Transient
    private String onfacture;

    public YvsComDocVentes() {
        documents = new ArrayList<>();
    }

    public YvsComDocVentes(Long id) {
        super();
        this.id = id;
    }

    public YvsComDocVentes(YvsComDocVentes y) {
        super();
        this.id = y.id;
        this.annulerBy = y.annulerBy;
        this.cloturer = y.cloturer;
        this.commision = y.commision;
        this.dateAnnuler = y.dateAnnuler;
        this.dateCloturer = y.dateCloturer;
        this.dateLivraison = y.dateLivraison;
        this.dateLivraisonPrevu = y.dateLivraisonPrevu;
        this.comptabilise = y.comptabilise;
        this.dateSave = y.dateSave;
        this.dateSolder = y.dateSolder;
        this.dateUpdate = y.dateUpdate;
        this.dateValider = y.dateValider;
        this.depotLivrer = y.depotLivrer;
        this.description = y.description;
        this.heureDoc = y.heureDoc;
        this.impression = y.impression;
        this.livraisonAuto = y.livraisonAuto;
        this.livreur = y.livreur;
        this.modelReglement = y.modelReglement;
        this.montantAvance = y.montantAvance;
        this.mouvStock = y.mouvStock;
        this.nomClient = y.nomClient;
        this.numDoc = y.numDoc;
        this.numeroExterne = y.numeroExterne;
        this.numPiece = y.numPiece;
        this.tiers = y.tiers;
        this.trancheLivrer = y.trancheLivrer;
        this.typeDoc = y.typeDoc;
        this.statut = y.statut;
        this.statutLivre = y.statutLivre;
        this.statutRegle = y.statutRegle;
        this.validerBy = y.validerBy;
        this.adresse = y.adresse;
        this.author = y.author;
        this.telephone = y.telephone;
        this.documentLie = y.documentLie;
        this.client = y.client;
        this.enteteDoc = y.enteteDoc;
        this.categorieComptable = y.categorieComptable;
        this.cloturerBy = y.cloturerBy;
        this.contenus = new ArrayList<>(y.contenus);
        this.nom_client = y.nom_client;
        this.etapeValide = y.etapeValide;
        this.etapeTotal = y.etapeTotal;
        this.operateur = y.operateur;
    }

    public YvsComDocVentes(YvsComDocVentes y, boolean all) {
        this.id = y.id;
        this.cloturer = y.cloturer;
        this.commision = y.commision;
        this.dateAnnuler = y.dateAnnuler;
        this.dateCloturer = y.dateCloturer;
        this.dateLivraison = y.dateLivraison;
        this.dateLivraisonPrevu = y.dateLivraisonPrevu;
        this.comptabilise = y.comptabilise;
        this.dateSave = y.dateSave;
        this.dateSolder = y.dateSolder;
        this.dateUpdate = y.dateUpdate;
        this.dateValider = y.dateValider;
        this.description = y.description;
        this.heureDoc = y.heureDoc;
        this.impression = y.impression;
        this.livraisonAuto = y.livraisonAuto;
        this.montantAvance = y.montantAvance;
        this.mouvStock = y.mouvStock;
        this.nomClient = y.nomClient;
        this.numDoc = y.numDoc;
        this.numeroExterne = y.numeroExterne;
        this.numPiece = y.numPiece;
        this.typeDoc = y.typeDoc;
        this.statut = y.statut;
        this.statutLivre = y.statutLivre;
        this.statutRegle = y.statutRegle;
        if (trancheLivrer != null) {
            this.trancheLivrer = new YvsGrhTrancheHoraire(y.trancheLivrer.getId());
        }
        this.depotLivrer = new YvsBaseDepots(y.depotLivrer.getId());
        if (y.modelReglement != null) {
            this.modelReglement = new YvsBaseModelReglement(y.modelReglement.getId());
        }
        if (y.tiers != null) {
            this.tiers = new YvsComClient(y.tiers.getId());
        }
        if (y.livreur != null) {
            this.livreur = new YvsUsers(y.livreur.getId());
        }
        if (y.annulerBy != null) {
            this.annulerBy = new YvsUsers(y.annulerBy.getId());
        }
        if (y.validerBy != null) {
            this.validerBy = new YvsUsers(y.validerBy.getId());
        }
        if (y.adresse != null) {
            this.adresse = new YvsDictionnaire(y.adresse.getId());
        }
        this.author = new YvsUsersAgence(y.author.getId());
        this.telephone = y.telephone;
//        this.documentLie = y.documentLie;
        this.client = new YvsComClient(y.client);
        this.enteteDoc = new YvsComEnteteDocVente(y.enteteDoc.getId());
        this.categorieComptable = new YvsBaseCategorieComptable(y.categorieComptable.getId());
//        this.cloturerBy = y.cloturerBy;
//        this.contenus = new ArrayList<>(y.contenus);
        this.nom_client = y.nom_client;
        this.etapeValide = y.etapeValide;
        this.etapeTotal = y.etapeTotal;
//        this.operateur = y.operateur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getHeureDoc() {
        return heureDoc;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setHeureDoc(Date heureDoc) {
        this.heureDoc = heureDoc;
    }

    public Double getMontantAvance() {
        return montantAvance != null ? montantAvance : 0;
    }

    public void setMontantAvance(Double montantAvance) {
        this.montantAvance = montantAvance;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Boolean getMouvStock() {
        return mouvStock;
    }

    public void setMouvStock(Boolean mouvStock) {
        this.mouvStock = mouvStock;
    }

    public Integer getImpression() {
        return impression;
    }

    public void setImpression(Integer impression) {
        this.impression = impression;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSolder() {
        return dateSolder;
    }
//
//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")

    public void setDateSolder(Date dateSolder) {
        this.dateSolder = dateSolder;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateLivraison() {
        return dateLivraison != null ? dateLivraison : new Date();
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateCloturer() {
        return dateCloturer != null ? dateCloturer : new Date();
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateCloturer(Date dateCloturer) {
        this.dateCloturer = dateCloturer;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateValider() {
        return dateValider != null ? dateValider : new Date();
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateAnnuler() {
        return dateAnnuler != null ? dateAnnuler : new Date();
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateAnnuler(Date dateAnnuler) {
        this.dateAnnuler = dateAnnuler;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateLivraisonPrevu() {
        return dateLivraisonPrevu;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateLivraisonPrevu(Date dateLivraisonPrevu) {
        this.dateLivraisonPrevu = dateLivraisonPrevu;
    }

    public Boolean getLivraisonAuto() {
        return livraisonAuto;
    }

    public void setLivraisonAuto(Boolean livraisonAuto) {
        this.livraisonAuto = livraisonAuto;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

//    @XmlJavaTypeAdapter(DateTimeAdapter.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Double getCommision() {
        return commision;
    }

    public void setCommision(Double commision) {
        this.commision = commision;
    }

    public Integer getEtapeTotal() {
        return etapeTotal;
    }

    public void setEtapeTotal(Integer etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public Integer getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(Integer etapeValide) {
        this.etapeValide = etapeValide;
    }

    public String getNumeroExterne() {
        return numeroExterne;
    }

    public void setNumeroExterne(String numeroExterne) {
        this.numeroExterne = numeroExterne;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public YvsBaseCategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(YvsBaseCategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public YvsBaseDepots getDepotLivrer() {
        return depotLivrer;
    }

    public void setDepotLivrer(YvsBaseDepots depotLivrer) {
        this.depotLivrer = depotLivrer;
    }

    public YvsUsers getAnnulerBy() {
        return annulerBy;
    }

    public void setAnnulerBy(YvsUsers annulerBy) {
        this.annulerBy = annulerBy;
    }

    public YvsBaseModelReglement getModelReglement() {
        return modelReglement;
    }

    public void setModelReglement(YvsBaseModelReglement modelReglement) {
        this.modelReglement = modelReglement;
    }

    public YvsComClient getTiers() {
        return tiers;
    }

    public void setTiers(YvsComClient tiers) {
        this.tiers = tiers;
    }

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
    }

    public YvsUsers getValiderBy() {
        return validerBy;
    }

    public void setValiderBy(YvsUsers validerBy) {
        this.validerBy = validerBy;
    }

    public YvsComDocVentes getDocumentLie() {
        return documentLie;
    }

    public void setDocumentLie(YvsComDocVentes documentLie) {
        this.documentLie = documentLie;
    }

    public YvsComEnteteDocVente getEnteteDoc() {
        return enteteDoc;
    }

    public void setEnteteDoc(YvsComEnteteDocVente enteteDoc) {
        this.enteteDoc = enteteDoc;
    }

    @Override
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsDictionnaire getAdresse() {
        return adresse;
    }

    public void setAdresse(YvsDictionnaire adresse) {
        this.adresse = adresse;
    }

    public YvsGrhTrancheHoraire getTrancheLivrer() {
        return trancheLivrer;
    }

    public void setTrancheLivrer(YvsGrhTrancheHoraire trancheLivrer) {
        this.trancheLivrer = trancheLivrer;
    }

    public YvsUsers getOperateur() {
        return operateur;
    }

    public void setOperateur(YvsUsers operateur) {
        this.operateur = operateur;
    }

    public YvsUsers getLivreur() {
        return livreur;
    }

    public void setLivreur(YvsUsers livreur) {
        this.livreur = livreur;
    }

    public YvsUsers getCloturerBy() {
        return cloturerBy;
    }

    public void setCloturerBy(YvsUsers cloturerBy) {
        this.cloturerBy = cloturerBy;
    }

    public double getMontantHT() {
        montantHT = getMontantTTC() - getMontantTaxe();
        return montantHT;
    }

    public void setMontantHT(double montantHT) {
        this.montantHT = montantHT;
    }

    public double getMontantPlanifier() {
        return montantPlanifier;
    }

    public void setMontantPlanifier(double montantPlanifier) {
        this.montantPlanifier = montantPlanifier;
    }

    public double getMontantTaxe() {
        return montantTaxe;
    }

    public void setMontantTaxe(double montantTaxe) {
        this.montantTaxe = montantTaxe;
    }

    public double getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        this.montantTTC = montantTTC;
    }

    public double getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(double montantRemise) {
        this.montantRemise = montantRemise;
    }

    public double getMontantRemises() {
        return montantRemises;
    }

    public void setMontantRemises(double montantRemises) {
        this.montantRemises = montantRemises;
    }

    public double getMontantAvoir() {
        return montantAvoir;
    }

    public void setMontantAvoir(double montantAvoir) {
        this.montantAvoir = montantAvoir;
    }

    public double getMontantAvanceAvoir() {
        return montantAvanceAvoir;
    }

    public void setMontantAvanceAvoir(double montantAvanceAvoir) {
        this.montantAvanceAvoir = montantAvanceAvoir;
    }

    public double getMontantTotal() {
        montantTotal = getMontantTTC() + getMontantCS() - getMontantRemises() - getMontantAvoir() + getMontantAvanceAvoir();
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantRistourne() {
        return montantRistourne;
    }

    public void setMontantRistourne(double montantRistourne) {
        this.montantRistourne = montantRistourne;
    }

    public double getMontantCommission() {
        return montantCommission;
    }

    public void setMontantCommission(double montantCommission) {
        this.montantCommission = montantCommission;
    }

    public double getMontantCS() {
        return montantCS;
    }

    public void setMontantCS(double montantCS) {
        this.montantCS = montantCS;
    }

    public double getMontantResteApayer() {
        montantResteApayer = getMontantNetAPayer() - getMontantAvance();
        return montantResteApayer;
    }

    public void setMontantResteApayer(double montantResteApayer) {
        this.montantResteApayer = montantResteApayer;
    }

    public double getMontantNetAPayer() {
        montantNetAPayer = getMontantTotal();
        return montantNetAPayer;
    }

    public void setMontantNetAPayer(double montantNetAPayer) {
        this.montantNetAPayer = montantNetAPayer;
    }

    public double getMontantTaxeR() {
        return montantTaxeR;
    }

    public void setMontantTaxeR(double montantTaxeR) {
        this.montantTaxeR = montantTaxeR;
    }

    public Boolean isSynchroniser() {
        return synchroniser;
    }

    public void setSynchroniser(Boolean synchroniser) {
        this.synchroniser = synchroniser;
    }

    @XmlTransient
    public double getMontantResteAPlanifier() {
        double re = getMontantTotal();
//        for (YvsComptaCaissePieceVente p : reglements) {
//            re -= p.getMontant();
//        }
        return re;
    }

    @XmlTransient
    public String getNom_client() {
        nom_client = "";
        if (getNomClient().trim().length() > 0 && getClient() != null) {
            if (getNomClient().equals(getClient().getNom_prenom())) {
                nom_client = getClient().getNom_prenom();
            } else {
                nom_client = getNomClient() + " [" + getClient().getCodeClient() + "]";
            }
        } else if (getClient() != null) {
            nom_client = getClient().getNom_prenom();
        } else if (getNomClient().trim().length() > 0) {
            nom_client = getNomClient();
        }
        return nom_client;
    }

    public List<YvsComptaCaissePieceVente> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceVente> reglements) {
        this.reglements = reglements;
    }

    public List<YvsComCommercialVente> getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(List<YvsComCommercialVente> commerciaux) {
        this.commerciaux = commerciaux;
    }

    public List<YvsComDocVentes> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocVentes> documents) {
        this.documents = documents;
    }

    @XmlTransient
    public String getOnfacture() {
        onfacture = Constantes.ETAT_VALIDE;
        if (!isFacture()) {
            onfacture = Constantes.ETAT_ATTENTE;
            if (documents != null ? !documents.isEmpty() : false) {
                onfacture = Constantes.ETAT_ENCOURS;
                for (YvsComDocVentes d : documents) {
                    if (d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        onfacture = Constantes.ETAT_VALIDE;
                        break;
                    }
                }
            }
        }
        return onfacture;
    }

    @XmlTransient
    public boolean isFacture() {
        facture = (typeDoc != null ? (typeDoc.equals("FV") ? true : (typeDoc.equals("FAV") ? true : (typeDoc.equals("FRV")))) : false);
        return facture;
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
        if (!(object instanceof YvsComDocVentes)) {
            return false;
        }
        YvsComDocVentes other = (YvsComDocVentes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public List<YvsComContenuDocVente> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocVente> contenus) {
        this.contenus = contenus;
    }

    @Override
    public String toString() {
        return "YvsComDocVentes{" + "id=" + id + ", numPiece=" + numPiece + ", typeDoc=" + typeDoc + ", statut=" + statut + ", nomClient=" + nomClient + ", numDoc=" + numDoc + ", heureDoc=" + heureDoc + ", montantAvance=" + montantAvance + ", dateSave=" + dateSave + ", mouvStock=" + mouvStock + ", impression=" + impression + ", dateSolder=" + dateSolder + ", dateLivraison=" + dateLivraison + ", cloturer=" + cloturer + ", dateCloturer=" + dateCloturer + ", dateValider=" + dateValider + ", dateAnnuler=" + dateAnnuler + ", description=" + description + ", statutLivre=" + statutLivre + ", statutRegle=" + statutRegle + ", dateLivraisonPrevu=" + dateLivraisonPrevu + ", livraisonAuto=" + livraisonAuto + ", dateUpdate=" + dateUpdate + ", commision=" + commision + ", etapeTotal=" + etapeTotal + ", etapeValide=" + etapeValide + ", numeroExterne=" + numeroExterne + ", telephone=" + telephone + ", comptabilise=" + comptabilise + ", nature=" + nature + ", notes=" + notes + ", categorieComptable=" + categorieComptable + ", depotLivrer=" + depotLivrer + ", annulerBy=" + annulerBy + ", modelReglement=" + modelReglement + ", tiers=" + tiers + ", client=" + client + ", validerBy=" + validerBy + ", documentLie=" + documentLie + ", enteteDoc=" + enteteDoc + ", author=" + author + ", adresse=" + adresse + ", trancheLivrer=" + trancheLivrer + ", operateur=" + operateur + ", livreur=" + livreur + ", cloturerBy=" + cloturerBy + ", contenus=" + contenus + ", reglements=" + reglements + ", commerciaux=" + commerciaux + ", synchroniser=" + synchroniser + ", montantHT=" + montantHT + ", montantTaxe=" + montantTaxe + ", montantTTC=" + montantTTC + ", montantRemise=" + montantRemise + ", montantRemises=" + montantRemises + ", montantTotal=" + montantTotal + ", montantRistourne=" + montantRistourne + ", montantCommission=" + montantCommission + ", montantCS=" + montantCS + ", montantResteApayer=" + montantResteApayer + ", montantTaxeR=" + montantTaxeR + ", montantPlanifier=" + montantPlanifier + ", montantNetAPayer=" + montantNetAPayer + ", montantAvoir=" + montantAvoir + ", montantAvanceAvoir=" + montantAvanceAvoir + ", livrer=" + livrer + ", nom_client=" + nom_client + '}';
    }

}
