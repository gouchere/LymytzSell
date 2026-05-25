/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "yvs_base_exercice")
@NamedQueries({
        @NamedQuery(name = "YvsBaseExercice.findAll", query = "SELECT y FROM YvsBaseExercice y"),
        @NamedQuery(name = "YvsBaseExercice.findActifByDate", query = "SELECT y FROM YvsBaseExercice y WHERE y.actif = true AND :dateJour BETWEEN y.dateDebut AND y.dateFin"),
        @NamedQuery(name = "YvsBaseExercice.findById", query = "SELECT y FROM YvsBaseExercice y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseExercice.findByReference", query = "SELECT y FROM YvsBaseExercice y WHERE y.reference = :reference"),
        @NamedQuery(name = "YvsBaseExercice.findByDateDebut", query = "SELECT y FROM YvsBaseExercice y WHERE y.dateDebut = :dateDebut"),
        @NamedQuery(name = "YvsBaseExercice.findByDateFin", query = "SELECT y FROM YvsBaseExercice y WHERE y.dateFin = :dateFin"),
        @NamedQuery(name = "YvsBaseExercice.findByActif", query = "SELECT y FROM YvsBaseExercice y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBaseExercice.findByCloturer", query = "SELECT y FROM YvsBaseExercice y WHERE y.cloturer = :cloturer"),
        @NamedQuery(name = "YvsBaseExercice.findByDateUpdate", query = "SELECT y FROM YvsBaseExercice y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseExercice.findByDateSave", query = "SELECT y FROM YvsBaseExercice y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseExercice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "reference")
    private String reference;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "cloturer")
    private Boolean cloturer;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseExercice(Long id) {
        this.id = id;
    }

    public Boolean getActif() {
        return actif != null && actif;
    }

    public Boolean getCloturer() {
        return cloturer != null && cloturer;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseExercice[ id=" + id + " ]";
    }

}
