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
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_plan_tarifaire_tranche")
@NamedQueries({
        @NamedQuery(name = "YvsBasePlanTarifaireTranche.findAll", query = "SELECT y FROM YvsBasePlanTarifaireTranche y"),
        @NamedQuery(name = "YvsBasePlanTarifaireTranche.findById", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByBase", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.base = :base"),
        @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByValeurMin", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.valeurMin = :valeurMin"),
        @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByValeurMax", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.valeurMax = :valeurMax"),
        @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByRemise", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.remise = :remise"),
        @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByNatureRemise", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.natureRemise = :natureRemise"),
        @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByPuv", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.puv = :puv"),
        @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByDateUpdate", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByDateSave", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBasePlanTarifaireTranche implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "base")
    private String base;
    @Column(name = "valeur_min")
    private Double valeurMin;
    @Column(name = "valeur_max")
    private Double valeurMax;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "nature_remise")
    private String natureRemise;
    @Column(name = "puv")
    private Double puv;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePlanTarifaire plan;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBasePlanTarifaireTranche(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBasePlanTarifaireTranche[ id=" + id + " ]";
    }

}
