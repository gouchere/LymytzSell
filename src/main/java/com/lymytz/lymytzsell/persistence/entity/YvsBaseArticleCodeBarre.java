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
@Table(name = "yvs_base_article_code_barre")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findAll", query = "SELECT y FROM YvsBaseArticleCodeBarre y"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findById", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByCodeBarre", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.codeBarre = :codeBarre"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByDescription", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByDateSave", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByDateUpdate", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.dateUpdate = :dateUpdate")})
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class YvsBaseArticleCodeBarre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_barre")
    private String codeBarre;
    @Column(name = "description")
    private String description;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnement;

    public YvsBaseArticleCodeBarre() {
    }

    public YvsBaseArticleCodeBarre(Long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "lymytz.dao.entity.YvsBaseArticleCodeBarre[ id=" + id + " ]";
    }
    
}
