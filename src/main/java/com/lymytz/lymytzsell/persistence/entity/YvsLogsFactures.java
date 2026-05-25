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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_logs_factures")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class YvsLogsFactures implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_doc")
    private Date dateDoc;
    @Column(name = "vendeur")
    private Long vendeur;
    @Column(name = "creneau")
    private Long creneau;
    @Column(name = "entete")
    private Long entete;
    @Column(name = "content_json", columnDefinition = "jsonb")
    private String contentJson;
}
