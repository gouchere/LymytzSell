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
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_com_rabais")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComRabais.findAll", query = "SELECT y FROM YvsComRabais y"),
    @NamedQuery(name = "YvsComRabais.findById", query = "SELECT y FROM YvsComRabais y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComRabais.findRabais", query = "SELECT y FROM YvsComRabais y WHERE y.article.conditionnement=:conditionnement AND y.article.article.point=:point AND y.actif=true AND y.article.actif=true "
            + " AND ((:date BETWEEN y.dateDebut AND y.dateFin AND y.permanent=FALSE)OR y.permanent=TRUE)"),
    @NamedQuery(name = "YvsComRabais.findByMontant", query = "SELECT y FROM YvsComRabais y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComRabais.findByDateDebut", query = "SELECT y FROM YvsComRabais y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsComRabais.findByDateFin", query = "SELECT y FROM YvsComRabais y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsComRabais.findByPermanent", query = "SELECT y FROM YvsComRabais y WHERE y.permanent = :permanent"),
    @NamedQuery(name = "YvsComRabais.findByActif", query = "SELECT y FROM YvsComRabais y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComRabais.findByDateUpdate", query = "SELECT y FROM YvsComRabais y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComRabais.findByDateSave", query = "SELECT y FROM YvsComRabais y WHERE y.dateSave = :dateSave")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class YvsComRabais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnementPoint article;

    public YvsComRabais(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsComRabais[ id=" + id + " ]";
    }

}
