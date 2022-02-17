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
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_article_code_barre")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findAll", query = "SELECT y FROM YvsBaseArticleCodeBarre y"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findById", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByCodeBarre", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.codeBarre = :codeBarre"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByDescription", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByDateSave", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByDateUpdate", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.dateUpdate = :dateUpdate")})
public class YvsBaseArticleCodeBarre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_barre")
    private String codeBarre;
    @Column(name = "description")
    private String description;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseArticleCodeBarre() {
    }

    public YvsBaseArticleCodeBarre(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
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
        if (!(object instanceof YvsBaseArticleCodeBarre)) {
            return false;
        }
        YvsBaseArticleCodeBarre other = (YvsBaseArticleCodeBarre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticleCodeBarre[ id=" + id + " ]";
    }
    
}
