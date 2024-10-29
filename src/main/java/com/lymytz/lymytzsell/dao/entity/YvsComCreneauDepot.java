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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_creneau_depot")
@NamedQueries({
        @NamedQuery(name = "YvsComCreneauDepot.findAll", query = "SELECT y FROM YvsComCreneauDepot y"),
        @NamedQuery(name = "YvsComCreneauDepot.findById", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComCreneauDepot.findByActif", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsComCreneauDepot.findByPermanent", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.permanent = :permanent"),
        @NamedQuery(name = "YvsComCreneauDepot.findByDateUpdate", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComCreneauDepot.findByDateSave", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComCreneauDepot implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
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
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepots depot;
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhTrancheHoraire tranche;

    public YvsComCreneauDepot(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComCreneauDepot[ id=" + id + " ]";
    }

}
