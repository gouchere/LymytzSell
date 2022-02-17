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
public final class TableBean {

    private StringProperty nameEntity;
    private StringProperty simpleName;
    private StringProperty nameTable;
    private LongProperty nbLocal;

    public TableBean() {
        simpleName = new SimpleStringProperty();
        nameEntity = new SimpleStringProperty();
        nameTable = new SimpleStringProperty();
        nbLocal = new SimpleLongProperty(0L);
    }

    public String getNameEntity() {
        return nameEntity.get();
    }

    public void setNameEntity(String nameEntity) {
        this.nameEntity.setValue(nameEntity);
    }

    public Long getNbLocal() {
        return nbLocal.get();
    }

    public void setNbLocal(Long nbLocal) {
        this.nbLocal.set(nbLocal);
    }

    public String getNameTable() {
        return nameTable.get();
    }

    public void setNameTable(String nameTable) {
        this.nameTable.set(nameTable);
    }

    public String getSimpleName() {
        return simpleName.get();
    }

    public void setSimpleName(String simpleName) {
        this.simpleName.set(simpleName);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.nameTable.getValue());
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
        final TableBean other = (TableBean) obj;
        if (!Objects.equals(this.nameTable.getValue(), other.nameTable.getValue())) {
            return false;
        }
        return true;
    }

}
