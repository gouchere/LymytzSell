/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao;

import java.util.Objects;

/**
 *
 * @author Admin
 */
public class ParamOption {

    private Object valeur;
    private String param;
    private String operateur;

    public ParamOption() {
    }

    public ParamOption(String param, Object valeur, String operateur) {
        this.valeur = valeur;
        this.param = param;
        this.operateur = operateur;
    }

    public Object getValeur() {
        return valeur;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
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
        if (!Objects.equals(this.param, other.param)) {
            return false;
        }
        return true;
    }

}
