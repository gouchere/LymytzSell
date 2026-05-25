/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.persistence.dao.util;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Admin Classe utilisé comme abstraction des données d'une table de la
 * BD
 * @value renvoie au colonnes de la table
 * @action détermine l'action à réaliser sur les données INSERT|UPDATE|DELETE
 * @pKey la valeur de la clé primaire de la ligne
 */
@Getter
@Setter
public class LymytzData {

    private EntityColumn[] value;
    private String action;
    private Long pKey;
    private Long idListen;  //Id de la ligne qui contient l'élément dans la table Listen_data
    private Long idDistant;  //Id de l'élément sur le serveur distant

    public LymytzData() {
    }

    public LymytzData(EntityColumn[] value) {
        this.value = value;
    }

    public LymytzData(EntityColumn[] value, String action) {
        this.value = value;
        this.action = action;
    }
}
