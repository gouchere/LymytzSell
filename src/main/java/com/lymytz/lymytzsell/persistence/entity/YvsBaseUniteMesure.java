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

import java.io.Serializable;
import java.util.Date;
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

/**
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_unite_mesure")
@NamedQueries({
        @NamedQuery(name = "YvsBaseUniteMesure.findAll", query = "SELECT y FROM YvsBaseUniteMesure y"),
        @NamedQuery(name = "YvsBaseUniteMesure.findById", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseUniteMesure.findByReference", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.reference = :reference"),
        @NamedQuery(name = "YvsBaseUniteMesure.findByLibelle", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.libelle = :libelle"),
        @NamedQuery(name = "YvsBaseUniteMesure.findByDescription", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.description = :description"),
        @NamedQuery(name = "YvsBaseUniteMesure.findByType", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.type = :type"),
        @NamedQuery(name = "YvsBaseUniteMesure.findByDateUpdate", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseUniteMesure.findByDateSave", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseUniteMesure.findByDefaut", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.defaut = :defaut")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseUniteMesure implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "reference")
    private String reference;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "description")
    private String description;
    @Column(name = "type")
    private String type;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "defaut")
    private Boolean defaut;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;

    public YvsBaseUniteMesure(Long id) {
        this.id = id;
    }

    public YvsBaseUniteMesure(Long id, String ref) {
        this.id = id;
        this.reference = ref;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseUniteMesure[ id=" + id + " ]";
    }

}
