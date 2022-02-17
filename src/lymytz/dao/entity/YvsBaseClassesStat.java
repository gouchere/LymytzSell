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
@Table(name = "yvs_base_classes_stat")
@NamedQueries({
    @NamedQuery(name = "YvsBaseClassesStat.findAll", query = "SELECT y FROM YvsBaseClassesStat y"),
    @NamedQuery(name = "YvsBaseClassesStat.findById", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseClassesStat.findByCodeRef", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.codeRef = :codeRef"),
    @NamedQuery(name = "YvsBaseClassesStat.findByDesignation", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseClassesStat.findByActif", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseClassesStat.findByVisibleSynthese", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.visibleSynthese = :visibleSynthese"),
    @NamedQuery(name = "YvsBaseClassesStat.findByVisibleJournal", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.visibleJournal = :visibleJournal"),
    @NamedQuery(name = "YvsBaseClassesStat.findByDateSave", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseClassesStat.findByDateUpdate", query = "SELECT y FROM YvsBaseClassesStat y WHERE y.dateUpdate = :dateUpdate")})
public class YvsBaseClassesStat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_ref")
    private String codeRef;
    @Column(name = "designation")
    private String designation;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "visible_synthese")
    private Boolean visibleSynthese;
    @Column(name = "visible_journal")
    private Boolean visibleJournal;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseClassesStat parent;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseClassesStat() {
    }

    public YvsBaseClassesStat(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Boolean getActif() {
        return actif!=null?actif:false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getVisibleSynthese() {
        return visibleSynthese!=null?visibleSynthese:false;
    }

    public void setVisibleSynthese(Boolean visibleSynthese) {
        this.visibleSynthese = visibleSynthese;
    }

    public Boolean getVisibleJournal() {
        return visibleJournal!=null?visibleJournal:false;
    }

    public void setVisibleJournal(Boolean visibleJournal) {
        this.visibleJournal = visibleJournal;
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

    public YvsBaseClassesStat getParent() {
        return parent;
    }

    public void setParent(YvsBaseClassesStat parent) {
        this.parent = parent;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsBaseClassesStat)) {
            return false;
        }
        YvsBaseClassesStat other = (YvsBaseClassesStat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseClassesStat[ id=" + id + " ]";
    }

}
