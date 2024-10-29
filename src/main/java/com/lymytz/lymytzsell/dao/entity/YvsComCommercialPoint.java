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
@Table(name = "yvs_com_commercial_point")
@NamedQueries({
        @NamedQuery(name = "YvsComCommercialPoint.findAll", query = "SELECT y FROM YvsComCommercialPoint y"),
        @NamedQuery(name = "YvsComCommercialPoint.findById", query = "SELECT y FROM YvsComCommercialPoint y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComCommercialPoint.findByDateSave", query = "SELECT y FROM YvsComCommercialPoint y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsComCommercialPoint.findByDateUpdate", query = "SELECT y FROM YvsComCommercialPoint y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComCommercialPoint.findByExecuteTrigger", query = "SELECT y FROM YvsComCommercialPoint y WHERE y.executeTrigger = :executeTrigger")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComCommercialPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @JoinColumn(name = "point", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePointVente point;
    @JoinColumn(name = "commercial", referencedColumnName = "id")
    @ManyToOne
    private YvsComComerciale commercial;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComCommercialPoint(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComCommercialPoint[ id=" + id + " ]";
    }

}
