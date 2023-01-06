/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.utils;

/**
 *
 * @author LYMYTZ
 */
public class EtatDoc {

    private String codeEtat;
    private String libelle;

    public EtatDoc() {
    }

    public EtatDoc(String codeEtat, String libelle) {
        this.codeEtat = codeEtat;
        this.libelle = libelle;
    }

    public String getCodeEtat() {
        return codeEtat;
    }

    public void setCodeEtat(String codeEtat) {
        this.codeEtat = codeEtat;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
    
    
}
