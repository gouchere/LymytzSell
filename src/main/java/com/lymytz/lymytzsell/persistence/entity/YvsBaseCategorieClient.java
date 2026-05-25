/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
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
 *
 * @author Admin
 */
@Entity
@Table(name = "yvs_base_categorie_client")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCategorieClient.findAll", query = "SELECT y FROM YvsBaseCategorieClient y"),
    @NamedQuery(name = "YvsBaseCategorieClient.findById", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByCode", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByLibelle", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByDescription", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByLierClient", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.lierClient = :lierClient"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByDefaut", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.defaut = :defaut"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByActif", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByDateUpdate", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseCategorieClient.findByDateSave", query = "SELECT y FROM YvsBaseCategorieClient y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class YvsBaseCategorieClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "description")
    private String description;
    @Column(name = "lier_client")
    private Boolean lierClient;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCategorieClient parent;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseCategorieClient() {
    }

    public YvsBaseCategorieClient(Long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseCategorieClient[ id=" + id + " ]";
    }

}
