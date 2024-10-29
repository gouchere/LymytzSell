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
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_commercial_vente")
@NamedQueries({
        @NamedQuery(name = "YvsComCommercialVente.findAll", query = "SELECT y FROM YvsComCommercialVente y"),
        @NamedQuery(name = "YvsComCommercialVente.findById", query = "SELECT y FROM YvsComCommercialVente y WHERE y.id = :id"),
        @NamedQuery(name = "YvsComCommercialVente.findByTaux", query = "SELECT y FROM YvsComCommercialVente y WHERE y.taux = :taux"),
        @NamedQuery(name = "YvsComCommercialVente.findByDateSave", query = "SELECT y FROM YvsComCommercialVente y WHERE y.dateSave = :dateSave"),
        @NamedQuery(name = "YvsComCommercialVente.findByDateUpdate", query = "SELECT y FROM YvsComCommercialVente y WHERE y.dateUpdate = :dateUpdate"),
        @NamedQuery(name = "YvsComCommercialVente.findByAuthor", query = "SELECT y FROM YvsComCommercialVente y WHERE y.author = :author"),
        @NamedQuery(name = "YvsComCommercialVente.findByResponsable", query = "SELECT y FROM YvsComCommercialVente y WHERE y.responsable = :responsable"),
        @NamedQuery(name = "YvsComCommercialVente.findByDiminueCa", query = "SELECT y FROM YvsComCommercialVente y WHERE y.diminueCa = :diminueCa")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor
public class YvsComCommercialVente extends YvsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "responsable")
    private Boolean responsable;
    @Column(name = "diminue_ca")
    private Boolean diminueCa;
    @JoinColumn(name = "commercial", referencedColumnName = "id")
    @ManyToOne
    private YvsComComerciale commercial;
    @JoinColumn(name = "facture", referencedColumnName = "id")
    @ManyToOne
    private YvsComDocVentes facture;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComCommercialVente(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComCommercialVente[ id=" + id + " ]";
    }

}
