/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsBaseConditionnement;
import lymytz.dao.entity.YvsBasePointVente;
import lymytz.dao.entity.YvsComContenuDocVente;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.entity.YvsComRabais;
import lymytz.dao.entity.YvsComTaxeContenuVente;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.bean.ContentPanier;
import lymytz.service.application.bean.PrixArticles;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.view.component.CustomComponents;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class Onglets extends Tab {

    LQueryFactories dao;
    private double netAPayer;
    private double montantRecu;
    private YvsComDocVentes facture;
    private List<ContentPanier> contentFacture;
    HomeCaisseController page;
    private CheckBox memoireDlg = new CheckBox("Ne plus me rappeler...");

    public Onglets(String title, HomeCaisseController page) {
        super(title, new ScrollPane(new VBox()));
        contentFacture = new ArrayList<>();
        this.page = page;
        dao = new LQueryFactories();
    }

    public Onglets(YvsComDocVentes facture, HomeCaisseController page) {
        this((facture != null ? (facture.getId() > 0 ? facture.getNumDoc() : facture.getClient().getCodeClient() + "-" + facture.getId()) : ""), page);
        this.facture = facture;
        //ajoute un évènement
        this.setOnSelectionChanged(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                int idx = page.TAB_FACTURES.getSelectionModel().getSelectedIndex();
                if (idx >= 0 && page.TAB_FACTURES.getTabs().size() > idx) {
                    displayMontantsBean();
                    page.displayDetailFacture(((Onglets) page.TAB_FACTURES.getTabs().get(idx)).getFacture());
                }
            }
        });
    }

    public List<ContentPanier> getContentFacture() {
        return contentFacture;
    }

    public void setContentFacture(List<ContentPanier> contentFacture) {
        this.contentFacture = contentFacture;
    }

    public double getNetAPayer() {
        return netAPayer;
    }

    public void setNetAPayer(double netAPayer) {
        this.netAPayer = netAPayer;
    }

    public YvsComDocVentes getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocVentes facture) {
        this.facture = facture;
    }

    public double getMontantRecu() {
        return montantRecu;
    }

    public void setMontantRecu(double montantRecu) {
        this.montantRecu = montantRecu;
    }

    public VBox buildLineContent(ContentPanier line) {
        VBox resultBox = new VBox();
        resultBox.setId(line.getConditionnement().getId() + "");
        resultBox.getStyleClass().add("background_art");
        Label textQte = new Label(line.getQuantite() + "");
        textQte.setPadding(new Insets(0, 5, 0, 5));
        eventForLabelQte(textQte, true, this, line);
//        textQte.textProperty().bind(line.getQuantiteP());
        //
        ImageView buttonDel = getButtonUpDown("delete-icon.png", line, "DELETE");
        ImageView buttonAdd = getButtonUpDown("add1.png", line, "ADD");
        ImageView buttonMinus = getButtonUpDown("minus_ico.png", line, "MOVE");
        HBox boxBottom = new HBox(buttonDel);
        boxBottom.getStyleClass().add("border_top");
        HBox boxBottom1 = new HBox(buttonMinus, textQte, buttonAdd);
        boxBottom1.setAlignment(Pos.CENTER_RIGHT);
        boxBottom.setPrefWidth(370);
        boxBottom1.setPrefWidth(330);
        boxBottom.getChildren().add(boxBottom1);

        HBox boxTop = new HBox();
        VBox boxInfosArt = new VBox();
        Label Lref = new Label(line.getConditionnement().getArticle().getRefArt());
        Label Ltotal = CustomComponents.getLabelMontantTT(line.getMontantTotalTTC());
        Ltotal.setAlignment(Pos.BOTTOM_RIGHT);
        Ltotal.setPrefHeight(90);
        Label Ldevise = new Label(" Fcfa");
        Ldevise.setAlignment(Pos.BOTTOM_RIGHT);
        Ldevise.setPrefHeight(90);
        Label Lprix = CustomComponents.getLabelN(Constantes.nbf.format(line.getPrix()));
        eventForLabelQte(Lprix, false, this, line);
        boxInfosArt.getChildren().addAll(CustomComponents.getLabelBold(line.getConditionnement().getArticle().getRefArt()), new Label(line.getConditionnement().getArticle().getDesignation()));
        boxInfosArt.getChildren().addAll(CustomComponents.getLabelBold(line.getConditionnement().getUnite().getReference()));
        boxInfosArt.getChildren().add(new HBox(10, new Label("P.U : "), Lprix,
                new Label("Remise : "), CustomComponents.getLabelN(Constantes.nbf.format(line.getRemise()))
        ));
        boxInfosArt.getChildren().add(new HBox(10, new Label("Rist. :"), CustomComponents.getLabelN(Constantes.nbf.format(line.getRistourne())), new Label(""), new Label("Rabais. : "), CustomComponents.getLabelN(Constantes.nbf.format(line.getRabais()))));
        boxTop.getChildren().addAll(UtilsProject.buildImageProduit("coffee.png"), boxInfosArt, new Label("      "), Ltotal, Ldevise);
        resultBox.getChildren().addAll(boxTop, boxBottom);
        resultBox.getStyleClass().add("border_bottom");
//        resultBox.setPadding(new Insets(3));
        //Initialise les contrôle bouton

        return resultBox;
    }

    private ImageView getButtonUpDown(String path, ContentPanier line, String action) {
        ImageView imv = UtilsProject.buildImageProduit(path);
        imv.setFitHeight(22);
        imv.setFitWidth(22);
        imv.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (!facture.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    switch (action) {
                        case "DELETE":
                            moveLineContent(line);
                            break;
                        case "ADD":
                            addArticleOnFacture(line.getConditionnement(), 1, false, line.getPrix());
                            break;
                        case "MOVE":
                            addArticleOnFacture(line.getConditionnement(), -1, false, line.getPrix());
                            break;
                    }
                } else {
                    LymytzService.openAlertDialog("Impossible de modifier la facture déjà validé", "Facture déjà validé", "Impossible de modifier", Alert.AlertType.ERROR);
                }
            }
        });
        return imv;
    }

    public ContentPanier factorieContent(YvsBaseConditionnement cond) {
        ContentPanier re = new ContentPanier();
        re.setComission(0d);
        re.setConditionnement(cond);
        re.setIdContent(-1);
        re.setPrix(cond.getPrix());
        re.setPrixMin(cond.getPrixMin());
        re.setQuantite(1);
        re.setMontantTotalTTC(re.getQuantite() * re.getPrix());
        return re;
    }

    public void addLineContent(ContentPanier line, boolean evalueMontant) {
        if (line != null) {
            ScrollPane sp = (ScrollPane) this.getContent();
            VBox content = (VBox) sp.getContent();
            // vérifie si la ligne est déjà dans le panier
            VBox b = verifie(line, content);
            if (b != null) {
                if (line.getQuantite() > 0) {
                    int idx = content.getChildren().indexOf(b);
                    if (idx >= 0) {
                        content.getChildren().remove(idx);
                        content.getChildren().add(idx, this.buildLineContent(line));
                    }
                    idx = this.getContentFacture().indexOf(line);
                    if (idx >= 0) {
                        this.getContentFacture().set(idx, line);
                    }
                }else{
                    moveLineContent(line);
                }
            } else {
                content.getChildren().add(0, this.buildLineContent(line));
                this.getContentFacture().add(line);
            }
            //Recalcule les prix...
            if (evalueMontant) {
                displayMontantsBean();
            }
        }
    }

    private VBox verifie(ContentPanier line, VBox content) {
        for (Node b : content.getChildren()) {
            if (((VBox) b).getId().equals("" + line.getConditionnement().getId())) {
                return (VBox) b;
            }
        }
        return null;
    }

    public void moveLineContent(ContentPanier line) {
        if (line != null) {
            ScrollPane sp = (ScrollPane) this.getContent();
            VBox content = (VBox) sp.getContent();
            VBox b = verifie(line, content);
            if (b != null) {
                int idx = this.getContentFacture().indexOf(line);
                if (idx >= 0) {
                    this.getContentFacture().remove(idx);
                    content.getChildren().remove(b);
                    displayMontantsBean();
                }
            }
            this.getContentFacture().remove(line);

        }
    }

    public ContentPanier evaluePrix(ContentPanier line) {
        if (line != null) {
            PrixArticles prix = getPrixArticle(line.getConditionnement(), line.getQuantite(), getFacture(), line.getPrix());
            line.setPr(prix.getPr());
            line.setPrix(prix.getPrixUnite());
            line.setRemise(prix.getRemise());
            line.setRistourne(prix.getRistourne());
            line.setTaxe(prix.getTaxe());
            line.setMontantTotal(prix.getNetApayer());
            line.setMontantTotalHT(prix.getTotalHt());
            line.setMontantTotalTTC(prix.getTotalTTC());
            line.setRabais(prix.getRabais());
            return line;
        }
        return null;
    }

    public void addArticleOnFacture(YvsBaseConditionnement art) {
        if (art != null) {
            if (!addArticleOnFacture(art, 1, false, 0)) {
            }
        } else {
            LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", "Aucun conditionnement trouvé pour cet article !", Alert.AlertType.ERROR);
        }
    }
    ButtonType re;

    public boolean addArticleOnFacture(YvsBaseConditionnement cond, double q, boolean resetQte, double prixV) {
        Onglets tab = (Onglets) page.TAB_FACTURES.getSelectionModel().getSelectedItem();
        if (tab != null ? (tab.getFacture().getStatut().equals(Constantes.ETAT_EDITABLE)) : false) {
            page.displayPropertyArticle(cond, false);
            ContentPanier line = tab.factorieContent(cond);
            //contrôle de stock
            if (!UtilsProject.REPLICATION) {
                if (tab.getFacture().getTypeDoc().equals(Constantes.TYPE_FV)) {
                    /* Contrôle de stock */
                    if (cond.getStock() - q < 0) {
                        Boolean sellWithoutStock = (Boolean) dao.findOneObjectByNQ("YvsBaseArticleDepot.findIfSellWithOutStock", new String[]{"article", "depot"}, new Object[]{cond.getArticle(), UtilsProject.depotLivraison});
                        sellWithoutStock = (sellWithoutStock != null) ? sellWithoutStock : true;
                        if (!sellWithoutStock) {
                            LymytzService.openAlertDialog("Insertion impossible!", "Erreur ", "Le stock de cet article est insuffisant dans le dépôt planifié", Alert.AlertType.ERROR);
                            return false;
                        } else {
                            if (!memoireDlg.isSelected()) {
                                re = LymytzService.openCustumAlertDialogChoice("Stock insuffisant voulez-vous continuer?", "Stock insuffisant", "L'article vendu est insuffisant en stock", Alert.AlertType.CONFIRMATION, memoireDlg);
                            } else {

                            }
                            if (re != null ? !re.equals(ButtonType.OK) : false) {
                                tab.getContentFacture().remove(line);
                                return false;
                            }
                        }
                    }
                }
            }
            double qte = q;
            if (tab.getContentFacture().contains(line)) {
                line = tab.getContentFacture().get(tab.getContentFacture().indexOf(line));
                if (!resetQte) {
                    qte = line.getQuantite() + q;
                } else {
                    qte = q;
                }
            }
            if (qte <= 0) {
                tab.moveLineContent(line);
            } else {
                line.setPrix(prixV);
                line.setQuantite(qte);
                tab.addLineContent(evaluePrix(line), true);
            }
            int i = 1;
            for (ContentPanier c : tab.getContentFacture()) {
                c.setIdContent(-i);
                i++;
            }
            //Redonne le curseur si on est en mode saisie automatique (saisie par code barre)
            if (!page.CHK_DISPLAY.isSelected()) {
                page.giveFocusAtTxtFind();
            }
        } else {
            if (tab == null) {
                LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", "Aucune facture n'a été initié !", Alert.AlertType.ERROR);
                return false;
            } else {
                LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", "Cette facture n'est plus éditable", Alert.AlertType.ERROR);
            }
            return false;
        }
        return true;
    }

    public void loadContentOnView(YvsComDocVentes doc) {
        ContentPanier bean;
        getContentFacture().clear();
        if (doc != null) {
            for (YvsComContenuDocVente c : doc.getContenus()) {
                bean = new ContentPanier();
                bean.setConditionnement(c.getConditionnement());
                bean.setIdContent(c.getId());
                bean.setPr(c.getPr());
                bean.setPrix(c.getPrix());
                bean.setRabais(c.getRabais());
                bean.setPrixMin(c.getPuvMin());
                bean.setQuantite(c.getQuantite());
                bean.setRemise(c.getRemise());
                bean.setRistourne(c.getRistourne());
                bean.setTaxe(soeTaxes(c));
                bean.setMontantTotal(c.getPrixTotal());
                bean.setMontantTotalTTC(c.getPrixTotal());
                bean.setMontantTotalHT(c.getPrixTotal() - bean.getTaxe());
                addLineContent(bean, false);
            }
            //charge les montants        
            displayMontantsBean();
//Charge les autres montant
            setNetAPayer(doc.getMontantResteApayer());
            page.displayDetailFacture(doc);

        }
    }

    public void displayMontantsBean() {
        double net = 0;
        double taxe = 0, remise = 0, ttc = 0, tht = 0, ristourne = 0;
        for (ContentPanier c : getContentFacture()) {
            net += (c.getMontantTotal());
            taxe += (c.getTaxe());
            remise += (c.getRemise());
            ttc += (c.getMontantTotalTTC());
            ristourne += (c.getRistourne());
            tht += (c.getMontantTotalHT());
        }
        if (getFacture() != null) {

            //récupère les avances sur la facture
            Double avance = (Double) dao.findOneObjectByNQ("YvsComptaCaissePieceVente.findByFactureStatutS", new String[]{"facture", "statut"}, new Object[]{getFacture(), Constantes.STATUT_DOC_PAYER});
            getFacture().setMontantAvance((avance != null) ? avance : 0d);
            getFacture().setMontantTaxe(taxe);
            setNetAPayer(net);
            getFacture().setMontantTTC(ttc);
            getFacture().setMontantRistourne(ristourne);
            getFacture().setMontantRemises(remise);
            getFacture().setMontantTaxe(taxe);
            page.LAB_TTC.setText(Constantes.nbf.format(ttc));
            page.LAB_T_REM.setText(Constantes.nbf.format(remise));
            page.LAB_T_RIST.setText(Constantes.nbf.format(ristourne));
//            LAB_T_TAXE.setText(Constantes.nbF.format(taxe));            
            page.LAB_T_AVANCE.setText(Constantes.nbf.format(getFacture().getMontantAvance()));
            page.LAB_NET_A_PAYER.setText(Constantes.nbf.format(getFacture().getMontantResteApayer()));
            page.ECRAN.setText(Constantes.nbf.format(net));
        }
    }

