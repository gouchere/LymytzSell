/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.bean;

import lymytz.dao.entity.YvsBaseArticles;


/**
 *
 * @author LENOVO
 */
public class PrixArticles {

    private YvsBaseArticles articles;
    private double prixUnite;
    private double remise;
    private double rabais;
    private double ristourne;
    private double pr;
    private double taxe;
    private double totalHt;
    private double totalTTC;
    private double NetApayer;

    public PrixArticles() {
    }

    public YvsBaseArticles getArticles() {
        return articles;
    }

    public void setArticles(YvsBaseArticles articles) {
        this.articles = articles;
    }

    public double getPrixUnite() {
        return prixUnite;
    }

    public void setPrixUnite(double prixUnite) {
        this.prixUnite = prixUnite;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public double getRistourne() {
        return ristourne;
    }

    public void setRistourne(double ristourne) {
        this.ristourne = ristourne;
    }

    public double getTotalHt() {
        return totalHt;
    }

    public void setTotalHt(double totalHt) {
        this.totalHt = totalHt;
    }

    public double getPr() {
        return pr;
    }

    public void setPr(double pr) {
        this.pr = pr;
    }

    public double getTaxe() {
        return taxe;
    }

    public void setTaxe(double taxe) {
        this.taxe = taxe;
    }

    public double getTotalTTC() {
        return totalTTC;
    }

    public void setTotalTTC(double totalTTC) {
        this.totalTTC = totalTTC;
    }

    public double getNetApayer() {
        return NetApayer;
    }

    public void setNetApayer(double NetApayer) {
        this.NetApayer = NetApayer;
    }

    public double getRabais() {
        return rabais;
    }

    public void setRabais(double rabais) {
        this.rabais = rabais;
    }

    
}
