/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.bean;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author LENOVO
 */
public class Planning {

    private SimpleStringProperty date;
    private SimpleStringProperty pointVente;
    private SimpleStringProperty tranche;
    private SimpleBooleanProperty actif;
    private SimpleLongProperty id;

    public Planning() {
        this.date = new SimpleStringProperty();
        this.pointVente = new SimpleStringProperty();
        this.tranche = new SimpleStringProperty();
        this.actif = new SimpleBooleanProperty();
        this.id = new SimpleLongProperty();

    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getPointVente() {
        return pointVente.get();
    }

    public void setPointVente(String pointVente) {
        this.pointVente.set(pointVente);
    }

    public String getTranche() {
        return tranche.get();
    }

    public void setTranche(String tranche) {
        this.tranche.set(tranche);
    }

    public Boolean getActif() {
        return actif.get();
    }

    public void setActif(Boolean actif) {
        this.actif.set(actif);
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    @Override
    public String toString() {
        return pointVente.getValue()+" "+tranche.getValue();
    }

}
