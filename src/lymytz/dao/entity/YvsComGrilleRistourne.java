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
import javax.validation.constraints.Size;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_grille_ristourne")
@NamedQueries({
    @NamedQuery(name = "YvsComGrilleRistourne.findAll", query = "SELECT y FROM YvsComGrilleRistourne y"),
    @NamedQuery(name = "YvsComGrilleRistourne.findById", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByMontantMinimal", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.montantMinimal = :montantMinimal"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByMontantMaximal", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.montantMaximal = :montantMaximal"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByMontantRistourne", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.montantRistourne = :montantRistourne"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByNatureMontant", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.natureMontant = :natureMontant"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByBase", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.base = :base"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByDateUpdate", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByDateSave", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByExecuteTrigger", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.executeTrigger = :executeTrigger")})
public class YvsComGrilleRistourne implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_minimal")
    private Double montantMinimal;
    @Column(name = "montant_maximal")
    private Double montantMaximal;
    @Column(name = "montant_ristourne")
    private Double montantRistourne;
    @Size(max = 2147483647)
    @Column(name = "nature_montant")
    private String natureMontant;
    @Size(max = 2147483647)
    @Column(name = "base")
    private String base;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "ristourne", referencedColumnName = "id")
    @ManyToOne
    private YvsComRistourne ristourne;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComGrilleRistourne() {
    }

    public YvsComGrilleRistourne(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontantMinimal() {
        return montantMinimal;
    }

    public void setMontantMinimal(Double montantMinimal) {
        this.montantMinimal = montantMinimal;
    }

    public Double getMontantMaximal() {
        return montantMaximal;
    }

    public void setMontantMaximal(Double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public Double getMontantRistourne() {
        return montantRistourne;
    }

    public void setMontantRistourne(Double montantRistourne) {
        this.montantRistourne = montantRistourne;
    }

    public String getNatureMontant() {
        return natureMontant;
    }

    public void setNatureMontant(String natureMontant) {
        this.natureMontant = natureMontant;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
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

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsComRistourne getRistourne() {
        return ristourne;
    }

    public void setRistourne(YvsComRistourne ristourne) {
        this.ristourne = ristourne;
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
        if (!(object instanceof YvsComGrilleRistourne)) {
            return false;
        }
        YvsComGrilleRistourne other = (YvsComGrilleRistourne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComGrilleRistourne[ id=" + id + " ]";
    }
    
}
