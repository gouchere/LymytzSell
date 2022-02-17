/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.bean;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author LYMYTZ
 */
public class Articles {

    private IntegerProperty num;
    private StringProperty reference;
    private StringProperty designation;
    private StringProperty unite;
    private StringProperty categorie;
    private StringProperty famille;
    private StringProperty groupe;
    private StringProperty classe;
    private DoubleProperty pua;
    private DoubleProperty puv;
    private DoubleProperty prixMin;
    private BooleanProperty actif;

    public Articles() {
        num = new SimpleIntegerProperty();
        reference = new SimpleStringProperty();
        designation = new SimpleStringProperty();
        unite = new SimpleStringProperty();
        categorie = new SimpleStringProperty();
        famille = new SimpleStringProperty();
        groupe = new SimpleStringProperty();
        classe = new SimpleStringProperty();
        pua = new SimpleDoubleProperty();
        puv = new SimpleDoubleProperty();
        prixMin = new SimpleDoubleProperty();
        actif = new SimpleBooleanProperty();
    }

    public Integer getNum() {
        return num.get();
    }

    public void setNum(Integer num) {
        this.num.set(num);
    }

    public String getReference() {
        return reference.get();
    }

    public void setReference(String reference) {
        this.reference.set(reference);
    }

    public String getDesignation() {
        return designation.get();
    }

    public void setDesignation(String designation) {
        this.designation.set(designation);
    }

    public String getUnite() {
        return unite.get();
    }

    public void setUnite(String unite) {
        this.unite.set(unite);
    }

    public String getCategorie() {
        return categorie.get();
    }

    public void setCategorie(String categorie) {
        this.categorie.set(categorie);
    }

    public String getFamille() {
        return famille.get();
    }

    public void setFamille(String famille) {
        this.famille.set(famille);
    }

    public String getGroupe() {
        return groupe.get();
    }

    public void setGroupe(String groupe) {
        this.groupe.set(groupe);
    }

    public String getClasse() {
        return classe.get();
    }

    public void setClasse(String classe) {
        this.classe.set(classe);
    }

    public Double getPua() {
        return pua.get();
    }

    public void setPua(Double pua) {
        this.pua.set(pua);
    }

    public Double getPuv() {
        return puv.get();
    }

    public void setPuv(Double puv) {
        this.puv.set(puv);
    }

    public Double getPrixMin() {
        return prixMin.get();
    }

    public void setPrixMin(Double prixMin) {
        this.prixMin.set(prixMin);
    }

    public Boolean getActif() {
        return actif.get();
    }

    public void setActif(Boolean actif) {
        this.actif.set(actif);
    }

}
