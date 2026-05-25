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
import javax.persistence.FetchType;
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
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_base_caisse")
@NamedQueries({
        @NamedQuery(name = "YvsBaseCaisse.findAll", query = "SELECT y FROM YvsBaseCaisse y"),
        @NamedQuery(name = "YvsBaseCaisse.findById", query = "SELECT y FROM YvsBaseCaisse y WHERE y.id = :id"),
        @NamedQuery(name = "YvsBaseCaisse.findByIntitule", query = "SELECT y FROM YvsBaseCaisse y WHERE y.intitule = :intitule"),
        @NamedQuery(name = "YvsBaseCaisse.findByAdresse", query = "SELECT y FROM YvsBaseCaisse y WHERE y.adresse = :adresse"),
        @NamedQuery(name = "YvsBaseCaisse.findByActif", query = "SELECT y FROM YvsBaseCaisse y WHERE y.actif = :actif"),
        @NamedQuery(name = "YvsBaseCaisse.findByCanNegative", query = "SELECT y FROM YvsBaseCaisse y WHERE y.canNegative = :canNegative"),
        @NamedQuery(name = "YvsBaseCaisse.findByTypeCaisse", query = "SELECT y FROM YvsBaseCaisse y WHERE y.typeCaisse = :typeCaisse"),
        @NamedQuery(name = "YvsBaseCaisse.findByDefaultCaisse", query = "SELECT y FROM YvsBaseCaisse y WHERE y.defaultCaisse = :defaultCaisse"),
        @NamedQuery(name = "YvsBaseCaisse.findByGiveBilletage", query = "SELECT y FROM YvsBaseCaisse y WHERE y.giveBilletage = :giveBilletage"),
        @NamedQuery(name = "YvsBaseCaisse.findByCode", query = "SELECT y FROM YvsBaseCaisse y WHERE y.code = :code"),
        @NamedQuery(name = "YvsBaseCaisse.findByPrincipal", query = "SELECT y FROM YvsBaseCaisse y WHERE y.principal = :principal"),
        @NamedQuery(name = "YvsBaseCaisse.findByCaissier", query = "SELECT y FROM YvsBaseCaisse y WHERE y.caissier = :caissier ORDER BY y.intitule"),
        @NamedQuery(name = "YvsBaseCaisse.findByExecuteTrigger", query = "SELECT y FROM YvsBaseCaisse y WHERE y.executeTrigger = :executeTrigger")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class YvsBaseCaisse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "intitule")
    private String intitule;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "can_negative")
    private Boolean canNegative;
    @Column(name = "type_caisse")
    private String typeCaisse;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "default_caisse")
    private Boolean defaultCaisse;
    @Column(name = "give_billetage")
    private Boolean giveBilletage;
    @Column(name = "code")
    private String code;
    @Column(name = "principal")
    private Boolean principal;
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse parent;
    @JoinColumn(name = "code_acces", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCodeAcces codeAcces;
    @JoinColumn(name = "mode_reg_defaut", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModeReglement modeRegDefaut;
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers caissier;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "caisseSource", fetch = FetchType.LAZY)
    private List<YvsBaseLiaisonCaisse> caissesLiees;

    public YvsBaseCaisse() {
    }

    public YvsBaseCaisse(YvsBaseCaisse c) {
        super();
        this.id = c.id;
        this.intitule = c.intitule;
        this.actif = c.actif;
        this.adresse = c.adresse;
        this.code = c.code;
        this.codeAcces = c.codeAcces;
        this.giveBilletage = c.giveBilletage;
        this.modeRegDefaut = c.modeRegDefaut;
        this.typeCaisse = c.typeCaisse;
    }

    public YvsBaseCaisse(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseCaisse[ id=" + id + " ]";
    }
}
