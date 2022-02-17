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
public class LQuery {

    String query;
    String param;

    public LQuery() {
    }

    public LQuery(String query, String param) {
        this.query = query;
        this.param = param;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
