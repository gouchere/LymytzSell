    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    package com.lymytz.lymytzsell.service.application.bean;

    import com.lymytz.lymytzsell.dao.entity.YvsBaseConditionnement;
    import com.lymytz.lymytzsell.dao.entity.YvsComDocVentes;
    import javafx.beans.property.DoubleProperty;
    import javafx.beans.property.SimpleDoubleProperty;
    import lombok.Getter;
    import lombok.Setter;

    import java.beans.PropertyChangeSupport;
    import java.util.Date;
    import java.util.Objects;

    /**
     * @author LENOVO
     */
    public class ContentPanier {

        @Setter
        @Getter
        private long idContent;
        @Setter
        @Getter
        private YvsComDocVentes facture;
        @Setter
        @Getter
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
        @Getter
        @Setter
        private Date dateSave;

        @Setter
        @Getter
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
            quantite.addListener((observable, oldValue, newValue) -> setMontantTotal(newValue.doubleValue() * getPrix()));
        }

        public double getQuantite() {
            return quantite.get();
        }

        public void setQuantite(double quantite) {
            this.quantite.set(quantite);
        }

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

        public void setComission(double comission) {
            this.comission.set(comission);
        }

        public double getMontantTotal() {
            return montantTotal.get();
        }

        public void setMontantTotal(double montantTotal) {
            this.montantTotal.set(montantTotal);
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
