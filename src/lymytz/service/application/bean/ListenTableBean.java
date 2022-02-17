/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.bean;

import java.util.Objects;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author LYMYTZ
 */
public final class ListenTableBean {

    private LongProperty id;
    private LongProperty idSource;
    private StringProperty table;
    private StringProperty message;
    private StringProperty dateSave;
    private StringProperty action;

    public ListenTableBean() {
        message = new SimpleStringProperty();
        table = new SimpleStringProperty();
        action = new SimpleStringProperty();
        dateSave = new SimpleStringProperty();
        id = new SimpleLongProperty();
        idSource = new SimpleLongProperty();
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public Long getIdSource() {
        return idSource.getValue();
    }

    public void setIdSource(Long idSource) {
        this.idSource.setValue(idSource);
    }

    public String getTable() {
        return table.get();
    }

    public void setTable(String table) {
        this.table.set(table);
    }

    public String getDateSave() {
        return dateSave.get();
    }

    public void setDateSave(String dateSave) {
        this.dateSave.set(dateSave);
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public String getAction() {
        return action.get();
    }

    public void setAction(String  action) {
        this.action.set(action);
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id.getValue());
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
        final ListenTableBean other = (ListenTableBean) obj;
        if (!Objects.equals(this.id.getValue(), other.id.getValue())) {
            return false;
        }
        return true;
    }

}
