/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro;

import java.lang.reflect.Type;

/**
 *
 * @author LYMYTZ
 */
public class FileDeSynchro {

    private long idListen;
    private String statut;
    private Type type;

    public FileDeSynchro() {
    }

    public FileDeSynchro(long id) {
        this.idListen = id;
    }

    public FileDeSynchro(long id, String statut) {
        this(id);
        this.statut = statut;
    }

    public FileDeSynchro(long id,String statut, Type type) {
        this(id, statut);
        this.type = type;
    }

    public long getIdListen() {
        return idListen;
    }

    public void setIdListen(long idListen) {
        this.idListen = idListen;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (this.idListen ^ (this.idListen >>> 32));
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
        final FileDeSynchro other = (FileDeSynchro) obj;
        if (this.idListen != other.idListen) {
            return false;
        }
        return true;
    }

}
