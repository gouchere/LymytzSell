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
@Table(name = "yvs_com_categorie_tarifaire")
@NamedQueries({
        @NamedQuery(name = "YvsComCategorieTarifaire.findAll", query = "SELECT y FROM YvsComCategorieTarifaire y"),
        @NamedQuery(name = "YvsComCategorieTarifaire.findById", query = "SELECT y FROM YvsComCategorieTarifaire y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComCategorieTarifaire.findByDateDebut", query = "SELECT y FROM YvsComCategorieTarifaire y WHERE y.dateDebut = :dateDebut"),
        @NamedQuery(name = "YvsComCategorieTarifaire.findByDateFin", query = "SELECT y FROM YvsComCategorieTarifaire y WHERE y.dateFin = :dateFin"),
        @NamedQuery(name = "YvsComCategorieTarifaire.findByPriorite", query = "SELECT y FROM YvsComCategorieTarifaire y WHERE y.priorite = :priorite"),
        @NamedQuery(name = "YvsComCategorieTarifaire.findByActif", query = "SELECT y FROM YvsComCategorieTarifaire y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsComCategorieTarifaire.findByPermanent", query = "SELECT y FROM YvsComCategorieTarifaire y WHERE y.permanent = :permanent"),
        @NamedQuery(name = "YvsComCategorieTarifaire.findByDateUpdate", query = "SELECT y FROM YvsComCategorieTarifaire y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComCategorieTarifaire.findByDateSave", query = "SELECT y FROM YvsComCategorieTarifaire y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComCategorieTarifaire implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "priorite")
    private Integer priorite;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private YvsComClient client;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCategorieClient categorie;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComCategorieTarifaire(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComCategorieTarifaire[ id=" + id + " ]";
    }

}
