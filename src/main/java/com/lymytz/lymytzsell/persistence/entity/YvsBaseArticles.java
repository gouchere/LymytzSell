/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.persistence.entity;

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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_articles")
@NamedQuery(name = "YvsBaseArticles.findAll", query = "SELECT y FROM YvsBaseArticles y")
@NamedQuery(name = "YvsBaseArticles.findById", query = "SELECT y FROM YvsBaseArticles y WHERE y.id = :id")
@NamedQuery(name = "YvsBaseArticles.findByChangePrix", query = "SELECT y FROM YvsBaseArticles y WHERE y.changePrix = :changePrix")
@NamedQuery(name = "YvsBaseArticles.findByDescription", query = "SELECT y FROM YvsBaseArticles y WHERE y.description = :description")
@NamedQuery(name = "YvsBaseArticles.findByDesignation", query = "SELECT y FROM YvsBaseArticles y WHERE y.designation = :designation")
@NamedQuery(name = "YvsBaseArticles.findByPhoto1", query = "SELECT y FROM YvsBaseArticles y WHERE y.photo1 = :photo1")
@NamedQuery(name = "YvsBaseArticles.findByMasseNet", query = "SELECT y FROM YvsBaseArticles y WHERE y.masseNet = :masseNet")
@NamedQuery(name = "YvsBaseArticles.findByRefArt", query = "SELECT y FROM YvsBaseArticles y WHERE y.refArt = :refArt")
@NamedQuery(name = "YvsBaseArticles.findBySuiviEnStock", query = "SELECT y FROM YvsBaseArticles y WHERE y.suiviEnStock = :suiviEnStock")
@NamedQuery(name = "YvsBaseArticles.findByVisibleEnSynthese", query = "SELECT y FROM YvsBaseArticles y WHERE y.visibleEnSynthese = :visibleEnSynthese")
@NamedQuery(name = "YvsBaseArticles.findByCoefficient", query = "SELECT y FROM YvsBaseArticles y WHERE y.coefficient = :coefficient")
@NamedQuery(name = "YvsBaseArticles.findByService", query = "SELECT y FROM YvsBaseArticles y WHERE y.service = :service")
@NamedQuery(name = "YvsBaseArticles.findByMethodeVal", query = "SELECT y FROM YvsBaseArticles y WHERE y.methodeVal = :methodeVal")
@NamedQuery(name = "YvsBaseArticles.findByActif", query = "SELECT y FROM YvsBaseArticles y WHERE y.actif = :actif")
@NamedQuery(name = "YvsBaseArticles.findByPhoto2", query = "SELECT y FROM YvsBaseArticles y WHERE y.photo2 = :photo2")
@NamedQuery(name = "YvsBaseArticles.findByPhoto3", query = "SELECT y FROM YvsBaseArticles y WHERE y.photo3 = :photo3")
@NamedQuery(name = "YvsBaseArticles.findByCategorie", query = "SELECT y FROM YvsBaseArticles y WHERE y.categorie = :categorie")
@NamedQuery(name = "YvsBaseArticles.findByDureeVie", query = "SELECT y FROM YvsBaseArticles y WHERE y.dureeVie = :dureeVie")
@NamedQuery(name = "YvsBaseArticles.findByDureeGarantie", query = "SELECT y FROM YvsBaseArticles y WHERE y.dureeGarantie = :dureeGarantie")
@NamedQuery(name = "YvsBaseArticles.findByFichier", query = "SELECT y FROM YvsBaseArticles y WHERE y.fichier = :fichier")
@NamedQuery(name = "YvsBaseArticles.findByPuvTtc", query = "SELECT y FROM YvsBaseArticles y WHERE y.puvTtc = :puvTtc")
@NamedQuery(name = "YvsBaseArticles.findByPuaTtc", query = "SELECT y FROM YvsBaseArticles y WHERE y.puaTtc = :puaTtc")
@NamedQuery(name = "YvsBaseArticles.findByDateUpdate", query = "SELECT y FROM YvsBaseArticles y WHERE y.dateUpdate = :dateUpdate")
@NamedQuery(name = "YvsBaseArticles.findByDateSave", query = "SELECT y FROM YvsBaseArticles y WHERE y.dateSave = :dateSave")
@NamedQuery(name = "YvsBaseArticles.findByTypeService", query = "SELECT y FROM YvsBaseArticles y WHERE y.typeService = :typeService")
@NamedQuery(name = "YvsBaseArticles.findByDateLastMvt", query = "SELECT y FROM YvsBaseArticles y WHERE y.dateLastMvt = :dateLastMvt")
@NamedQuery(name = "YvsBaseArticles.findByTauxEcartPr", query = "SELECT y FROM YvsBaseArticles y WHERE y.tauxEcartPr = :tauxEcartPr")
@NamedQuery(name = "YvsBaseArticles.findByExecuteTrigger", query = "SELECT y FROM YvsBaseArticles y WHERE y.executeTrigger = :executeTrigger")
@NamedQuery(name = "YvsBaseArticles.findByTags", query = "SELECT y FROM YvsBaseArticles y WHERE y.tags = :tags")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class YvsBaseArticles implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "change_prix")
    private Boolean changePrix;
    @Column(name = "description")
    private String description;
    @Column(name = "designation")
    private String designation;
    @Column(name = "photo_1")
    private String photo1;
    @Column(name = "masse_net")
    private Double masseNet;
    @Column(name = "ref_art")
    private String refArt;
    @Column(name = "suivi_en_stock")
    private Boolean suiviEnStock;
    @Column(name = "visible_en_synthese")
    private Boolean visibleEnSynthese;
    @Column(name = "coefficient")
    private Double coefficient;
    @Column(name = "service")
    private Boolean service;
    @Column(name = "methode_val")
    private String methodeVal;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "photo_2")
    private String photo2;
    @Column(name = "photo_3")
    private String photo3;
    @Column(name = "categorie")
    private String categorie;
    @Column(name = "duree_vie")
    private Double dureeVie;
    @Column(name = "duree_garantie")
    private Double dureeGarantie;
    @Column(name = "fichier")
    private String fichier;
    @Column(name = "puv_ttc")
    private Boolean puvTtc;
    @Column(name = "pua_ttc")
    private Boolean puaTtc;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "type_service")
    private Character typeService;
    @Column(name = "date_last_mvt")
    @Temporal(TemporalType.DATE)
    private Date dateLastMvt;
    @Column(name = "taux_ecart_pr")
    private Double tauxEcartPr;
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @Column(name = "tags")
    private String tags;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseFamilleArticle famille;

    @Transient
    private List<String> photos;
    @Transient
    private String photo;
    @Transient
    private String bytePhoto1;
    @Transient
    private String bytePhoto2;
    @Transient
    private String bytePhoto3;

    public YvsBaseArticles() {
        photos = new ArrayList<>();
    }

    public YvsBaseArticles(Long id) {
        this.id = id;
    }

    public YvsBaseArticles(Long id, String ref, String designation) {
        this.id = id;
        this.refArt = ref;
        this.designation = designation;
    }

    @XmlTransient
    public List<String> getPhotos() {
        photos.clear();
        if (photo1 != null && !photo1.trim().isEmpty()) {
            photos.add(photo1);
        }
        if (photo2 != null && !photo2.trim().isEmpty()) {
            photos.add(photo2);
        }
        if (photo3 != null && !photo3.trim().isEmpty()) {
            photos.add(photo3);
        }
        return photos;
    }

    @XmlTransient
    public String getPhoto() {
        photo = null;
        if (getPhotos() != null && !photos.isEmpty()) {
            photo = photos.get(0);
        }
        return photo;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticles[ id=" + id + " ]";
    }

}
