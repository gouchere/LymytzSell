/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lymytz.lymytzsell.dao.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_remise")
@NamedQueries({
        @NamedQuery(name = "YvsComRemise.findAll", query = "SELECT y FROM YvsComRemise y"),
        @NamedQuery(name = "YvsComRemise.findById", query = "SELECT y FROM YvsComRemise y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComRemise.findByRefRemise", query = "SELECT y FROM YvsComRemise y WHERE y.refRemise = :refRemise"),
        @NamedQuery(name = "YvsComRemise.findByPermanent", query = "SELECT y FROM YvsComRemise y WHERE y.permanent = :permanent"),
        @NamedQuery(name = "YvsComRemise.findByActif", query = "SELECT y FROM YvsComRemise y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsComRemise.findByDateDebut", query = "SELECT y FROM YvsComRemise y WHERE y.dateDebut = :dateDebut"),
        @NamedQuery(name = "YvsComRemise.findByDateFin", query = "SELECT y FROM YvsComRemise y WHERE y.dateFin = :dateFin"),
        @NamedQuery(name = "YvsComRemise.findByDateUpdate", query = "SELECT y FROM YvsComRemise y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComRemise.findByDateSave", query = "SELECT y FROM YvsComRemise y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsComRemise.findByDescription", query = "SELECT y FROM YvsComRemise y WHERE y.description = :description")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComRemise implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "ref_remise")
    private String refRemise;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "remise")
    private List<YvsComGrilleRemise> yvsComGrilleRemiseList;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComRemise(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComRemise[ id=" + id + " ]";
    }

}
