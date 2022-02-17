/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lymytz.service.application.synchro;

/**
 *
 * @author LYMYTZ
 */
public class ParamQuery {
    private String colone;
    private Object value;
    private String operateur;

    public ParamQuery() {
    }

    public ParamQuery(String colone, Object value, String operateur) {
        this.colone = colone;
        this.value = value;
        this.operateur = operateur;
    }

    public String getColone() {
        return colone;
    }

    public void setColone(String colone) {
        this.colone = colone;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }

    @Override
    public String toString() {
        return colone+"="+value;
    }
    
    
}
