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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
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
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
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

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseClassesStat[ id=" + id + " ]";
    }

}
