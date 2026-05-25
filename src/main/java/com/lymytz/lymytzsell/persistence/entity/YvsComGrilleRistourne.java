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
@Table(name = "yvs_com_grille_ristourne")
@NamedQueries({
        @NamedQuery(name = "YvsComGrilleRistourne.findAll", query = "SELECT y FROM YvsComGrilleRistourne y"),
        @NamedQuery(name = "YvsComGrilleRistourne.findById", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComGrilleRistourne.findByMontantMinimal", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.montantMinimal = :montantMinimal"),
        @NamedQuery(name = "YvsComGrilleRistourne.findByMontantMaximal", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.montantMaximal = :montantMaximal"),
        @NamedQuery(name = "YvsComGrilleRistourne.findByMontantRistourne", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.montantRistourne = :montantRistourne"),
        @NamedQuery(name = "YvsComGrilleRistourne.findByNatureMontant", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.natureMontant = :natureMontant"),
        @NamedQuery(name = "YvsComGrilleRistourne.findByBase", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.base = :base"),
        @NamedQuery(name = "YvsComGrilleRistourne.findByDateUpdate", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComGrilleRistourne.findByDateSave", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsComGrilleRistourne.findByExecuteTrigger", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.executeTrigger = :executeTrigger")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComGrilleRistourne implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_minimal")
    private Double montantMinimal;
    @Column(name = "montant_maximal")
    private Double montantMaximal;
    @Column(name = "montant_ristourne")
    private Double montantRistourne;
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
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;
    @JoinColumn(name = "ristourne", referencedColumnName = "id")
    @ManyToOne
    private YvsComRistourne ristourne;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComGrilleRistourne(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComGrilleRistourne[ id=" + id + " ]";
    }

}
