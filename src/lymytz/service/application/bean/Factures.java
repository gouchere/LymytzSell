/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.bean;

import java.util.Date;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import lymytz.dao.entity.YvsComEnteteDocVente;

/**
 *
 * @author LENOVO
 */
public class Factures {

    private Integer numLine;
    private SimpleLongProperty id;
    private SimpleStringProperty numDoc;
    private SimpleStringProperty numPiece;
    private SimpleStringProperty nomClient;
    private SimpleStringProperty categorieClient;
    private SimpleStringProperty categorieComptable;
    private SimpleStringProperty adresse;
    private SimpleBooleanProperty actif;
    private SimpleBooleanProperty toListen;
    private SimpleLongProperty idDistant;
    private SimpleDoubleProperty total;
    private YvsComEnteteDocVente header;
    private Date date;
    private Date heure;
    private Date dateLiv;
    private String type;
    private String statutDoc;
    private String statutLivraison;
    private String statutReglement;

    public Factures() {
        numLine = new Integer(0);
        id = new SimpleLongProperty();
        numDoc = new SimpleStringProperty();
        numPiece = new SimpleStringProperty();
        nomClient = new SimpleStringProperty();
        categorieClient = new SimpleStringProperty();
        categorieComptable = new SimpleStringProperty();
        adresse = new SimpleStringProperty();
        actif = new SimpleBooleanProperty();
        toListen = new SimpleBooleanProperty();
        idDistant = new SimpleLongProperty();
        total = new SimpleDoubleProperty();
        header = new YvsComEnteteDocVente();
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getNumDoc() {
        return numDoc.get();
    }

    public void setNumDoc(String numDoc) {
        this.numDoc.set(numDoc);
    }

    public String getNumPiece() {
        return numPiece.get();
    }

    public void setNumPiece(String numPiece) {
        this.numPiece.set(numPiece);
    }

    public String getNomClient() {
        return nomClient.get();
    }

    public void setNomClient(String nomClient) {
        this.nomClient.set(nomClient);
    }

    public SimpleBooleanProperty getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif.set(actif);
    }

    public YvsComEnteteDocVente getHeader() {
        return header;
    }

    public void setHeader(YvsComEnteteDocVente header) {
        this.header = header;
    }

    public String getStatutDoc() {
        return statutDoc != null ? statutDoc : "";
    }

    public void setStatutDoc(String statutDoc) {
        this.statutDoc = statutDoc;
    }

    public String getStatutLivraison() {
        return statutLivraison != null ? statutLivraison : "";
    }

    public void setStatutLivraison(String statutLivraison) {
        this.statutLivraison = statutLivraison;
    }

    public String getStatutReglement() {
        return statutReglement != null ? statutReglement : "";
    }

    public void setStatutReglement(String statutReglement) {
        this.statutReglement = statutReglement;
    }

    public String getCategorieClient() {
        return categorieClient.get();
    }

    public void setCategorieClient(String categorieClient) {
        this.categorieClient.set(categorieClient);
    }

    public String getAdresse() {
        return adresse.get();
    }

    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }

    public String getCategorieComptable() {
        return categorieComptable.get();
    }

    public void setCategorieComptable(String categorieComptable) {
        this.categorieComptable.set(categorieComptable);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getHeure() {
        return heure;
    }

    public void setHeure(Date heure) {
        this.heure = heure;
    }

    public Date getDateLiv() {
        return dateLiv;
    }

    public void setDateLiv(Date dateLiv) {
        this.dateLiv = dateLiv;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumLine() {
        return numLine;
    }

    public void setNumLine(Integer numLine) {
        this.numLine = numLine;
    }

    public Boolean getToListen() {
        return toListen.get();
    }

    public void setToListen(Boolean toListen) {
        this.toListen.set(toListen);
    }

    public Long getIdDistant() {
        return idDistant.get();
    }

    public void setIdDistant(Long idDistant) {
        this.idDistant.set(idDistant);
    }

    public Double getTotal() {
        return total.get();
    }

    public void setTotal(Double total) {
        this.total.set(total);
    }

}
