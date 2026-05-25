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
@Table(name = "yvs_base_users_acces")
@NamedQueries({
        @NamedQuery(name = "YvsBaseUsersAcces.findAll", query = "SELECT y FROM YvsBaseUsersAcces y"),
        @NamedQuery(name = "YvsBaseUsersAcces.findById", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseUsersAcces.findByDateSave", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseUsersAcces.findByDateUpdate", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseUsersAcces.findByExecuteTrigger", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.executeTrigger = :executeTrigger")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBaseUsersAcces implements Serializable {
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
    @JoinColumn(name = "code", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCodeAcces code;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers users;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseUsersAcces(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseUsersAcces[ id=" + id + " ]";
    }

}
