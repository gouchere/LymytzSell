/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_articles")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticles.findAll", query = "SELECT y FROM YvsBaseArticles y"),
    @NamedQuery(name = "YvsBaseArticles.findById", query = "SELECT y FROM YvsBaseArticles y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticles.findByChangePrix", query = "SELECT y FROM YvsBaseArticles y WHERE y.changePrix = :changePrix"),
    @NamedQuery(name = "YvsBaseArticles.findByDescription", query = "SELECT y FROM YvsBaseArticles y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseArticles.findByDesignation", query = "SELECT y FROM YvsBaseArticles y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseArticles.findByPhoto1", query = "SELECT y FROM YvsBaseArticles y WHERE y.photo1 = :photo1"),
    @NamedQuery(name = "YvsBaseArticles.findByMasseNet", query = "SELECT y FROM YvsBaseArticles y WHERE y.masseNet = :masseNet"),
    @NamedQuery(name = "YvsBaseArticles.findByRefArt", query = "SELECT y FROM YvsBaseArticles y WHERE y.refArt = :refArt"),
    @NamedQuery(name = "YvsBaseArticles.findBySuiviEnStock", query = "SELECT y FROM YvsBaseArticles y WHERE y.suiviEnStock = :suiviEnStock"),
    @NamedQuery(name = "YvsBaseArticles.findByVisibleEnSynthese", query = "SELECT y FROM YvsBaseArticles y WHERE y.visibleEnSynthese = :visibleEnSynthese"),
    @NamedQuery(name = "YvsBaseArticles.findByCoefficient", query = "SELECT y FROM YvsBaseArticles y WHERE y.coefficient = :coefficient"),
    @NamedQuery(name = "YvsBaseArticles.findByService", query = "SELECT y FROM YvsBaseArticles y WHERE y.service = :service"),
    @NamedQuery(name = "YvsBaseArticles.findByMethodeVal", query = "SELECT y FROM YvsBaseArticles y WHERE y.methodeVal = :methodeVal"),
    @NamedQuery(name = "YvsBaseArticles.findByActif", query = "SELECT y FROM YvsBaseArticles y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseArticles.findByPhoto2", query = "SELECT y FROM YvsBaseArticles y WHERE y.photo2 = :photo2"),
    @NamedQuery(name = "YvsBaseArticles.findByPhoto3", query = "SELECT y FROM YvsBaseArticles y WHERE y.photo3 = :photo3"),
    @NamedQuery(name = "YvsBaseArticles.findByCategorie", query = "SELECT y FROM YvsBaseArticles y WHERE y.categorie = :categorie"),
    @NamedQuery(name = "YvsBaseArticles.findByDureeVie", query = "SELECT y FROM YvsBaseArticles y WHERE y.dureeVie = :dureeVie"),
    @NamedQuery(name = "YvsBaseArticles.findByDureeGarantie", query = "SELECT y FROM YvsBaseArticles y WHERE y.dureeGarantie = :dureeGarantie"),
    @NamedQuery(name = "YvsBaseArticles.findByFichier", query = "SELECT y FROM YvsBaseArticles y WHERE y.fichier = :fichier"),
    @NamedQuery(name = "YvsBaseArticles.findByPuvTtc", query = "SELECT y FROM YvsBaseArticles y WHERE y.puvTtc = :puvTtc"),
    @NamedQuery(name = "YvsBaseArticles.findByPuaTtc", query = "SELECT y FROM YvsBaseArticles y WHERE y.puaTtc = :puaTtc"),
    @NamedQuery(name = "YvsBaseArticles.findByDateUpdate", query = "SELECT y FROM YvsBaseArticles y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseArticles.findByDateSave", query = "SELECT y FROM YvsBaseArticles y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseArticles.findByTypeService", query = "SELECT y FROM YvsBaseArticles y WHERE y.typeService = :typeService"),
    @NamedQuery(name = "YvsBaseArticles.findByDateLastMvt", query = "SELECT y FROM YvsBaseArticles y WHERE y.dateLastMvt = :dateLastMvt"),
    @NamedQuery(name = "YvsBaseArticles.findByTauxEcartPr", query = "SELECT y FROM YvsBaseArticles y WHERE y.tauxEcartPr = :tauxEcartPr"),
    @NamedQuery(name = "YvsBaseArticles.findByExecuteTrigger", query = "SELECT y FROM YvsBaseArticles y WHERE y.executeTrigger = :executeTrigger"),
    @NamedQuery(name = "YvsBaseArticles.findByTags", query = "SELECT y FROM YvsBaseArticles y WHERE y.tags = :tags")})
public class YvsBaseArticles implements Serializable {

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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
//    @JoinColumn(name = "classe1", referencedColumnName = "id")
//    @ManyToOne 
//    private YvsBaseClassesStat classe1;
//    @JoinColumn(name = "classe2", referencedColumnName = "id")
//    @ManyToOne 
//    private YvsBaseClassesStat classe2;
//    
//    @JoinColumn(name = "groupe", referencedColumnName = "id")
//    @ManyToOne 
//    private YvsBaseGroupesArticle groupe;

