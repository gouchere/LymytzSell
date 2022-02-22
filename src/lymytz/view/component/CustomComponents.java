/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.view.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lymytz.dao.ParamConnection;
import lymytz.dao.entity.YvsBaseConditionnement;
import lymytz.service.application.composant.ButtonArticles;
import lymytz.service.start.StartController;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.view.LocalLoader;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author Admin
 */
public class CustomComponents {

    public CustomComponents() {
    }

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
        final ImageView btn_save = new ImageView(new Image(LocalLoader.class.getResource("icones/save.png").toExternalForm()));
        final ImageView btn_print = new ImageView(new Image(LocalLoader.class.getResource("icones/print.png").toExternalForm()));
        final ImageView btn_reg = new ImageView(new Image(LocalLoader.class.getResource("icones/money.png").toExternalForm()));
        final ImageView btn_liv = new ImageView(new Image(LocalLoader.class.getResource("icones/cdcopy.png").toExternalForm()));

        home.BTN_SAVE.setGraphic(btn_save);
        home.BTN_SAVE.getStylesheets().add("mes_boutons");
        home.BTN_PRINT.setGraphic(btn_print);
        home.BTN_REGLER.setGraphic(btn_reg);
        home.BTN_LIVRER.setGraphic(btn_liv);

        ImageView imv1 = new ImageView(new Image(LocalLoader.class.getResource("icones/12346.png").toExternalForm()));
        ImageView imv2 = new ImageView(new Image(LocalLoader.class.getResource("icones/details.png").toExternalForm()));
        ImageView imv3 = new ImageView(new Image(LocalLoader.class.getResource("icones/log_out.png").toExternalForm()));
        ImageView imv4 = new ImageView(new Image(LocalLoader.class.getResource("icones/compte.png").toExternalForm()));
        ImageView imv_quit_M = new ImageView(new Image(LocalLoader.class.getResource("icones/log_out.png").toExternalForm()));
        ImageView imv_new_M = new ImageView(new Image(LocalLoader.class.getResource("icones/12346.png").toExternalForm()));
        ImageView imv_compte_M = new ImageView(new Image(LocalLoader.class.getResource("icones/compte.png").toExternalForm()));
        ImageView imv_pref_M = new ImageView(new Image(LocalLoader.class.getResource("icones/connect.png").toExternalForm()));

        ImageView imv_help_4 = new ImageView(new Image(LocalLoader.class.getResource("icones/ico_server.png").toExternalForm()));
        ImageView imv_help_3 = new ImageView(new Image(LocalLoader.class.getResource("icones/ico_server.png").toExternalForm()));
        ImageView imv_help_1 = new ImageView(new Image(LocalLoader.class.getResource("icones/about.png").toExternalForm()));
        ImageView imv_help_2 = new ImageView(new Image(LocalLoader.class.getResource("icones/manual.png").toExternalForm()));
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
        imv_quit_M.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_quit_M.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imv_new_M.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_new_M.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imv_compte_M.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_compte_M.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imv_pref_M.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_pref_M.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        //
        imv_help_1.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_help_1.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imv_help_2.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_help_2.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imv_help_3.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_help_3.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imv_help_4.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_help_4.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        //
        ImageView imv_fich_1 = new ImageView(new Image(LocalLoader.class.getResource("icones/list.png").toExternalForm()));
        ImageView imv_fich_2 = new ImageView(new Image(LocalLoader.class.getResource("icones/list.png").toExternalForm()));
        ImageView imv_fich_3 = new ImageView(new Image(LocalLoader.class.getResource("icones/catalogue1.png").toExternalForm()));

        ImageView imv_data_1 = new ImageView(new Image(LocalLoader.class.getResource("icones/import_data.png").toExternalForm()));
        ImageView imv_data_2 = new ImageView(new Image(LocalLoader.class.getResource("icones/export_data.png").toExternalForm()));

        imv_fich_1.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_fich_1.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imv_fich_2.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_fich_2.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imv_fich_3.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_fich_3.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imv_data_1.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_data_1.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);
        imv_data_2.setFitHeight(Constantes.HEIGHT_ICON_MENU_BAR);
        imv_data_2.setFitWidth(Constantes.WIDTH_ICON_MENU_BAR);

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

        home.ITEM_COMPTE.setGraphic(imv_compte_M);
        home.ITEM_NEW.setGraphic(imv_new_M);
        home.ITEM_PREF.setGraphic(imv_pref_M);
        home.ITEM_QUIT.setGraphic(imv_quit_M);

        home.ITEM_ABOUT.setGraphic(imv_help_1);
        home.ITEM_DOC.setGraphic(imv_help_2);
        home.ITEM_PING_L.setGraphic(imv_help_3);
        home.ITEM_PING_R.setGraphic(imv_help_4);
        //
        home.ITEM_FACTURE.setGraphic(imv_fich_1);
        home.ITEM_COMANDE.setGraphic(imv_fich_2);
        home.ITEM_CATALOG.setGraphic(imv_fich_3);
        home.ITEM_IMPORT.setGraphic(imv_data_1);
        home.ITEM_EXPORT.setGraphic(imv_data_2);

        //dimensionne le header
        home.TOOLBAR.setPrefWidth(StartController.SCREENWIDTH);

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

    public static VBox displayCatalogue(YvsBaseConditionnement y1, HomeCaisseController home) {
        lymytz.service.application.composant.ButtonArticles vbox1 = new ButtonArticles(y1, home);
        vbox1.getChildren().add(new Label(y1.getArticle().getDesignation()));
        vbox1.setPrefWidth(320);
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
        serv.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                hbox1.getChildren().remove(pi);
                if (serv.getValue() != null) {
                    hbox1.getChildren().add(0, serv.getValue());
                } else {
                    hbox1.getChildren().add(0, new ImageView(new Image(LocalLoader.class.getResource("icones/coffee.png").toExternalForm())));
                }
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
        Label lab = new Label(text);
        lab.getStyleClass().add("bold");
        lab.getStyleClass().add("label_text");
        return lab;
    }

    public static Label getLabelMontantTT(Double montant) {
        Label lab = new Label(Constantes.nbf.format(montant));
        lab.getStyleClass().add("label_info");
        return lab;
    }

    public static void createImage(String photo, HBox hbox1) {
        Image img = null;
        try {
            File f = new File(ParamConnection.readFile(LymytzService.getFileInputStream()).getCheminPhotos() + "\\" + photo);
            img = new Image(new FileInputStream(f));
        } catch (FileNotFoundException ex) {
            File f = new File(LocalLoader.class.getResource("icones/produits.png").toExternalForm());
            if (f.exists()) {
                try {
                    img = new Image(new FileInputStream(f));
                } catch (FileNotFoundException ex1) {
                    Logger.getLogger(ButtonArticles.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        hbox1.getChildren().add(0, new ImageView(img));
    }

}