//    public void displayMontants() {
//        new UtilsBean().setMontantTotalDoc(getFacture(), getFacture().getContenus());
//        page.LAB_TTC.setText(Constantes.nbf.format(getFacture().getMontantTTC()));
//        page.LAB_T_REM.setText(Constantes.nbf.format(getFacture().getMontantRemises()));
//        page.LAB_T_RIST.setText(Constantes.nbf.format(getFacture().getMontantRistourne()));
////        LAB_T_TAXE.setText(Constantes.nbf.format(bean.getFacture().getMontantTTC() - bean.getFacture().getMontantHT()));
//        page.LAB_NET_A_PAYER.setText(Constantes.nbf.format(getFacture().getMontantResteApayer()));
//        page.LAB_T_AVANCE.setText(Constantes.nbf.format(getFacture().getMontantAvance()));
////        LAB_T_RESTE.setText(Constantes.nbf.format(bean.getFacture().getMontantResteApayer()));
//    }
    public double soeTaxes(YvsComContenuDocVente c) {
        double re = 0;
        for (YvsComTaxeContenuVente t : c.getTaxes()) {
            re += t.getMontant();
        }
        return re;
    }

    private PrixArticles getPrixArticle(YvsBaseConditionnement c, double qte, YvsComDocVentes facture, double prix) {
        PrixArticles re = new PrixArticles();
        re.setArticles(c.getArticle());
        re.setRabais(getRabais(c, UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint(), UtilsProject.headerDoc.getDateEntete()));
        if (prix <= 0 || !c.getArticle().getChangePrix()) {
            re.setPrixUnite((Double) dao.findOneObjectBySQLQ("select public.get_puv(?,?,?,?,?,?,?,?,?)", new Options[]{
                new Options(c.getArticle().getId(), 1), new Options(qte, 2), new Options(c.getPrix(), 3), new Options(facture.getClient().getId(), 4), new Options(0, 5),
                new Options(UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), 6), new Options(UtilsProject.headerDoc.getDateEntete(), 7), new Options(c.getId(), 8), new Options(false, 9)
            }));
        } else {
            re.setPrixUnite(prix);
        }
        re.setRemise((Double) dao.findOneObjectBySQLQ("select public.get_remise_vente(?,?,?,?,?,?,?)", new Options[]{
            new Options(c.getArticle().getId(), 1), new Options(qte, 2), new Options(re.getPrixUnite(), 3), new Options(facture.getClient().getId(), 4), new Options(UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), 5),
            new Options(UtilsProject.headerDoc.getDateEntete(), 6), new Options(c.getId(), 7)
        }));
        re.setTaxe((Double) dao.findOneObjectBySQLQ("select public.get_taxe(?,?,?,?,?,?,?)", new Options[]{
            new Options(c.getArticle().getId(), 1), new Options(facture.getCategorieComptable().getId(), 2), new Options(0, 3), new Options(re.getRemise(), 4), new Options(qte, 5),
            new Options(re.getPrixUnite(), 6), new Options(true, 7)
        }));
        re.setRistourne((Double) dao.findOneObjectBySQLQ("select public.get_ristourne(?,?,?,?,?)", new Options[]{
            new Options(c.getId(), 1), new Options(qte, 2), new Options((re.getPrixUnite() - re.getRabais()), 3), new Options(facture.getClient().getId(), 4),
            new Options(facture.getEnteteDoc().getDateEntete(), 5)
        }));
        double total = ((re.getPrixUnite() - re.getRabais()) * qte) - re.getRemise();
        total = (c.getArticle().getPuvTtc()) ? total : total + re.getTaxe();
        re.setTotalHt(total - re.getTaxe());
        re.setTotalTTC((c.getArticle().getPuvTtc()) ? total : (re.getTotalHt() + re.getTaxe()));
        re.setNetApayer(total);
        return re;
    }

    private void eventForLabelQte(Label label, boolean qte, Onglets onglet, ContentPanier content) {
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                //si on click avec le bouton gauche de la souris
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    //si double click
                    if (event.getClickCount() > 1) {
                        //ouvre la calculatrice
                        if (!qte) {
                            if (content.getConditionnement().getArticle().getChangePrix()) {
                                page.openDlgCalculatrice(onglet, "F", (qte ? "SET_QTE" : "SET_PRIX"), content);
                            } else {
                                LymytzService.openAlertDialog("Vous ne pouvez modifier le prix de cet article", "Modification du prix Impossible", "Impossible de modifier le prix de cet article", Alert.AlertType.ERROR);
                            }
                        } else {
                            page.openDlgCalculatrice(onglet, "F", (qte ? "SET_QTE" : "SET_PRIX"), content);
                        }
                    }
                }
            }
        });
    }

    private Double getRabais(YvsBaseConditionnement c, YvsBasePointVente p, Date date) {
        double rabais = 0;
        if ((c != null ? Constantes.asLong(c.getId()) : false) && (p != null ? Constantes.asLong(p.getId()) : false)) {
            YvsComRabais r = (YvsComRabais) dao.findOneByNQ("YvsComRabais.findRabais", new String[]{"conditionnement", "point", "date"}, new Object[]{c, p, date});
            if (r != null) {
                rabais = r.getMontant();
            }
        }
        return rabais;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.facture);
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
        final Onglets other = (Onglets) obj;
        if (!Objects.equals(this.facture, other.facture)) {
            return false;
        }
        return true;
    }

}
