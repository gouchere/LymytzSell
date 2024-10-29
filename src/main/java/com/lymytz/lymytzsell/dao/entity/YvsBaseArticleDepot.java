/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lymytz.lymytzsell.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
import java.io.Serializable;
import java.util.Date;

/**
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
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
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

    public YvsBaseArticleDepot() {
    }

    public YvsBaseArticleDepot(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticleDepot[ id=" + id + " ]";
    }

}
