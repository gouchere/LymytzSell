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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_synchro_serveurs")
@NamedQueries({
    @NamedQuery(name = "YvsSynchroServeurs.findAll", query = "SELECT y FROM YvsSynchroServeurs y"),
    @NamedQuery(name = "YvsSynchroServeurs.findById", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSynchroServeurs.findByNomServeur", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.nomServeur = :nomServeur"),
    @NamedQuery(name = "YvsSynchroServeurs.findByAdresseIp", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.adresseIp = :adresseIp"),
    @NamedQuery(name = "YvsSynchroServeurs.findByActif", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsSynchroServeurs.findByOnline", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.online = :online"),
    @NamedQuery(name = "YvsSynchroServeurs.findByDatabase", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.database = :database"),
    @NamedQuery(name = "YvsSynchroServeurs.findByPort", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.port = :port"),
    @NamedQuery(name = "YvsSynchroServeurs.findByUsers", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.users = :users"),
    @NamedQuery(name = "YvsSynchroServeurs.findByPassword", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.password = :password")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsSynchroServeurs implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "nom_serveur")
    private String nomServeur;
    @Column(name = "adresse_ip")
    private String adresseIp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "online")
    private Boolean online;
    @Column(name = "database")
    private String database;
    @Column(name = "port")
    private Integer port;
    @Column(name = "users")
    private String users;
    @Column(name = "password")
    private String password;

    public YvsSynchroServeurs(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsSynchroServeurs[ id=" + id + " ]";
    }
    
}
