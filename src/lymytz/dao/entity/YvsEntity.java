package lymytz.dao.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Transient;

/**
 *
 * @author Lymytz Dowes
 */
public abstract class YvsEntity implements Serializable {

    @Column(name = "execute_trigger")
    private String executeTrigger;
    @Transient
    protected String adresseServeur;

    @Transient
    protected long idDistant;

    public long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(long idDistant) {
        this.idDistant = idDistant;
    }

    public String getAdresseServeur() {
        return adresseServeur;
    }

    public void setAdresseServeur(String adresseServeur) {
        this.adresseServeur = adresseServeur;
    }

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public Long getId() {
        return 0L;
    }

    public abstract YvsUsersAgence getAuthor();

}
