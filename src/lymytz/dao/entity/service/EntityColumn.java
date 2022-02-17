/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity.service;

import java.lang.reflect.Type;
import java.util.Objects;
import javafx.scene.control.TableColumn;

/**
 *
 * @author Admin
 * Modélise chaque celulle d'une table de la base de données
 * @name désigne le nom logique de la propriéte de l'objet Entity
 * @columnName désigne le nom en base de donnée de la colonne
 * @type le type de données de la colonne
 * @typeDataBase type en base de donnée de la colonne
 * @hasjoinColumn précise si la colonne est une colonne de jointure (clé étrangère)
 * @tableName  Nom de la table 
 * @joinTable table de jointure si la colonne est une 
 * @JoinKey précise sur quelle colonne de @joinTable est réalisé la jointure
 * @columnValue la valeur de la celulle
 */
public class EntityColumn extends TableColumn {

    private String name;
    private String columnName;
    private Type type;
    private String typeDatabase;
    private boolean hasJoinColum;   //indique si la colonne est une jointure
    private String tableName;
    private String joinTable;       // Précise la table sur laquelle est réalisé la jointure lorsque c'est une jointure
    private String joinkey;         // Sur quelle colonne est réalisé la jointure...
    private int colIndex;
    private Object columnValue;     //Valeur de la celulle

    public EntityColumn() {
    }

    public EntityColumn(EntityColumn c) {
        super(c.name);
        this.name = c.name;
        this.columnName = c.columnName;
        this.type = c.type;
        this.columnValue = c.columnValue;
        this.hasJoinColum = c.hasJoinColum;
        this.joinTable = c.joinTable;
        this.joinkey = c.joinkey;
        this.tableName = c.tableName;
        this.typeDatabase = c.typeDatabase;
    }

    public EntityColumn(String name, String tableName, Type type) {
        this.name = name;
        this.columnName = tableName;
        this.type = type;
    }

    public EntityColumn(String columnNane, String name, String tableName, Type type) {
        this.columnName = columnNane;
        this.name = name;
        this.columnName = tableName;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(Object columnValue) {
        this.columnValue = columnValue;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public boolean isHasJoinColum() {
        return hasJoinColum;
    }

    public void setHasJoinColum(boolean hasJoinColum) {
        this.hasJoinColum = hasJoinColum;
    }

    public String getJoinTable() {
        return joinTable;
    }

    public void setJoinTable(String joinTable) {
        this.joinTable = joinTable;
    }

    public String getJoinkey() {
        return joinkey;
    }

    public void setJoinkey(String joinkey) {
        this.joinkey = joinkey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTypeDatabase() {
        return typeDatabase;
    }

    public void setTypeDatabase(String typeDatabase) {
        this.typeDatabase = typeDatabase;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.columnName);
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
        final EntityColumn other = (EntityColumn) obj;
        if (!Objects.equals(this.columnName, other.columnName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return columnValue.toString();
    }

}
