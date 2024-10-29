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
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_model_reglement")
@NamedQueries({
        @NamedQuery(name = "YvsBaseModelReglement.findAll", query = "SELECT y FROM YvsBaseModelReglement y "),
        @NamedQuery(name = "YvsBaseModelReglement.findById", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseModelReglement.findByReference", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.reference = :reference"),
        @NamedQuery(name = "YvsBaseModelReglement.findByDescription", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.description = :description"),
        @NamedQuery(name = "YvsBaseModelReglement.findByActif", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.actif = :actif AND y.societe=:societe"),
        @NamedQuery(name = "YvsBaseModelReglement.findByType", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.type = :type"),
        @NamedQuery(name = "YvsBaseModelReglement.findByDateUpdate", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseModelReglement.findByDateSave", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseModelReglement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "reference")
    private String reference;
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "type")
    private Character type;
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

    public YvsBaseModelReglement(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseModelReglement_1[ id=" + id + " ]";
    }

}
