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
@Table(name = "yvs_base_point_livraison")
@NamedQueries({
        @NamedQuery(name = "YvsBasePointLivraison.findAll", query = "SELECT y FROM YvsBasePointLivraison y"),
        @NamedQuery(name = "YvsBasePointLivraison.findById", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBasePointLivraison.findByLibelle", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.libelle = :libelle"),
        @NamedQuery(name = "YvsBasePointLivraison.findByDateSave", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBasePointLivraison.findByDateUpdate", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBasePointLivraison.findByExecuteTrigger", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.executeTrigger = :executeTrigger"),
        @NamedQuery(name = "YvsBasePointLivraison.findByTelephone", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.telephone = :telephone"),
        @NamedQuery(name = "YvsBasePointLivraison.findByLieuDit", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.lieuDit = :lieuDit"),
        @NamedQuery(name = "YvsBasePointLivraison.findByDescription", query = "SELECT y FROM YvsBasePointLivraison y WHERE y.description = :description")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsBasePointLivraison implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "lieu_dit")
    private String lieuDit;
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne
    private YvsDictionnaire ville;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBasePointLivraison(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBasePointLivraison[ id=" + id + " ]";
    }

}
