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
@Table(name = "yvs_dictionnaire")
@NamedQueries({
    @NamedQuery(name = "YvsDictionnaire.findAll", query = "SELECT y FROM YvsDictionnaire y"),
    @NamedQuery(name = "YvsDictionnaire.findById", query = "SELECT y FROM YvsDictionnaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsDictionnaire.findByLibele", query = "SELECT y FROM YvsDictionnaire y WHERE y.libele = :libele"),
    @NamedQuery(name = "YvsDictionnaire.findByTitre", query = "SELECT y FROM YvsDictionnaire y WHERE y.titre = :titre"),
    @NamedQuery(name = "YvsDictionnaire.findByActif", query = "SELECT y FROM YvsDictionnaire y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsDictionnaire.findByAbreviation", query = "SELECT y FROM YvsDictionnaire y WHERE y.abreviation = :abreviation"),
    @NamedQuery(name = "YvsDictionnaire.findVilles", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.parent WHERE d.titre = 'Villes' ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findAllByParent", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.parent WHERE d.parent = :parent ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findByDateUpdate", query = "SELECT y FROM YvsDictionnaire y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsDictionnaire.findByDateSave", query = "SELECT y FROM YvsDictionnaire y WHERE y.dateSave = :dateSave")})
public class YvsDictionnaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "libele")
    private String libele;
    @Column(name = "titre")
    private String titre;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "abreviation")
    private String abreviation;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private YvsDictionnaire parent;

    public YvsDictionnaire() {
    }

    public YvsDictionnaire(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
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

    public YvsDictionnaire getParent() {
        return parent;
    }

    public void setParent(YvsDictionnaire parent) {
        this.parent = parent;
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
        if (!(object instanceof YvsDictionnaire)) {
            return false;
        }
        YvsDictionnaire other = (YvsDictionnaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return libele;
    }

}
