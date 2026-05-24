/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.composant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import com.lymytz.lymytzsell.business.helpers.KeyBoardAction;
import javafx.event.Event;
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
import com.lymytz.lymytzsell.dao.Options;
import com.lymytz.lymytzsell.dao.entity.YvsBaseConditionnement;
import com.lymytz.lymytzsell.dao.entity.YvsBasePointVente;
import com.lymytz.lymytzsell.dao.entity.YvsComContenuDocVente;
import com.lymytz.lymytzsell.dao.entity.YvsComDocVentes;
import com.lymytz.lymytzsell.dao.entity.YvsComRabais;
import com.lymytz.lymytzsell.dao.entity.YvsComTaxeContenuVente;
import com.lymytz.lymytzsell.dao.query.LocalQueryFactories;
import com.lymytz.lymytzsell.service.application.bean.ContentPanier;
import com.lymytz.lymytzsell.service.application.bean.PrixArticles;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.view.component.CustomComponents;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
import lombok.Getter;
import lombok.Setter;

import static com.lymytz.lymytzsell.service.utils.MessagesConstants.ERREUR;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.MODIFICATION_FACTURE_IMPOSSIBLE;

/**
 * @author LENOVO
 */
public final class Onglets extends Tab {

    LocalQueryFactories dao;
    @Getter
    @Setter
    private double netAPayer;
    @Getter
    @Setter
    private double montantRecu;
    @Getter
    @Setter
    private YvsComDocVentes facture;
    @Getter
    @Setter
    private List<ContentPanier> contentFacture;
    HomeCaisseController page;
    private final CheckBox memoireDlg = new CheckBox("Ne plus me rappeler...");

    public Onglets(String title, HomeCaisseController page) {
        super(title, new ScrollPane(new VBox()));
        contentFacture = new ArrayList<>();
        this.page = page;
        dao = new LocalQueryFactories();
    }

    public Onglets(Onglets onglet) {
        this.facture = new YvsComDocVentes(onglet.facture, true);
        this.netAPayer = onglet.netAPayer;
        this.montantRecu = onglet.montantRecu;
        this.contentFacture = new ArrayList<>(onglet.contentFacture);
        this.dao = onglet.dao;
        this.page = onglet.page;
    }


    public Onglets(YvsComDocVentes facture, HomeCaisseController page) {
        this(facture != null ? facture.getId() > 0 ? facture.getNumDoc() : facture.getTypeDoc() + ":" + facture.getClient().getCodeClient() + "-" + facture.getId() : "", page);
        this.facture = facture;
        //ajoute un évènement
        this.setOnSelectionChanged((Event event) -> {
            int idx = page.TAB_FACTURES.getSelectionModel().getSelectedIndex();
            if (idx >= 0 && page.TAB_FACTURES.getTabs().size() > idx) {
                displayMontantsBean();
                page.displayDetailFacture(((Onglets) page.TAB_FACTURES.getTabs().get(idx)).getFacture());
            } else if (idx <= 0) {
                this.page.ECRAN.setText("0");
            }
        });
    }

    public VBox buildLineContent(ContentPanier line) {
        VBox resultBox = new VBox();
        resultBox.setId(line.getConditionnement().getId() + "");
        resultBox.getStyleClass().add("background_art");
        Label textQte = new Label(line.getQuantite() + "");
        textQte.setPadding(new Insets(0, 5, 0, 5));
        eventForLabelQte(textQte, true, this, line);
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
        Label totalLabel = CustomComponents.getLabelMontantTT(line.getMontantTotalTTC());
        totalLabel.setAlignment(Pos.BOTTOM_RIGHT);
        totalLabel.setPrefHeight(90);
        Label deviseLabel = new Label(" Fcfa");
        deviseLabel.setAlignment(Pos.BOTTOM_RIGHT);
        deviseLabel.setPrefHeight(90);
        Label prixLabel = CustomComponents.getLabelN(Constantes.nbf.format(line.getPrix()));
        eventForLabelQte(prixLabel, false, this, line);
        boxInfosArt.getChildren().addAll(CustomComponents.getLabelBold(line.getConditionnement().getArticle().getRefArt() + " (en " + line.getConditionnement().getUnite().getReference() + ")"), new Label(line.getConditionnement().getArticle().getDesignation()));
        HBox hBox = new HBox(10, new Label("P.U :"), prixLabel);
        hBox.setAlignment(Pos.CENTER_LEFT);
        boxInfosArt.getChildren().add(hBox);
        boxInfosArt.getChildren().add(new HBox(10, new Label("Rist. :"), CustomComponents.getLabelN(Constantes.nbf.format(line.getRistourne())), new Label(""), new Label("Remise/Rabais : "), CustomComponents.getLabelN(Constantes.nbf.format(line.getRemise() + line.getRabais()))));
        boxTop.getChildren().addAll(UtilsProject.buildImageProduit("coffee.png"), boxInfosArt, new Label("      "), totalLabel, deviseLabel);
        resultBox.getChildren().addAll(boxTop, boxBottom);
        resultBox.getStyleClass().add("border_bottom");
        return resultBox;
    }

