/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.synchro.ws;

import java.io.Serializable;

/**
 *
 * @author LYMYTZ
 * @param <T>
 */
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

    public ResultatAction(int codeInfo, Long idEntity, Long source, String message, String module, String fonctionalite, Object data, T entity) {
        
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCodeInfo() {
        return codeInfo;
    }

    public void setCodeInfo(int codeInfo) {
        this.codeInfo = codeInfo;
    }

    public Long getIdEntity() {
        return idEntity;
    }

    public void setIdEntity(Long idEntity) {
        this.idEntity = idEntity;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFonctionalite() {
        return fonctionalite;
    }

    public void setFonctionalite(String fonctionalite) {
        this.fonctionalite = fonctionalite;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public boolean isContinu() {
        return continu;
    }

    public void setContinu(boolean continu) {
        this.continu = continu;
    }
    
    
    
    
}
