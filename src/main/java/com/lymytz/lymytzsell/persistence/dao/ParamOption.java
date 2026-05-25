/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.persistence.dao;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 *
 * @author Admin
 */
@Setter
@Getter
public class ParamOption {

    private Object valeur;
    private String param;
    private String operateur;

    public ParamOption(String param, Object valeur, String operateur) {
        this.valeur = valeur;
        this.param = param;
        this.operateur = operateur;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.param);
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
        final ParamOption other = (ParamOption) obj;
        return Objects.equals(this.param, other.param);
    }

}
