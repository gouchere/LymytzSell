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
public class LQuery {

    String query;
    String param;

    public LQuery(String query, String param) {
        this.query = query;
        this.param = param;
    }

}
