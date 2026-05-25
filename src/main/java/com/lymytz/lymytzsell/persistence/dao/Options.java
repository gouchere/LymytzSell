/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.persistence.dao;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Admin
 */
@Setter
@Getter
public class Options {

    private Object valeur;
    private int position;

    public Options() {
    }

    public Options(Object valeur, int position) {
        this.valeur = valeur;
        this.position = position;
    }


}
