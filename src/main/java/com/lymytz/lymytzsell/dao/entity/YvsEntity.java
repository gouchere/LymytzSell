package com.lymytz.lymytzsell.dao.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Lymytz Dowes
 */
@Getter
@Setter
public abstract class YvsEntity implements Serializable {

    @Column(name = "execute_trigger")
    private String executeTrigger;
    @Transient
    protected String adresseServeur;

    @Transient
    protected long idDistant;

    public Long getId() {
        return 0L;
    }

    public abstract YvsUsersAgence getAuthor();

}
