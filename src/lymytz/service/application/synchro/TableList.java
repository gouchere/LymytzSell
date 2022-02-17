/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro;

import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Admin
 */
public class TableList {

    private StringProperty tableName;
    private StringProperty tableCode;
    private DoubleProperty NbAction;

    public TableList() {
        this.tableName = new SimpleStringProperty();
        this.tableCode = new SimpleStringProperty();
        this.NbAction = new SimpleDoubleProperty();
    }

    public TableList(String tableName, String tableCode) {
        this.tableName = new SimpleStringProperty(tableName);
        this.tableCode = new SimpleStringProperty(tableCode);
        this.NbAction = new SimpleDoubleProperty(0);
    }

    public String getTableName() {
        return tableName.get();
    }

    public void setTableName(String tableName) {
        this.tableName.set(tableName);
    }

    public String getTableCode() {
        return tableCode.get();
    }

    public void setTableCode(String tableCode) {
        this.tableCode.set(tableCode);
    }

    public Double getNbAction() {
        return NbAction.get();
    }

    public void setNbAction(Double NbAction) {
        this.NbAction.set(NbAction);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.tableCode);
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
        final TableList other = (TableList) obj;
        if (!Objects.equals(this.tableCode.getValue(), other.tableCode.getValue())) {
            return false;
        }
        return true;
    }
    
    

}
