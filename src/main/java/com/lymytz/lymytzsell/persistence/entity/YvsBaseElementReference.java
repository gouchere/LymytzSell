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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_element_reference")
@NamedQueries({
    @NamedQuery(name = "YvsBaseElementReference.findAll", query = "SELECT y FROM YvsBaseElementReference y"),
    @NamedQuery(name = "YvsBaseElementReference.findById", query = "SELECT y FROM YvsBaseElementReference y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseElementReference.findByDesignation", query = "SELECT y FROM YvsBaseElementReference y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseElementReference.findByModule", query = "SELECT y FROM YvsBaseElementReference y WHERE y.module = :module"),
    @NamedQuery(name = "YvsBaseElementReference.findByDateUpdate", query = "SELECT y FROM YvsBaseElementReference y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseElementReference.findByDateSave", query = "SELECT y FROM YvsBaseElementReference y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseElementReference.findByModelCourant", query = "SELECT y FROM YvsBaseElementReference y WHERE y.modelCourant = :modelCourant"),
    @NamedQuery(name = "YvsBaseElementReference.findByDefaultPrefix", query = "SELECT y FROM YvsBaseElementReference y WHERE y.defaultPrefix = :defaultPrefix")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseElementReference implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "designation")
    private String designation;
    @Column(name = "module")
    private String module;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "model_courant")
    private Boolean modelCourant;
    @Column(name = "default_prefix")
    private String defaultPrefix;

    public YvsBaseElementReference(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseElementReference[ id=" + id + " ]";
    }

}
