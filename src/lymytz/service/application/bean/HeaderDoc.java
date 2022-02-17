/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.bean;

import java.util.Objects;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author LENOVO
 */
public class HeaderDoc {

    private SimpleStringProperty date;
    private SimpleStringProperty pointVente;
    private SimpleStringProperty tranche;
    private SimpleBooleanProperty cloturer;
    private SimpleStringProperty dateSave;
    private SimpleLongProperty id;
    private SimpleLongProperty idPlus;

    public HeaderDoc() {
        this.date = new SimpleStringProperty();
        this.dateSave = new SimpleStringProperty();
        this.pointVente = new SimpleStringProperty();
        this.tranche = new SimpleStringProperty();
        this.cloturer = new SimpleBooleanProperty();
        this.id = new SimpleLongProperty();
        this.idPlus = new SimpleLongProperty();

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

    public Boolean getCloturer() {
        return cloturer.get();
    }

    public void setCloturer(Boolean actif) {
        this.cloturer.set(actif);
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public Long getIdPlus() {
        return idPlus.get();
    }

    public void setIdPlus(Long idPlus) {
        this.idPlus.set(idPlus);
    }

    public String getDateSave() {
        return dateSave.get();
    }

    public void setDateSave(String dateSave) {
        this.dateSave.set(dateSave);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id.getValue());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HeaderDoc other = (HeaderDoc) obj;
        if (!Objects.equals(this.id.getValue(), other.id.getValue())) {
            return false;
        }
        return true;
    }
    
    

}
