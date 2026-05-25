/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.utils;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author LYMYTZ
 */
@Setter
@Getter
public class EtatDoc {

    private String codeEtat;
    private String libelle;

    public EtatDoc(String codeEtat, String libelle) {
        this.codeEtat = codeEtat;
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
    
    
}
