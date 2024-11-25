/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.dao.entity.service;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
/**
 *
 * @author Admin Cette classe permet de stocker en mémoire l'ensemble des Classe
 * (Entity) persistante du projet et les noms des tables associées
 * @entity  c'est le nom de l'entité persistent (nom de la classe)
 * @simpleName  nom compréhensible qu'on choisit de donner à cette classe
 * @anotationTable  nom de la table sur laquelle est mappé la base de donnée
 */

@Setter
@Getter
@NoArgsConstructor
public class EntityClass {

    @JacksonXmlProperty(localName = "path")
    private String entity;
    @JacksonXmlProperty(isAttribute = true, localName = "name")
    private String simpleName;
    @JacksonXmlProperty(isAttribute = true, localName = "table")
    private String anotationTable;
    private int equalsField = 1;
    private double NbAction = 1;

    public EntityClass(String entity, String name, String anotationTable) {
        this.entity = entity;
        this.anotationTable = anotationTable;
        this.simpleName = name;
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
        final EntityClass other = (EntityClass) obj;
        if (!Objects.equals(this.anotationTable, other.anotationTable)) {
            return false;
        }
        return true;
    }

}
