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
@Table(name = "yvs_base_liaison_caisse")
@NamedQueries({
        @NamedQuery(name = "YvsBaseLiaisonCaisse.findAll", query = "SELECT y FROM YvsBaseLiaisonCaisse y"),
        @NamedQuery(name = "YvsBaseLiaisonCaisse.findById", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseLiaisonCaisse.findBySource", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.caisseSource=:source "),
        @NamedQuery(name = "YvsBaseLiaisonCaisse.findByActif", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBaseLiaisonCaisse.findByDateUpdate", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseLiaisonCaisse.findByDateSave", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseLiaisonCaisse implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "caisse_cible", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse caisseCible;
    @JoinColumn(name = "caisse_source", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse caisseSource;

    public YvsBaseLiaisonCaisse(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseLiaisonCaisse[ id=" + id + " ]";
    }

}
