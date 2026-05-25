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
@Table(name = "yvs_base_point_vente_depot")
@NamedQueries({
        @NamedQuery(name = "YvsBasePointVenteDepot.findAll", query = "SELECT y FROM YvsBasePointVenteDepot y"),
        @NamedQuery(name = "YvsBasePointVenteDepot.findById", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBasePointVenteDepot.findIdDepotByPoint", query = "SELECT y.depot.id FROM YvsBasePointVenteDepot y WHERE y.pointVente = :pointVente AND y.actif = TRUE AND y.depot.actif = TRUE"),
        @NamedQuery(name = "YvsBasePointVenteDepot.findByActif", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBasePointVenteDepot.findByPrincipal", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.principal = :principal"),
        @NamedQuery(name = "YvsBasePointVenteDepot.findByDateUpdate", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBasePointVenteDepot.findByDateSave", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBasePointVenteDepot implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "principal")
    private Boolean principal;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "point_vente", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePointVente pointVente;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepots depot;

    public YvsBasePointVenteDepot(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBasePointVenteDepot[ id=" + id + " ]";
    }

}
