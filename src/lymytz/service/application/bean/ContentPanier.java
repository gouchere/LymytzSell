    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.bean;

import java.beans.PropertyChangeSupport;
import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import lymytz.dao.entity.YvsBaseConditionnement;
import lymytz.dao.entity.YvsComDocVentes;

/**
 *
 * @author LENOVO
 */
public class ContentPanier {

    private long idContent;
    private YvsComDocVentes facture;
    private YvsBaseConditionnement conditionnement;
    private final DoubleProperty quantite;
    private final DoubleProperty prix;
    private final DoubleProperty rabais;
    private final DoubleProperty prixMin;
    private final DoubleProperty remise;
    private final DoubleProperty taxe;
    private final DoubleProperty ristourne;
    private final DoubleProperty comission;
    private final DoubleProperty montantTotal;  //Net à payer
    private final DoubleProperty montantTotalHT;
    private final DoubleProperty montantTotalTTC;

    private double pr;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public ContentPanier() {
        this.conditionnement = new YvsBaseConditionnement();
        this.quantite = new SimpleDoubleProperty();
        this.prix = new SimpleDoubleProperty();
        this.rabais = new SimpleDoubleProperty();
        this.prixMin = new SimpleDoubleProperty();
        this.remise = new SimpleDoubleProperty();
        this.ristourne = new SimpleDoubleProperty();
        this.montantTotal = new SimpleDoubleProperty();
        this.comission = new SimpleDoubleProperty();
        this.taxe = new SimpleDoubleProperty();
        this.montantTotalHT = new SimpleDoubleProperty();
        this.montantTotalTTC = new SimpleDoubleProperty();
        quantite.addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setMontantTotal(newValue.doubleValue() * getPrix());
            }
        });
    }

    public long getIdContent() {
        return idContent;
    }

    public void setIdContent(long idContent) {
        this.idContent = idContent;
    }

    public double getQuantite() {
        return quantite.get();
    }

    public void setQuantite(double quantite) {
        this.quantite.set(quantite);
    }

//    public StringProperty getQuantiteP() {
//        return quantite;
//    }
    public double getPrix() {
        return prix.get();
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }

    public double getRabais() {
        return rabais.get();
    }

    public void setRabais(double prix) {
        this.rabais.set(prix);
    }

    public double getRemise() {
        return remise.get();
    }

    public void setRemise(double remise) {
        this.remise.set(remise);
    }

    public double getRistourne() {
        return ristourne.get();
    }

    public void setRistourne(double ristourne) {
        this.ristourne.set(ristourne);
    }

    public double getComission() {
        return comission.get();
    }

    public void setComission(double comission) {
        this.comission.set(comission);
    }

    public double getMontantTotal() {
        return montantTotal.get();
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal.set(montantTotal);
    }

    public double getPr() {
        return pr;
    }

    public void setPr(double pr) {
        this.pr = pr;
    }

    public double getTaxe() {
        return taxe.doubleValue();
    }

    public void setTaxe(double taxe) {
        this.taxe.set(taxe);
    }

    public double getPrixMin() {
        return prixMin.get();
    }

    public void setPrixMin(double prixMin) {
        this.prixMin.set(prixMin);
    }

    public double getMontantTotalTTC() {
        return montantTotalTTC.get();
    }

    public void setMontantTotalTTC(double montantTotalTTC) {
        this.montantTotalTTC.set(montantTotalTTC);
    }

    public double getMontantTotalHT() {
        return montantTotalHT.get();
    }

    public void setMontantTotalHT(double montantTotalHT) {
        this.montantTotalHT.set(montantTotalHT);
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsComDocVentes getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocVentes facture) {
        this.facture = facture;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.conditionnement);
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
        final ContentPanier other = (ContentPanier) obj;
        if (!Objects.equals(this.conditionnement, other.conditionnement)) {
            return false;
        }
        return true;
    }

}
