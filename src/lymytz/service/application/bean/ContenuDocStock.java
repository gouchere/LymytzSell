/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.bean;

import java.util.Date;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author LENOVO
 */
public class ContenuDocStock {

    private SimpleStringProperty refArt;
    private SimpleStringProperty designation;
    private SimpleDoubleProperty qteSortie;
    private SimpleStringProperty uniteSortie;
    private SimpleDoubleProperty qteEntree;
    private SimpleStringProperty uniteEntree;
    private Date date;
    private SimpleStringProperty creno;
    private SimpleDoubleProperty prix;
    private SimpleStringProperty depot;
    private SimpleStringProperty numero;
    private SimpleStringProperty statut;
    private SimpleLongProperty id;
    private boolean displayButtonOption;

    public ContenuDocStock(boolean displayOption) {
        refArt = new SimpleStringProperty();
        designation = new SimpleStringProperty();
        uniteEntree = new SimpleStringProperty();
        uniteSortie = new SimpleStringProperty();
        qteSortie = new SimpleDoubleProperty();
        qteEntree = new SimpleDoubleProperty();
        creno = new SimpleStringProperty();
        depot = new SimpleStringProperty();
        numero = new SimpleStringProperty();
        statut = new SimpleStringProperty();
        prix = new SimpleDoubleProperty();
        id = new SimpleLongProperty();
        this.displayButtonOption = displayOption;
    }

    public ContenuDocStock(String numdoc, boolean displayOption) {
        this(displayOption);
        designation = new SimpleStringProperty(numdoc);
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getRefArt() {
        return refArt.get();
    }

    public void setRefArt(String refArt) {
        this.refArt.set(refArt);
    }

    public String getDesignation() {
        return designation.get();
    }

    public void setDesignation(String designation) {
        this.designation.set(designation);
    }

    public Double getQteSortie() {
        return qteSortie.get();
    }

    public void setQteSortie(Double qteSortie) {
        this.qteSortie.set(qteSortie);
    }

    public String getUniteSortie() {
        return uniteSortie.get();
    }

    public void setUniteSortie(String uniteSortie) {
        this.uniteSortie.set(uniteSortie);
    }

    public Double getQteEntree() {
        return qteEntree.get();
    }

    public void setQteEntree(Double qteEntree) {
        this.qteEntree.set(qteEntree);
    }

    public String getUniteEntree() {
        return uniteEntree.get();
    }

    public void setUniteEntree(String uniteEntree) {
        this.uniteEntree.set(uniteEntree);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCreno() {
        return creno.get();
    }

    public void setCreno(String creno) {
        this.creno.set(creno);
    }

    public Double getPrix() {
        return prix.get();
    }

    public void setPrix(Double prix) {
        this.prix.set(prix);
    }

    public String getDepot() {
        return depot.get();
    }

    public void setDepot(String depot) {
        this.depot.set(depot);
    }

    public String getNumero() {
        return numero.get();
    }

    public void setNumero(String numero) {
        this.numero.set(numero);
    }

    public String getStatut() {
        if (statut.get() != null) {
            return statut.get();
        } else {
//            return Constantes.ETAT_ATTENTE;
        }
        return null;
    }

    public void setStatut(String statut) {
        this.statut.set(statut);
    }

    public boolean isDisplayButtonOption() {
        return displayButtonOption;
    }

    public void setDisplayButtonOption(boolean displayButtonOption) {
        this.displayButtonOption = displayButtonOption;
    }

}
