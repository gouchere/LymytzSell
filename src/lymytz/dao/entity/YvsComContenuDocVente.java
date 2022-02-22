/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity;

import java.io.Serializable;
import java.math.BigInteger;
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
import lymytz.service.utils.Constantes;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_contenu_doc_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComContenuDocVente.findAll", query = "SELECT y FROM YvsComContenuDocVente y"),
    @NamedQuery(name = "YvsComContenuDocVente.findById", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComContenuDocVente.findDocById", query = "SELECT y.docVente FROM YvsComContenuDocVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComContenuDocVente.findByDocVente", query = "SELECT y FROM YvsComContenuDocVente y JOIN FETCH y.docVente WHERE y.docVente = :docVente"),
    @NamedQuery(name = "YvsComContenuDocVente.findByPrix", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.prix = :prix"),
    @NamedQuery(name = "YvsComContenuDocVente.findByRemise", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.remise = :remise"),
    @NamedQuery(name = "YvsComContenuDocVente.findByTaxe", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.taxe = :taxe"),
    @NamedQuery(name = "YvsComContenuDocVente.findByRistourne", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.ristourne = :ristourne"),
    @NamedQuery(name = "YvsComContenuDocVente.findByComission", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.comission = :comission"),
    @NamedQuery(name = "YvsComContenuDocVente.findBySupp", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComContenuDocVente.findByActif", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComContenuDocVente.findByDateContenu", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.dateContenu = :dateContenu"),
    @NamedQuery(name = "YvsComContenuDocVente.findByDateSave", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComContenuDocVente.findByCommentaire", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.commentaire = :commentaire"),
    @NamedQuery(name = "YvsComContenuDocVente.findByNumSerie", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.numSerie = :numSerie"),
    @NamedQuery(name = "YvsComContenuDocVente.findByRabais", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.rabais = :rabais"),
    @NamedQuery(name = "YvsComContenuDocVente.findByPr", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.pr = :pr"),
    @NamedQuery(name = "YvsComContenuDocVente.findByStatut", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsComContenuDocVente.findByPuvMin", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.puvMin = :puvMin"),
    @NamedQuery(name = "YvsComContenuDocVente.findByQuantiteBonus", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.quantiteBonus = :quantiteBonus"),
    @NamedQuery(name = "YvsComContenuDocVente.findByIdReservation", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.idReservation = :idReservation"),
    @NamedQuery(name = "YvsComContenuDocVente.findByPrixTotal", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.prixTotal = :prixTotal"),
    @NamedQuery(name = "YvsComContenuDocVente.findByMouvStock", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.mouvStock = :mouvStock"),
    @NamedQuery(name = "YvsComContenuDocVente.findByQualite", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.qualite = :qualite"),
    @NamedQuery(name = "YvsComContenuDocVente.findByStatutLivree", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.statutLivree = :statutLivree"),
    @NamedQuery(name = "YvsComContenuDocVente.findByTauxRemise", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.tauxRemise = :tauxRemise"),
    @NamedQuery(name = "YvsComContenuDocVente.findByDateUpdate", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComContenuDocVente.findByFacture", query = "SELECT DISTINCT y FROM YvsComContenuDocVente y JOIN FETCH y.conditionnement JOIN FETCH y.article JOIN FETCH y.conditionnement.unite "
            + "LEFT JOIN FETCH y.taxes T JOIN FETCH T.taxe "
            + "WHERE y.docVente = :docVente ORDER BY y.id ASC"),
    @NamedQuery(name = "YvsComContenuDocVente.findQteByArticle", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.article = :article AND y.conditionnement =:unite AND y.docVente = :docVente"),
    @NamedQuery(name = "YvsComContenuDocVente.countQteVendu", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.docVente.typeDoc='FV' AND y.docVente.statut='V' AND y.docVente.enteteDoc=:header AND y.conditionnement=:conditionnement"),
    @NamedQuery(name = "YvsComContenuDocVente.findByDocLierTypeStatutArticleS", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.docVente.documentLie = :docVente AND y.docVente.statut = :statut AND y.docVente.typeDoc = :typeDoc AND y.article = :article AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsComContenuDocVente.findQteByArticle_", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocVente y WHERE y.article = :article AND y.conditionnement = :unite AND y.conditionnementBonus = :unite AND y.docVente = :docVente"),
    @NamedQuery(name = "YvsComContenuDocVente.findQteBonusByFacture", query = "SELECT SUM(y.quantiteBonus) FROM YvsComContenuDocVente y WHERE y.article = :article AND y.conditionnement =:unite AND y.docVente = :docVente"),
    @NamedQuery(name = "YvsComContenuDocVente.findTotalByTypeDocAndHeader", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc = :header AND y.docVente.typeDoc = :typeDoc AND y.docVente.statut = 'V'"),
    @NamedQuery(name = "YvsComContenuDocVente.findTotalByTypeDocAndLivre", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc = :header AND y.docVente.typeDoc = :typeDoc AND y.docVente.statut = 'V' AND y.docVente.statutLivre = :statutL"),
    @NamedQuery(name = "YvsComContenuDocVente.findTotalByTypeDocAndRegle", query = "SELECT SUM(y.prixTotal) FROM YvsComContenuDocVente y WHERE y.docVente.enteteDoc = :header AND y.docVente.typeDoc = :typeDoc AND y.docVente.statut = 'V' AND y.docVente.statutRegle = :statutR"),

    @NamedQuery(name = "YvsComContenuDocVente.findByCalculPr", query = "SELECT y FROM YvsComContenuDocVente y WHERE y.calculPr = :calculPr")})
public class YvsComContenuDocVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_contenu_doc_vente_id_seq", name = "yvs_contenu_doc_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_contenu_doc_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "prix")
    private Double prix;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "taxe")
    private Double taxe;
    @Column(name = "ristourne")
    private Double ristourne;
    @Column(name = "comission")
    private Double comission;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_contenu")
    @Temporal(TemporalType.DATE)
    private Date dateContenu;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "num_serie")
    private String numSerie;
    @Column(name = "rabais")
    private Double rabais;
    @Column(name = "pr")
    private Double pr;
    @Column(name = "statut")
    private String statut;
    @Column(name = "puv_min")
    private Double puvMin;
    @Column(name = "quantite_bonus")
    private Double quantiteBonus;
    @Column(name = "id_reservation")
    private BigInteger idReservation;
    @Column(name = "prix_total")
    private Double prixTotal;
    @Column(name = "mouv_stock")
    private Boolean mouvStock;
    @Column(name = "qualite")
    private BigInteger qualite;
    @Column(name = "statut_livree")
    private Character statutLivree;
    @Column(name = "taux_remise")
    private Double tauxRemise;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "calcul_pr")
    private Boolean calculPr;
    @JoinColumn(name = "article_bonus", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles articleBonus;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "conditionnement_bonus", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnementBonus;
    @JoinColumn(name = "depot_livraison_prevu", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepots depotLivraisonPrevu;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private YvsComContenuDocVente parent;
    @JoinColumn(name = "doc_vente", referencedColumnName = "id")
    @ManyToOne
    private YvsComDocVentes docVente;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "contenu")
    private List<YvsComTaxeContenuVente> taxes;

    public YvsComContenuDocVente() {
    }

    public YvsComContenuDocVente(Long id) {
        this.id = id;
    }

    public YvsComContenuDocVente(YvsComContenuDocVente c) {
        this(c.id);
        actif = c.actif;
        article = c.getArticle();
        articleBonus = c.getArticleBonus();
        calculPr = c.calculPr;
        comission = c.comission;
        commentaire = c.commentaire;
        conditionnement = c.getConditionnement();
        conditionnementBonus = c.getConditionnementBonus();
        dateContenu = c.dateContenu;
        dateSave = c.dateSave;
        dateUpdate = c.dateUpdate;
        depotLivraisonPrevu = c.getDepotLivraisonPrevu();
        docVente = c.getDocVente();
        idDistant = c.idDistant;
        idReservation = c.idReservation;
        numSerie = c.numSerie;
        parent = c.parent;
        pr = c.pr;
        prix = c.prix;
        prixTotal = c.prixTotal;
        puvMin = c.puvMin;
        quantite = c.quantite;
        quantiteBonus = c.quantiteBonus;
        rabais = c.rabais;
        remise = c.remise;
        ristourne = c.ristourne;
        statut = c.statut;
        statutLivree = c.statutLivree;
        taxe = c.taxe;

    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getPrix() {
        return prix != null ? prix : 0;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Double getRemise() {
        return remise != null ? remise : 0;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public Double getTaxe() {
        return taxe != null ? taxe : 0;
    }

    public void setTaxe(Double taxe) {
        this.taxe = taxe;
    }

    public Double getRistourne() {
        return ristourne != null ? ristourne : 0;
    }

    public void setRistourne(Double ristourne) {
        this.ristourne = ristourne;
    }

    public Double getComission() {
        return comission != null ? comission : 0;
    }

    public void setComission(Double comission) {
        this.comission = comission;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Date getDateContenu() {
        return dateContenu;
    }

    public void setDateContenu(Date dateContenu) {
        this.dateContenu = dateContenu;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public Double getRabais() {
        return rabais != null ? rabais : 0;
    }

    public void setRabais(Double rabais) {
        this.rabais = rabais;
    }

    public Double getPr() {
        return pr != null ? pr : 0;
    }

    public void setPr(Double pr) {
        this.pr = pr;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Double getPuvMin() {
        return puvMin != null ? puvMin : 0;
    }

    public void setPuvMin(Double puvMin) {
        this.puvMin = puvMin;
    }

    public Double getQuantiteBonus() {
        return quantiteBonus != null ? quantiteBonus : 0;
    }

    public void setQuantiteBonus(Double quantiteBonus) {
        this.quantiteBonus = quantiteBonus;
    }

    public BigInteger getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(BigInteger idReservation) {
        this.idReservation = idReservation;
    }

    public Double getPrixTotal() {
        return prixTotal != null ? prixTotal : 0;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Boolean getMouvStock() {
        return mouvStock;
    }

    public void setMouvStock(Boolean mouvStock) {
        this.mouvStock = mouvStock;
    }

    public BigInteger getQualite() {
        return qualite;
    }

    public void setQualite(BigInteger qualite) {
        this.qualite = qualite;
    }

    public Character getStatutLivree() {
        return statutLivree != null ? statutLivree : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutLivree(Character statutLivree) {
        this.statutLivree = statutLivree;
    }

    public Double getTauxRemise() {
        return tauxRemise != null ? tauxRemise : 0;
    }

    public void setTauxRemise(Double tauxRemise) {
        this.tauxRemise = tauxRemise;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Boolean getCalculPr() {
        return calculPr;
    }

    public void setCalculPr(Boolean calculPr) {
        this.calculPr = calculPr;
    }

    public YvsBaseArticles getArticleBonus() {
        return articleBonus;
    }

    public void setArticleBonus(YvsBaseArticles articleBonus) {
        this.articleBonus = articleBonus;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsBaseConditionnement getConditionnementBonus() {
        return conditionnementBonus;
    }

    public void setConditionnementBonus(YvsBaseConditionnement conditionnementBonus) {
        this.conditionnementBonus = conditionnementBonus;
    }

    public YvsBaseDepots getDepotLivraisonPrevu() {
        return depotLivraisonPrevu;
    }

    public void setDepotLivraisonPrevu(YvsBaseDepots depotLivraisonPrevu) {
        this.depotLivraisonPrevu = depotLivraisonPrevu;
    }

    public List<YvsComTaxeContenuVente> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsComTaxeContenuVente> taxes) {
        this.taxes = taxes;
    }

    public YvsComContenuDocVente getParent() {
        return parent;
    }

    public void setParent(YvsComContenuDocVente parent) {
        this.parent = parent;
    }

    public YvsComDocVentes getDocVente() {
        return docVente;
    }

    public void setDocVente(YvsComDocVentes docVente) {
        this.docVente = docVente;
    }

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
        if (!(object instanceof YvsComContenuDocVente)) {
            return false;
        }
        YvsComContenuDocVente other = (YvsComContenuDocVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComContenuDocVente[ id=" + id + " ]";
    }

}
