/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity.service;

import java.util.Objects;

/**
 *
 * @author Admin Cette classe permet de stocker en mémoire l'ensemble des Classe
 * (Entity) persistante du projet et les noms des tables associées
 * @entity  c'est le nom de l'entité persistent (nom de la classe)
 * @simpleName  nom compréhensible qu'on choisit de donner à cette classe
 * @anotationTable  nom de la table sur laquelle est mappé la base de donnée
 */
public class LymytzEntityClass {

    private String entity;
    private String simpleName;
    private String anotationTable;
    private int equalsField = 1;
    private double NbAction = 1;

    public LymytzEntityClass() {
    }

    public LymytzEntityClass(String entity) {
        this.entity = entity;
    }

    public LymytzEntityClass(String entity, String name, String anotationTable) {
        this.entity = entity;
        this.anotationTable = anotationTable;
        this.simpleName = name;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getAnotationTable() {
        return anotationTable;
    }

    public void setAnotationTable(String anotationTable) {
        this.anotationTable = anotationTable;
    }

    public int getEqualsField() {
        return equalsField;
    }

    public void setEqualsField(int equalsField) {
        this.equalsField = equalsField;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public double getNbAction() {
        return NbAction;
    }

    public void setNbAction(double NbAction) {
        this.NbAction = NbAction;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.anotationTable);
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
        final LymytzEntityClass other = (LymytzEntityClass) obj;
        if (!Objects.equals(this.anotationTable, other.anotationTable)) {
            return false;
        }
        return true;
    }

}
