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
@Table(name = "yvs_com_grille_remise")
@NamedQueries({
        @NamedQuery(name = "YvsComGrilleRemise.findAll", query = "SELECT y FROM YvsComGrilleRemise y"),
        @NamedQuery(name = "YvsComGrilleRemise.findById", query = "SELECT y FROM YvsComGrilleRemise y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComGrilleRemise.findByMontantMinimal", query = "SELECT y FROM YvsComGrilleRemise y WHERE y.montantMinimal = :montantMinimal"),
        @NamedQuery(name = "YvsComGrilleRemise.findByMontantMaximal", query = "SELECT y FROM YvsComGrilleRemise y WHERE y.montantMaximal = :montantMaximal"),
        @NamedQuery(name = "YvsComGrilleRemise.findByMontantRemise", query = "SELECT y FROM YvsComGrilleRemise y WHERE y.montantRemise = :montantRemise"),
        @NamedQuery(name = "YvsComGrilleRemise.findByNatureMontant", query = "SELECT y FROM YvsComGrilleRemise y WHERE y.natureMontant = :natureMontant"),
        @NamedQuery(name = "YvsComGrilleRemise.findByBase", query = "SELECT y FROM YvsComGrilleRemise y WHERE y.base = :base"),
        @NamedQuery(name = "YvsComGrilleRemise.findByDateUpdate", query = "SELECT y FROM YvsComGrilleRemise y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComGrilleRemise.findByDateSave", query = "SELECT y FROM YvsComGrilleRemise y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsComGrilleRemise.findByExecuteTrigger", query = "SELECT y FROM YvsComGrilleRemise y WHERE y.executeTrigger = :executeTrigger")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComGrilleRemise implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "montant_minimal")
    private Double montantMinimal;
    @Column(name = "montant_maximal")
    private Double montantMaximal;
    @Column(name = "montant_remise")
    private Double montantRemise;
    @Column(name = "nature_montant")
    private String natureMontant;
    @Column(name = "base")
    private String base;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @JoinColumn(name = "remise", referencedColumnName = "id")
    @ManyToOne
    private YvsComRemise remise;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComGrilleRemise(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComGrilleRemise[ id=" + id + " ]";
    }

}
