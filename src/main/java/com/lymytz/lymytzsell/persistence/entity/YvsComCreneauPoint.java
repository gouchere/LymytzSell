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
@Table(name = "yvs_com_creneau_point")
@NamedQueries({
        @NamedQuery(name = "YvsComCreneauPoint.findAll", query = "SELECT y FROM YvsComCreneauPoint y"),
        @NamedQuery(name = "YvsComCreneauPoint.findById", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComCreneauPoint.findByActif", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsComCreneauPoint.findByPermanent", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.permanent = :permanent"),
        @NamedQuery(name = "YvsComCreneauPoint.findByDateUpdate", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComCreneauPoint.findByDateSave", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComCreneauPoint implements Serializable {

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
    @JoinColumn(name = "point", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePointVente point;
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhTrancheHoraire tranche;

    public YvsComCreneauPoint(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComCreneauPoint[ id=" + id + " ]";
    }

}