    @Transient
    private List<String> photos;
    @Transient
    private String photo, bytePhoto1, bytePhoto2, bytePhoto3;

    public YvsBaseArticles() {
        photos = new ArrayList<>();
    }

    public YvsBaseArticles(Long id) {
        this.id = id;
    }
    public YvsBaseArticles(Long id, String ref, String designation) {
        this.id = id;
        this.refArt=ref;
        this.designation=designation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getChangePrix() {
        return changePrix;
    }

    public void setChangePrix(Boolean changePrix) {
        this.changePrix = changePrix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public Double getMasseNet() {
        return masseNet;
    }

    public void setMasseNet(Double masseNet) {
        this.masseNet = masseNet;
    }

    public String getRefArt() {
        return refArt;
    }

    public void setRefArt(String refArt) {
        this.refArt = refArt;
    }

    public Boolean getSuiviEnStock() {
        return suiviEnStock;
    }

    public void setSuiviEnStock(Boolean suiviEnStock) {
        this.suiviEnStock = suiviEnStock;
    }

    public Boolean getVisibleEnSynthese() {
        return visibleEnSynthese;
    }

    public void setVisibleEnSynthese(Boolean visibleEnSynthese) {
        this.visibleEnSynthese = visibleEnSynthese;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Boolean getService() {
        return service;
    }

    public void setService(Boolean service) {
        this.service = service;
    }

    public String getMethodeVal() {
        return methodeVal;
    }

    public void setMethodeVal(String methodeVal) {
        this.methodeVal = methodeVal;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlTransient
    public List<String> getPhotos() {
        photos.clear();
        if (photo1 != null ? photo1.trim().length() > 0 : false) {
            photos.add(photo1);
        }
        if (photo2 != null ? photo2.trim().length() > 0 : false) {
            photos.add(photo2);
        }
        if (photo3 != null ? photo3.trim().length() > 0 : false) {
            photos.add(photo3);
        }
        return photos;
    }

    @XmlTransient
    public String getPhoto() {
        photo = null;
        if (getPhotos() != null ? !photos.isEmpty() : false) {
            photo = photos.get(0);
        }
        return photo;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getBytePhoto1() {
        return bytePhoto1;
    }

    public void setBytePhoto1(String bytePhoto1) {
        this.bytePhoto1 = bytePhoto1;
    }

    public String getBytePhoto2() {
        return bytePhoto2;
    }

    public void setBytePhoto2(String bytePhoto2) {
        this.bytePhoto2 = bytePhoto2;
    }

    public String getBytePhoto3() {
        return bytePhoto3;
    }

    public void setBytePhoto3(String bytePhoto3) {
        this.bytePhoto3 = bytePhoto3;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Double getDureeVie() {
        return dureeVie;
    }

    public void setDureeVie(Double dureeVie) {
        this.dureeVie = dureeVie;
    }

    public Double getDureeGarantie() {
        return dureeGarantie;
    }

    public void setDureeGarantie(Double dureeGarantie) {
        this.dureeGarantie = dureeGarantie;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public Boolean getPuvTtc() {
        return puvTtc;
    }

    public void setPuvTtc(Boolean puvTtc) {
        this.puvTtc = puvTtc;
    }

    public Boolean getPuaTtc() {
        return puaTtc;
    }

    public void setPuaTtc(Boolean puaTtc) {
        this.puaTtc = puaTtc;
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

    public Character getTypeService() {
        return typeService;
    }

    public void setTypeService(Character typeService) {
        this.typeService = typeService;
    }

    public Date getDateLastMvt() {
        return dateLastMvt;
    }

    public void setDateLastMvt(Date dateLastMvt) {
        this.dateLastMvt = dateLastMvt;
    }

    public Double getTauxEcartPr() {
        return tauxEcartPr;
    }

    public void setTauxEcartPr(Double tauxEcartPr) {
        this.tauxEcartPr = tauxEcartPr;
    }

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

//    public YvsBaseClassesStat getClasse1() {
//        return classe1;
//    }
//
//    public void setClasse1(YvsBaseClassesStat classe1) {
//        this.classe1 = classe1;
//    }
//
//    public YvsBaseClassesStat getClasse2() {
//        return classe2;
//    }
//
//    public void setClasse2(YvsBaseClassesStat classe2) {
//        this.classe2 = classe2;
//    }

    public YvsBaseFamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(YvsBaseFamilleArticle famille) {
        this.famille = famille;
    }

//    public YvsBaseGroupesArticle getGroupe() {
//        return groupe;
//    }
//
//    public void setGroupe(YvsBaseGroupesArticle groupe) {
//        this.groupe = groupe;
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
        if (!(object instanceof YvsBaseArticles)) {
            return false;
        }
        YvsBaseArticles other = (YvsBaseArticles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticles[ id=" + id + " ]";
    }

}
