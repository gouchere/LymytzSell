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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "yvs_base_article_depot")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleDepot.findAll", query = "SELECT y FROM YvsBaseArticleDepot y"),
    @NamedQuery(name = "YvsBaseArticleDepot.findById", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByStockMax", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.stockMax = :stockMax"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByStockMin", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.stockMin = :stockMin"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByQuantiteStock", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.quantiteStock = :quantiteStock"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByActif", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByStockAlert", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.stockAlert = :stockAlert"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByMargStockMoyen", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.margStockMoyen = :margStockMoyen"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByStockNet", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.stockNet = :stockNet"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByDateUpdate", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByDateSave", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByRequiereLot", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.requiereLot = :requiereLot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findBySuiviStock", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.suiviStock = :suiviStock"),
    @NamedQuery(name = "YvsBaseArticleDepot.findIfSellWithOutStock", query = "SELECT y.sellWithoutStock FROM YvsBaseArticleDepot y WHERE y.article=:article AND y.depot=:depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByDefaultPr", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.defaultPr = :defaultPr"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByQuantiteVendu", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.quantiteVendu = :quantiteVendu"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByQuantiteAchat", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.quantiteAchat = :quantiteAchat"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByQuantiteProduit", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.quantiteProduit = :quantiteProduit"),
    @NamedQuery(name = "YvsBaseArticleDepot.findDepotActifByArt", query = "SELECT DISTINCT(y.depot) FROM YvsBaseArticleDepot y WHERE y.depot.actif =true AND y.article=:article AND y.actif=true"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByCategorie", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.categorie = :categorie"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByArticleDepot", query = "SELECT y FROM YvsBaseArticleDepot y JOIN FETCH y.article JOIN FETCH y.depot WHERE y.article = :article AND y.depot = :depot"),})
public class YvsBaseArticleDepot implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "stock_max")
    private Double stockMax;
    @Column(name = "stock_min")
    private Double stockMin;
    @Column(name = "quantite_stock")
    private Double quantiteStock;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "stock_alert")
    private Double stockAlert;
    @Column(name = "marg_stock_moyen")
    private Double margStockMoyen;
    @Column(name = "stock_net")
    private Double stockNet;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "requiere_lot")
    private Boolean requiereLot;
    @Column(name = "suivi_stock")
    private Boolean suiviStock;
    @Column(name = "sell_without_stock")
    private Boolean sellWithoutStock;
    @Column(name = "default_pr")
    private Boolean defaultPr;
    @Column(name = "quantite_vendu")
    private Double quantiteVendu;
    @Column(name = "quantite_achat")
    private Double quantiteAchat;
    @Column(name = "quantite_produit")
    private Double quantiteProduit;
    @Column(name = "categorie")
    private String categorie;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepots depot;
    @JoinColumn(name = "depot_pr", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepots depotPr;
    @JoinColumn(name = "default_cond", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement defaultCond;
//    @JoinColumn(name = "author", referencedColumnName = "id")
//    @ManyToOne
//    private YvsUsersAgence author;


    public YvsBaseArticleDepot() {
    }

    public YvsBaseArticleDepot(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getStockMax() {
        return stockMax;
    }

    public void setStockMax(Double stockMax) {
        this.stockMax = stockMax;
    }

    public Double getStockMin() {
        return stockMin;
    }

    public void setStockMin(Double stockMin) {
        this.stockMin = stockMin;
    }

    public Double getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(Double quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Double getStockAlert() {
        return stockAlert;
    }

    public void setStockAlert(Double stockAlert) {
        this.stockAlert = stockAlert;
    }

    public Double getMargStockMoyen() {
        return margStockMoyen;
    }

    public void setMargStockMoyen(Double margStockMoyen) {
        this.margStockMoyen = margStockMoyen;
    }

    public Double getStockNet() {
        return stockNet;
    }

    public void setStockNet(Double stockNet) {
        this.stockNet = stockNet;
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

    public Boolean getRequiereLot() {
        return requiereLot;
    }

    public void setRequiereLot(Boolean requiereLot) {
        this.requiereLot = requiereLot;
    }

    public Boolean getSuiviStock() {
        return suiviStock;
    }

    public void setSuiviStock(Boolean suiviStock) {
        this.suiviStock = suiviStock;
    }

    public Boolean getSellWithoutStock() {
        return sellWithoutStock;
    }

    public void setSellWithoutStock(Boolean sellWithoutStock) {
        this.sellWithoutStock = sellWithoutStock;
    }

    public Boolean getDefaultPr() {
        return defaultPr;
    }

    public void setDefaultPr(Boolean defaultPr) {
        this.defaultPr = defaultPr;
    }

    public Double getQuantiteVendu() {
        return quantiteVendu;
    }

    public void setQuantiteVendu(Double quantiteVendu) {
        this.quantiteVendu = quantiteVendu;
    }

    public Double getQuantiteAchat() {
        return quantiteAchat;
    }

    public void setQuantiteAchat(Double quantiteAchat) {
        this.quantiteAchat = quantiteAchat;
    }

    public Double getQuantiteProduit() {
        return quantiteProduit;
    }

    public void setQuantiteProduit(Double quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    public YvsBaseDepots getDepotPr() {
        return depotPr;
    }

    public void setDepotPr(YvsBaseDepots depotPr) {
        this.depotPr = depotPr;
    }

    public YvsBaseConditionnement getDefaultCond() {
        return defaultCond;
    }

    public void setDefaultCond(YvsBaseConditionnement defaultCond) {
        this.defaultCond = defaultCond;
    }
//
//    public YvsUsersAgence getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(YvsUsersAgence author) {
//        this.author = author;
//    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsBaseArticleDepot)) {
            return false;
        }
        YvsBaseArticleDepot other = (YvsBaseArticleDepot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticleDepot[ id=" + id + " ]";
    }
    
}
