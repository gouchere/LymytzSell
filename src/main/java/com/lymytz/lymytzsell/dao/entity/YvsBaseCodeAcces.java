/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lymytz.lymytzsell.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_base_code_acces")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCodeAcces.findAll", query = "SELECT y FROM YvsBaseCodeAcces y"),
    @NamedQuery(name = "YvsBaseCodeAcces.findById", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCodeAcces.findByCode", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseCodeAcces.findByDescription", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseCodeAcces.findByDateSave", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseCodeAcces.findByDateUpdate", query = "SELECT y FROM YvsBaseCodeAcces y WHERE y.dateUpdate = :dateUpdate")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class YvsBaseCodeAcces implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "description")
    private String description;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseCodeAcces() {
    }

    public YvsBaseCodeAcces(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseCodeAcces[ id=" + id + " ]";
    }
    
}
