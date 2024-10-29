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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Admin
 */
@Entity
@Table(name = "yvs_synchro_data_synchro")
@NamedQueries({
        @NamedQuery(name = "YvsSynchroDataSynchro.findAll", query = "SELECT y FROM YvsSynchroDataSynchro y"),
        @NamedQuery(name = "YvsSynchroDataSynchro.findById", query = "SELECT y FROM YvsSynchroDataSynchro y WHERE y.id = :id"),
        @NamedQuery(name = "YvsSynchroDataSynchro.findOne", query = "SELECT y FROM YvsSynchroDataSynchro y WHERE y.idListen = :listen AND y.idDistant = :distant AND y.serveur = :serveur"),
        @NamedQuery(name = "YvsSynchroDataSynchro.findByDateSave", query = "SELECT y FROM YvsSynchroDataSynchro y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsSynchroDataSynchro.findIdDistant", query = "SELECT y.idListen.idSource FROM YvsSynchroDataSynchro y WHERE y.idListen.nameTable=:table AND y.idDistant=:idDistant"),
        @NamedQuery(name = "YvsSynchroDataSynchro.findByIdDistant", query = "SELECT y FROM YvsSynchroDataSynchro y WHERE y.idDistant = :idDistant")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsSynchroDataSynchro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_synchro_data_synchro_id_seq", name = "yvs_synchro_data_synchro_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_synchro_data_synchro_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "id_distant")
    private Long idDistant;
    @JoinColumn(name = "id_listen", referencedColumnName = "id")
    @ManyToOne
    private YvsSynchroListenTable idListen;
    @JoinColumn(name = "serveur", referencedColumnName = "id")
    @ManyToOne
    private YvsSynchroServeurs serveur;

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsSynchroDataSynchro[ id=" + id + " ]";
    }

}
