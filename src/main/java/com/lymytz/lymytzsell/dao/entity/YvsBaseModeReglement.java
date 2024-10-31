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
@Table(name = "yvs_base_mode_reglement")
@NamedQueries({
        @NamedQuery(name = "YvsBaseModeReglement.findAll", query = "SELECT y FROM YvsBaseModeReglement y"),
        @NamedQuery(name = "YvsBaseModeReglement.findById", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseModeReglement.findByDesignation", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.designation = :designation"),
        @NamedQuery(name = "YvsBaseModeReglement.findByDescription", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.description = :description"),
        @NamedQuery(name = "YvsBaseModeReglement.findByActif", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.actif = :actif AND y.societe=:societe"),
        @NamedQuery(name = "YvsBaseModeReglement.findByTypeReglement", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.typeReglement = :typeReglement"),
        @NamedQuery(name = "YvsBaseModeReglement.findByDefault", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.typeReglement = :type AND y.actif = :actif AND y.defaultMode = :defaut ORDER BY y.designation"),
        @NamedQuery(name = "YvsBaseModeReglement.findByDefaultMode", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.defaultMode = :defaultMode"),
        @NamedQuery(name = "YvsBaseModeReglement.findByDateUpdate", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsBaseModeReglement.findByDateSave", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsBaseModeReglement.findByNumeroMarchand", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.numeroMarchand = :numeroMarchand"),
        @NamedQuery(name = "YvsBaseModeReglement.findByCodePaiement", query = "SELECT y FROM YvsBaseModeReglement y WHERE y.codePaiement = :codePaiement")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@NoArgsConstructor
public class YvsBaseModeReglement extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "designation")
    private String designation;
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "type_reglement")
    private String typeReglement;
    @Column(name = "default_mode")
    private Boolean defaultMode;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "numero_marchand")
    private String numeroMarchand;
    @Column(name = "code_paiement")
    private String codePaiement;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseModeReglement(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseModeReglement[ id=" + id + " ]";
    }
}
