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
@Table(name = "yvs_synchro_listen_table")
@NamedQueries({
        @NamedQuery(name = "YvsSynchroListenTable.findAll", query = "SELECT y FROM YvsSynchroListenTable y"),
        @NamedQuery(name = "YvsSynchroListenTable.findById", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.id = :id"),
        @NamedQuery(name = "YvsSynchroListenTable.findNbFailedById", query = "SELECT y.nbFailed FROM YvsSynchroListenTable y WHERE y.id = :id"),
        @NamedQuery(name = "YvsSynchroListenTable.findByActionSource", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.idSource = :idSource AND y.nameTable = :nameTable AND y.actionName = :action"),
        @NamedQuery(name = "YvsSynchroListenTable.findByNameTable", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.nameTable = :nameTable"),
        @NamedQuery(name = "YvsSynchroListenTable.findByIdSource", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.idSource = :idSource"),
        @NamedQuery(name = "YvsSynchroListenTable.findByDateSave", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsSynchroListenTable.findByToListen", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.toListen = :toListen ORDER BY y.id"),
        @NamedQuery(name = "YvsSynchroListenTable.findByActionName", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.actionName = :actionName"),
        @NamedQuery(name = "YvsSynchroListenTable.findByOrdre", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.ordre = :ordre"),
        @NamedQuery(name = "YvsSynchroListenTable.findByAuthor", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.author = :author")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsSynchroListenTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "name_table")
    private String nameTable;
    @Column(name = "id_source")
    private Long idSource;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "message")
    private String message;
    @Column(name = "to_listen")
    private Boolean toListen;
    @Column(name = "action_name")
    private String actionName;
    @Basic(optional = false)
    @Column(name = "ordre")
    private long ordre;
    @Column(name = "author")
    private Long author;
    @Column(name = "nb_failed")
    private Integer nbFailed;
    @ManyToOne
    @JoinColumn(name = "serveur", referencedColumnName = "id")
    private YvsSynchroServeurs serveur;

    public YvsSynchroListenTable(Long id) {
        this.id = id;
    }

    public YvsSynchroListenTable(Long id, long ordre) {
        this.id = id;
        this.ordre = ordre;
    }

    public Integer getNbFailed() {
        return nbFailed != null ? nbFailed : 0;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsSynchroListenTable[ id=" + id + " ]";
    }

}
