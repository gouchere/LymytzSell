/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.component;

import com.lymytz.lymytzsell.dao.entity.YvsBaseConditionnement;
import com.lymytz.lymytzsell.service.application.composant.ButtonArticles;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.view.LocalLoader;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 *
 * @author Admin
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomComponents {
    /**
     * Gestion des tailles et redimensionnement*
     * @param home
     */
    public static void initEventComponents(HomeCaisseController home) {
        home.SPLIT_CENTER.getDividers().get(1).positionProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (home.RIGHT_BOX.getWidth() == 0) {
                home.ECRAN.setPrefWidth(450);
                home.LAB_TITLE_PANIER.setPrefWidth(450);
            } else {
                home.ECRAN.setPrefWidth(home.RIGHT_BOX.getWidth() + 50);
                home.LAB_TITLE_PANIER.setPrefWidth(home.RIGHT_BOX.getWidth() + 50);
            }
        });
    }

    public static void custumMenuAndToolBar(HomeCaisseController home) {
        final ImageView btnSave = new ImageView(new Image(Objects.requireNonNull(CustomComponents.class.getResourceAsStream("/icones/save.png"))));
        final ImageView btnPrint = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/print.png"))));
        final ImageView btnReg = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/money.png"))));
        final ImageView btnLiv = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/cdcopy.png"))));

        home.BTN_SAVE.setGraphic(btnSave);
        home.BTN_SAVE.getStyleClass().add("mes_boutons");
        home.BTN_PRINT.setGraphic(btnPrint);
        home.BTN_REGLER.setGraphic(btnReg);
        home.BTN_LIVRER.setGraphic(btnLiv);

        ImageView imv1 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/12346.png"))));
        ImageView imv2 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/details.png"))));
        ImageView imv3 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/log_out.png"))));
        ImageView imv4 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/compte.png"))));
        ImageView imageViewQuit = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/log_out.png"))));
        ImageView imageViewNew = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/12346.png"))));
        ImageView imageViewCompte = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/compte.png"))));
        ImageView imageViewPreference = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/connect.png"))));

        ImageView imvHelp4 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/ico_server.png"))));
        ImageView imvHelp3 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/ico_server.png"))));
        ImageView imvHelp1 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/about.png"))));
        ImageView imvHelp2 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/manual.png"))));
        //        
        imv1.setFitHeight(Constantes.HEIGHT_ICON_TOOL_BAR);
        imv1.setFitWidth(Constantes.WIDTH_ICON_TOOL_BAR);
        imv2.setFitHeight(Constantes.HEIGHT_ICON_TOOL_BAR);
        imv2.setFitWidth(Constantes.WIDTH_ICON_TOOL_BAR);
        imv3.setFitHeight(Constantes.HEIGHT_ICON_TOOL_BAR);
        imv3.setFitWidth(Constantes.WIDTH_ICON_TOOL_BAR);
        imv4.setFitHeight(Constantes.HEIGHT_ICON_TOOL_BAR);
        imv4.setFitWidth(Constantes.WIDTH_ICON_TOOL_BAR);

        //
        imageViewQuit.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imageViewQuit.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imageViewNew.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imageViewNew.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imageViewCompte.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imageViewCompte.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imageViewPreference.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imageViewPreference.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        //
        imvHelp1.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imvHelp1.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imvHelp2.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imvHelp2.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imvHelp3.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imvHelp3.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imvHelp4.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imvHelp4.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        //
        ImageView imvFich1 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/list.png"))));
        ImageView imvFich2 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/list.png"))));
        ImageView imvFich3 = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/catalogue1.png"))));

        ImageView imageViewImportData = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/import_data.png"))));
        ImageView imageViewExportData = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/export_data.png"))));

        imvFich1.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imvFich1.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imvFich2.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imvFich2.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imvFich3.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imvFich3.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imageViewImportData.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imageViewImportData.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imageViewExportData.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imageViewExportData.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);

        home.BTN_NEW_CMDE.setGraphic(imv1);
        home.BTN_LIST_FAC.setGraphic(imv2);
        home.BTN_QUIT.setGraphic(imv3);
        home.BTN_COMPTE.setGraphic(imv4);

        home.BTN_NEW_CMDE.setTooltip(new Tooltip("Créer une nouvelle commande"));
        home.BTN_LIST_FAC.setTooltip(new Tooltip("Voir la liste des factures"));
        home.BTN_COMPTE.setTooltip(new Tooltip("Accéder à mes informations"));
        home.BTN_QUIT.setTooltip(new Tooltip("Quitter l'application"));

        home.BTN_SAVE.setTooltip(new Tooltip("Enregistrer et valider la facture courante"));
        home.BTN_PRINT.setTooltip(new Tooltip("Imprimer la facture courante"));
        home.BTN_REGLER.setTooltip(new Tooltip("Enregistrer un règlement pour la facture en cours"));
        home.BTN_LIVRER.setTooltip(new Tooltip("Livrer la facture en cours"));

        home.ITEM_COMPTE.setGraphic(imageViewCompte);
        home.ITEM_NEW.setGraphic(imageViewNew);
        home.ITEM_PREF.setGraphic(imageViewPreference);
        home.ITEM_QUIT.setGraphic(imageViewQuit);

        home.ITEM_ABOUT.setGraphic(imvHelp1);
        home.ITEM_DOC.setGraphic(imvHelp2);
        home.ITEM_PING_L.setGraphic(imvHelp3);
        home.ITEM_PING_R.setGraphic(imvHelp4);
        //
        home.ITEM_FACTURE.setGraphic(imvFich1);
        home.ITEM_COMANDE.setGraphic(imvFich2);
        home.ITEM_CATALOG.setGraphic(imvFich3);
        home.ITEM_IMPORT.setGraphic(imageViewImportData);
        home.ITEM_EXPORT.setGraphic(imageViewExportData);

    }

    public static GridPane displayCatalogue(YvsBaseConditionnement y1, YvsBaseConditionnement y2, HomeCaisseController home) {
        GridPane gp = home.displayCatalogue();
        if (y1 != null) {
            VBox vbox1 = displayCatalogue(y1, home);
            gp.add(vbox1, 0, 0);
        }
        if (y2 != null) {
            VBox vbox1 = displayCatalogue(y2, home);
            gp.add(vbox1, 1, 0);
        }
        return gp;
    }

    public static GridPane getBasicGridPane(){
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(10, 10, 10, 10));
        gp.setAlignment(Pos.CENTER);
        return gp;
    }

    public static VBox displayCatalogue(YvsBaseConditionnement y1, HomeCaisseController home) {
        ButtonArticles vbox1 = new ButtonArticles(y1, home);
        vbox1.getStyleClass().add("catalogue-item");
        var labelDesignation=new Label(y1.getArticle().getDesignation());
        labelDesignation.getStyleClass().add("catalogue-item-label");
        labelDesignation.setWrapText(true);
        vbox1.getChildren().add(labelDesignation);
        vbox1.setPadding(new Insets(1, 0, 0, 4));
        HBox hbox1 = new HBox();
        ProgressIndicator pi = new ProgressIndicator(0d);

        VBox vbox2 = new VBox();
        vbox2.getChildren().add(getLabelBold(y1.getArticle().getRefArt()));
        vbox2.getChildren().add(new HBox(new Label("Famille: "), getLabel10(y1.getArticle().getFamille().getDesignation())));
        vbox2.getChildren().add(new HBox(new Label("Unité: "), getLabel10(y1.getUnite() != null ? y1.getUnite().getReference() : "")));
        vbox2.getChildren().add(new HBox(new Label("Prix: "), getLabelN(Constantes.nbf.format(y1.getPrix()))));
        vbox2.getChildren().add(new HBox(new Label("Prix Min.: "), getLabelN(Constantes.nbf.format(y1.getPrixMin()))));
        hbox1.getChildren().addAll(pi, vbox2);
        vbox1.getChildren().add(hbox1);
        // ajoute l'image...
        LoaderImage serv = new LoaderImage(y1.getArticle().getPhoto());
        serv.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            hbox1.getChildren().remove(pi);
            if (serv.getValue() != null) {
                hbox1.getChildren().add(0, serv.getValue());
            } else {
                hbox1.getChildren().add(0, new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/coffee.png")))));
            }
        });
        new Thread(serv).start();
        return vbox1;
    }

    public static Label getLabelBold(String text) {
        Label lab = new Label(text);
        lab.getStyleClass().add("bold");
        return lab;
    }

    public static Label getLabel10(String text) {
        Label lab = new Label(text);
        lab.getStyleClass().add("label_text");
        return lab;
    }

    public static Label getLabelN(String text) {
        Label lab = getLabelBold(text);
        lab.getStyleClass().add("label_text");
        return lab;
    }

    public static Label getLabelMontantTT(Double montant) {
        Label lab = new Label(Constantes.nbf.format(montant));
        lab.getStyleClass().add("label_info");
        return lab;
    }

 /*   public static void createImage(String photo, HBox hbox1) {
        Image img = null;
        try {
            File f = new File(ParamConnection.readFile(LymytzService.getFileInputStream()).getCheminPhotos() + "\\" + photo);
            img = new Image(new FileInputStream(f));
        } catch (FileNotFoundException ex) {
            File f = new File(CustomComponents.class.getResourceAsStream("/icones/produits.png"));
            if (f.exists()) {
                try {
                    img = new Image(new FileInputStream(f));
                } catch (FileNotFoundException ex1) {
                    Logger.getLogger(ButtonArticles.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        hbox1.getChildren().add(0, new ImageView(img));
    }*/

}
