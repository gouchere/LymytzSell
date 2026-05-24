/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.synchro.ws;

import lombok.Getter;

import java.io.Serializable;

/**
 *
 * @param <T>
 * @author LYMYTZ
 */
@Getter
public class ResultatAction<T extends Serializable> {

    private boolean result = false;
    private int codeInfo;
    private Long idEntity;
    private Long source;
    private String message;
    private String module;
    private String fonctionalite;
    private Object data;
    private T entity;
    private boolean continu = true;

    public ResultatAction() {
    }

    public ResultatAction(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public ResultatAction(int codeInfo, Long idEntity, Long source, String message, String module, String fonctionalite, Object data, T entity) {

    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setCodeInfo(int codeInfo) {
        this.codeInfo = codeInfo;
    }

    public void setIdEntity(Long idEntity) {
        this.idEntity = idEntity;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setFonctionalite(String fonctionalite) {
        this.fonctionalite = fonctionalite;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public void setContinu(boolean continu) {
        this.continu = continu;
    }


}
