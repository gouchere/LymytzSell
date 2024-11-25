/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lymytz.lymytzsell.service.application.synchro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author LYMYTZ
 */
@Getter
@Setter
@AllArgsConstructor
public class ParamQuery {
    private String colone;
    private Object value;
    private String operateur;

    @Override
    public String toString() {
        return colone+"="+value;
    }
    
    
}