    private ImageView getButtonUpDown(String path, ContentPanier line, String action) {
        ImageView imv = UtilsProject.buildImageProduit(path);
        imv.setFitHeight(22);
        imv.setFitWidth(22);
        imv.setOnMouseClicked(event -> {
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
                    default:
                        throw new IllegalStateException("Unexpected value: " + action);
                }
            } else {
                LymytzService.openAlertDialog("Impossible de modifier la facture déjà validé", "Facture déjà validé", "Impossible de modifier", Alert.AlertType.ERROR);
            }
        });
        return imv;
    }

    public ContentPanier buildLineContentanier(YvsBaseConditionnement cond) {
        ContentPanier contentPanier = new ContentPanier();
        contentPanier.setComission(0d);
        contentPanier.setConditionnement(cond);
        contentPanier.setIdContent(-1);
        contentPanier.setPrix(cond.getPrix());
        contentPanier.setPrixMin(cond.getPrixMin());
        contentPanier.setQuantite(0);
        contentPanier.setMontantTotalTTC(contentPanier.getQuantite() * contentPanier.getPrix());
        return contentPanier;
    }

    public void addLineContent(ContentPanier line, boolean evalueMontant) {
        if (line != null) {
            ScrollPane sp = (ScrollPane) this.getContent();
            VBox content = (VBox) sp.getContent();
            // vérifie si la ligne est déjà dans le panier
            VBox b = retriveLineInCart(line, content);
            if (b != null && line.getQuantite() > 0) {
                int idx = content.getChildren().indexOf(b);
                if (idx >= 0) {
                    content.getChildren().remove(idx);
                    content.getChildren().add(idx, this.buildLineContent(line));
                }
                idx = this.getContentFacture().indexOf(line);
                if (idx >= 0) {
                    this.getContentFacture().set(idx, line);
                }
            } else if (b != null && line.getQuantite() < 0) {
                moveLineContent(line);
            } else if (b == null) {
                content.getChildren().add(0, this.buildLineContent(line));
                this.getContentFacture().add(line);
            }
            //Recalcule les prix...
            if (evalueMontant) {
                displayMontantsBean();
            }
        }
    }

    private VBox retriveLineInCart(ContentPanier line, VBox content) {
        for (Node b : content.getChildren()) {
            if (b.getId().equals("" + line.getConditionnement().getId())) {
                return (VBox) b;
            }
        }
        return null;
    }

    public void moveLineContent(ContentPanier line) {
        if (line != null) {
            ScrollPane sp = (ScrollPane) this.getContent();
            VBox content = (VBox) sp.getContent();
            VBox b = retriveLineInCart(line, content);
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
            addArticleOnFacture(art, 1, false, 0);
        } else {
            LymytzService.openAlertDialog(MODIFICATION_FACTURE_IMPOSSIBLE, ERREUR, "Aucun conditionnement trouvé pour cet article !", Alert.AlertType.ERROR);
        }
    }

    ButtonType re;

    public boolean addArticleOnFacture(YvsBaseConditionnement cond, double qteLine, boolean resetQte, double prixV) {
        Onglets tab = (Onglets) page.TAB_FACTURES.getSelectionModel().getSelectedItem();
        if (isEditableCurrentFacture()) {
            //page.displayPropertyArticle(cond, false);
            ContentPanier line = tab.buildLineContentanier(cond);
            cond.setStock(page.displayStockArticle(cond));
            //contrôle de stock
            if (isStockEnable(cond, qteLine, tab, line)) {
                line = tab.getContentFacture().stream().filter(line::equals).findFirst().orElse(line);
                final var qte = (resetQte) ? qteLine : line.getQuantite() + qteLine;
                addOrRemoveLineInCard(line, qte, prixV);
                IntStream.range(0, tab.getContentFacture().size())
                        .forEach(index -> tab.getContentFacture().get(index).setIdContent(-index));
                readyForNew();
            } else {
                return false;
            }
        } else {
            LymytzService.openAlertDialog("Modification de la facture impossible !", "Erreur ", tab == null ? "Aucune facture n'a été initié!" : "Cette facture n'est plus éditable", Alert.AlertType.ERROR);
        }
        return true;
    }

    private void addOrRemoveLineInCard(ContentPanier line, double quantite, double prixArticle) {
        Onglets currentOnget = (Onglets) page.TAB_FACTURES.getSelectionModel().getSelectedItem();
        if (quantite <= 0) {
            Optional.ofNullable(currentOnget).ifPresent(tab -> tab.moveLineContent(line));
        } else {
            line.setPrix(prixArticle);
            line.setQuantite(quantite);
            ContentPanier cp = evaluePrix(line);
            if (!Boolean.TRUE.equals(UtilsProject.paramVente.getSellLowerPr()) && cp.getPr() >= cp.getPrix()) {
                //On ne vend pas en dessous du pr
                LymytzService.openAlertDialog("Impossible d'ajouter cet article !", ERREUR, "Vous ne pouvez vendre en dessous du prix de revient du produit", Alert.AlertType.ERROR);
            } else {
                Optional.ofNullable(currentOnget).ifPresent(tab -> tab.addLineContent(cp, true));
            }
        }
    }

    private void readyForNew() {
        //Redonne le curseur si on est en mode saisie automatique (saisie par code barre)
        if (!page.CHK_DISPLAY.isSelected()) {
            page.giveFocusAtTxtFind();
        }
    }

    private boolean isStockEnable(YvsBaseConditionnement cond, double qteLine, Onglets tab, ContentPanier line) {
        return (!UtilsProject.REPLICATION && (Constantes.TYPE_FV.equals(tab.getFacture().getTypeDoc()) &&
                (cond.getStock() - qteLine > 0 || canSaveWithoutStock(cond, tab, line)))) ||
                Constantes.TYPE_BCV.equals(tab.getFacture().getTypeDoc())
                || UtilsProject.REPLICATION;
    }

    private boolean isEditableCurrentFacture() {
        Onglets currentOnget = (Onglets) page.TAB_FACTURES.getSelectionModel().getSelectedItem();
        return Optional.ofNullable(currentOnget)
                .map(Onglets::getFacture)
                .filter(currentFacture -> Constantes.ETAT_EDITABLE.equals(currentFacture.getStatut()))
                .isPresent();
    }

    private boolean canSaveWithoutStock(YvsBaseConditionnement cond, Onglets tab, ContentPanier line) {
        Boolean sellWithoutStock = (Boolean) dao.findOneObjectByNQ("YvsBaseArticleDepot.findIfSellWithOutStock", new String[]{"article", "depot"}, new Object[]{cond.getArticle(), UtilsProject.depotLivraison});
        sellWithoutStock = sellWithoutStock == null || sellWithoutStock;
        if (!sellWithoutStock) {
            LymytzService.openAlertDialog("Insertion impossible!", "Erreur ", "Le stock de cet article est insuffisant dans le dépôt planifié", Alert.AlertType.ERROR);
            return false;
        } else {
            if (!memoireDlg.isSelected()) {
                re = LymytzService.openCustumAlertDialogChoice("Stock insuffisant voulez-vous continuer?", "Stock insuffisant", "L'article vendu est insuffisant en stock", Alert.AlertType.CONFIRMATION, memoireDlg);
            }
            if (re != null && !re.equals(ButtonType.OK)) {
                tab.getContentFacture().remove(line);
                return false;
            }
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
        double taxe = 0;
        double remise = 0;
        double ttc = 0;
        double ristourne = 0;
        for (ContentPanier c : getContentFacture()) {
            net += (c.getMontantTotal());
            taxe += (c.getTaxe());
            remise += (c.getRemise());
            ttc += (c.getMontantTotalTTC());
            ristourne += (c.getRistourne());
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
            page.LAB_T_AVANCE.setText(Constantes.nbf.format(getFacture().getMontantAvance()));
            page.LAB_NET_A_PAYER.setText(Constantes.nbf.format(getFacture().getMontantResteApayer()));
            page.ECRAN.setText(Constantes.nbf.format(net));
        }
    }

    public double soeTaxes(YvsComContenuDocVente c) {
        double montantTaxe = 0;
        for (YvsComTaxeContenuVente t : c.getTaxes()) {
            montantTaxe += t.getMontant();
        }
        return montantTaxe;
    }

    private PrixArticles getPrixArticle(YvsBaseConditionnement c, double qte, YvsComDocVentes facture, double prix) {
        PrixArticles prixArticles = new PrixArticles();
        prixArticles.setArticles(c.getArticle());
        prixArticles.setRabais(getRabais(c, UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint(), UtilsProject.headerDoc.getDateEntete()));
        if (prix <= 0 || Boolean.FALSE.equals(c.getArticle().getChangePrix())) {
            prixArticles.setPrixUnite((Double) dao.findOneObjectBySQLQ("select public.get_puv(?,?,?,?,?,?,?,?,?)", new Options[]{
                    new Options(c.getArticle().getId(), 1), new Options(qte, 2), new Options(c.getPrix(), 3), new Options(facture.getClient().getId(), 4), new Options(0, 5),
                    new Options(UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), 6), new Options(UtilsProject.headerDoc.getDateEntete(), 7), new Options(c.getId(), 8), new Options(false, 9)
            }));
        } else {
            prixArticles.setPrixUnite(prix);
        }
        prixArticles.setRemise((Double) dao.findOneObjectBySQLQ("select public.get_remise_vente(?,?,?,?,?,?,?)", new Options[]{
                new Options(c.getArticle().getId(), 1), new Options(qte, 2), new Options(prixArticles.getPrixUnite(), 3), new Options(facture.getClient().getId(), 4), new Options(UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), 5),
                new Options(UtilsProject.headerDoc.getDateEntete(), 6), new Options(c.getId(), 7)
        }));
        prixArticles.setTaxe((Double) dao.findOneObjectBySQLQ("select public.get_taxe(?,?,?,?,?,?,?)", new Options[]{
                new Options(c.getArticle().getId(), 1), new Options(facture.getCategorieComptable().getId(), 2), new Options(0, 3), new Options(prixArticles.getRemise(), 4), new Options(qte, 5),
                new Options(prixArticles.getPrixUnite(), 6), new Options(true, 7)
        }));
        prixArticles.setRistourne((Double) dao.findOneObjectBySQLQ("select public.get_ristourne(?,?,?,?,?)", new Options[]{
                new Options(c.getId(), 1), new Options(qte, 2), new Options((prixArticles.getPrixUnite() - prixArticles.getRabais()), 3), new Options(facture.getClient().getId(), 4),
                new Options(facture.getEnteteDoc().getDateEntete(), 5)
        }));
        double total = ((prixArticles.getPrixUnite() - prixArticles.getRabais()) * qte) - prixArticles.getRemise();
        total = Boolean.TRUE.equals((c.getArticle().getPuvTtc())) ? total : total + prixArticles.getTaxe();
        prixArticles.setTotalHt(total - prixArticles.getTaxe());
        prixArticles.setTotalTTC(Boolean.TRUE.equals((c.getArticle().getPuvTtc())) ? total : (prixArticles.getTotalHt() + prixArticles.getTaxe()));
        prixArticles.setNetApayer(total);
        return prixArticles;
    }

    private void eventForLabelQte(Label label, boolean qte, Onglets onglet, ContentPanier content) {
        label.getStyleClass().add("catalogue-item-editable-label");
        label.setOnMouseClicked(event -> Optional.of(event).map(MouseEvent::getButton).filter(MouseButton.PRIMARY::equals).ifPresent(btn -> {
            if (event.getClickCount() > 1 && !qte) {
                if (Boolean.TRUE.equals(content.getConditionnement().getArticle().getChangePrix())) {
                    page.openDlgCalculatrice(onglet, "F", KeyBoardAction.SET_PRIX, content);
                } else {
                    LymytzService.openAlertDialog("Vous ne pouvez modifier le prix de cet article", "Modification du prix Impossible", "Impossible de modifier le prix de cet article", Alert.AlertType.ERROR);
                }
            } else {
                page.openDlgCalculatrice(onglet, "F", (qte ? KeyBoardAction.SET_QTE : KeyBoardAction.SET_PRIX), content);
            }
        }));
    }

    private Double getRabais(YvsBaseConditionnement c, YvsBasePointVente p, Date date) {
        double rabais = 0;
        if ((c != null && Constantes.asLong(c.getId())) && (p != null && Constantes.asLong(p.getId()))) {
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
        return Objects.equals(this.facture, other.facture);
    }

}
