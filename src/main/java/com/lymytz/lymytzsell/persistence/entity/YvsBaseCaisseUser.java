/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lymytz.lymytzsell.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
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
@Table(name = "yvs_base_caisse_user")
@NamedQueries({
        @NamedQuery(name = "YvsBaseCaisseUser.findAll", query = "SELECT y FROM YvsBaseCaisseUser y"),
        @NamedQuery(name = "YvsBaseCaisseUser.findById", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseCaisseUser.findByUser", query = "SELECT y.idCaisse FROM YvsBaseCaisseUser y WHERE y.idUser = :user"),
        @NamedQuery(name = "YvsBaseCaisseUser.findByDateSave", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseCaisseUser.findByDateUpdate", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseCaisseUser.findByActif", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.actif = :actif")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class YvsBaseCaisseUser implements Serializable {
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
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "id_caisse", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse idCaisse;
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers idUser;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseCaisseUser() {
    }

    public YvsBaseCaisseUser(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseCaisseUser[ id=" + id + " ]";
    }

}
