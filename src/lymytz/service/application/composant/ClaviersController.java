/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.composant;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.service.application.Controller;
import lymytz.service.application.FactureController;
import lymytz.service.application.ManagedApplication;
import lymytz.service.application.bean.ContentPanier;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.PrintTiket;
import lymytz.service.utils.UtilsProject;
import lymytz.view.main.HomeCaisseController;
import lymytz.view.main.report.PrintFacture;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class ClaviersController extends ManagedApplication implements Initializable, Controller {

    HomeCaisseController page;
    private Stage fenetre;
    private Onglets selectOnglet;
    ContentPanier lineContent;
    private boolean avance = false;
    double montantAvance;
    private String source;  //Indique l'action à la source de l'ouverture du claviers (F=facture à valider, A=avance à recevoir)
    private String action;  //Indique l'action à réaliser à partir du clavier:
    /**
     * SET_QTE, SET_PRIX, VALIDER,REGLER
     */

    @FXML
    private Label LAB_AFFICH;
    @FXML
    private Label LAB_TITRE_REST;
    @FXML
    private Label TITRE_CLAVIER;
    @FXML
    private VBox ZONE_REST;
    @FXML
    private Label LAB_REST;
    @FXML
    private Button BTN_CLEAR;
    @FXML
    private Button BTN_BACK;
    @FXML
    private Button BTN_VALID;
    @FXML
    private GridPane PAN_BUTONS;
    @FXML
    private Button BTN_PRINT_ONLY;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initController(HomeCaisseController page, Onglets fac, Stage fen, String source, String action, ContentPanier line) {
        this.page = page;
        this.selectOnglet = fac;
        this.fenetre = fen;
        this.source = source;
        this.action = action;
        this.lineContent = line;
        this.BTN_PRINT_ONLY.setVisible(true);
        if (fac != null ? fac.getFacture() != null : false) {
            if (!Constantes.asLong(fac.getFacture().getId())) {
                this.BTN_PRINT_ONLY.setVisible(false);
            }
            switch (action) {
                case "VALIDER":
                case "REGLER":
                    if (fac.getFacture().getTypeDoc().equals(Constantes.TYPE_BCV)) {
                        TITRE_CLAVIER.setText("Entrer le montant d'avance de la commande !");
                        LAB_TITRE_REST.setText("Reste à payer");
                        avance = true;
                    } else {
                        TITRE_CLAVIER.setText("Entrer Le montant reçu du client");
                        LAB_TITRE_REST.setText("A restituer");
                        avance = false;
                    }
                    LAB_TITRE_REST.setText("A restituer");
                    break;
                case "SET_QTE":
                    TITRE_CLAVIER.setText("Entrer la quantité !");
                    LAB_TITRE_REST.setText("");
                    avance = false;
                    break;
                case "SET_PRIX":
                    TITRE_CLAVIER.setText("Entrer le prix !");
                    LAB_TITRE_REST.setText("");
                    break;
            }

        }
    }

    public void initController(HomeCaisseController page, Onglets fac, Stage fen, String source, String action) {
        initController(page, fac, fen, source, action, null);
    }

    public void iManagedApplicationer(Onglets fac, Stage fen, String source) {
        this.selectOnglet = fac;
        this.fenetre = fen;
        this.source = source;
        if (fac != null ? fac.getFacture() != null : false) {
            if (fac.getFacture().getTypeDoc().equals(Constantes.TYPE_BCV)) {
                TITRE_CLAVIER.setText("Entrer le montant d'avance de la commande !");
                avance = true;
            } else {
                TITRE_CLAVIER.setText("Entrer le montant reçu du client");
                avance = false;
            }
        }
    }

    @FXML
    private void clearEcran(ActionEvent event) {
        LAB_AFFICH.setText("");
        displayReste();
    }

    @FXML
    private void effacerEcran(ActionEvent event) {
        if (LAB_AFFICH.getText() != null) {
            if (!LAB_AFFICH.getText().isEmpty()) {
                LAB_AFFICH.setText(LAB_AFFICH.getText().substring(0, LAB_AFFICH.getText().length() - 1));
            }
        }
        displayReste();
    }

    @FXML
    private void frappeCavier(ActionEvent event) {
        if (LAB_AFFICH.getText() == null) {
            LAB_AFFICH.setText("");
        }
        LAB_AFFICH.setText(LAB_AFFICH.getText().concat(((Button) event.getSource()).getText()));
        displayReste();
    }

    @FXML
    private void printOnly(ActionEvent event) {
    }

    @FXML
    private void saisieOnClavier(KeyEvent event) {
        if (event.getCode().isDigitKey()) {
            for (Node node : PAN_BUTONS.getChildren()) {
                if (node instanceof Button) {
                    if (event.getText().equals(((Button) node).getText())) {
                        Button b = (Button) node;
                        b.fire();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                b.requestFocus();
                            }
                        });
                    }
                }
            }
        } else if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.ACCEPT)) {
            BTN_VALID.fire();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    BTN_VALID.requestFocus();
                }
            });
        } else if (event.getCode().equals(KeyCode.BACK_SPACE) || event.getCode().equals(KeyCode.BACK_SLASH)) {
            BTN_BACK.fire();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    BTN_BACK.requestFocus();
                }
            });
        }
    }

    private void displayReste() {
        //calcule
        double recu = Double.valueOf((!LAB_AFFICH.getText().isEmpty()) ? LAB_AFFICH.getText() : "0");
        if (avance) {
            LAB_REST.setText(Constantes.nbf.format((selectOnglet.getNetAPayer() - recu)));
        } else {
            if (action.equals("VALIDER") || action.equals("REGLER")) {
                montantAvance = (selectOnglet.getFacture().getTypeDoc().equals(Constantes.TYPE_FV)) ? selectOnglet.getFacture().getMontantResteApayer() : montantAvance;
                LAB_REST.setText(Constantes.nbf.format((recu - montantAvance)));
            }
        }
        selectOnglet.setMontantRecu(recu);

    }

    private double getMontantAffiche() {
        //calcule
        return Double.valueOf((!LAB_AFFICH.getText().isEmpty()) ? LAB_AFFICH.getText() : "0");
    }

    @FXML
    private void valideFrappe(ActionEvent event) {
        selectOnglet.getFacture().setContenus(page.buildInfoTableToContentFacure(selectOnglet.getContentFacture()));
        if (avance) {
            if (selectOnglet.getFacture().getTypeDoc().equals(Constantes.TYPE_BCV)) {
                selectOnglet.getFacture().setMontantAvance(getMontantAffiche());
                avance = false;
                page.LAB_T_AVANCE.setText(Constantes.nbf.format(getMontantAffiche()));
                montantAvance = getMontantAffiche();
                page.LAB_NET_A_PAYER.setText(Constantes.nbf.format(selectOnglet.getNetAPayer() - montantAvance));
                TITRE_CLAVIER.setText("Entrer Le montant reçu du client");
                LAB_AFFICH.setText("");
            }
        } else {
            switch (this.action) {
                case "VALIDER":
                case "REGLER":
                    page.LAB_T_AVANCE.setText(Constantes.nbf.format(montantAvance));
                    page.LAB_NET_A_PAYER.setText(Constantes.nbf.format(selectOnglet.getNetAPayer() - montantAvance));
                    if (source.equals("F")) {
                        if (selectOnglet.getFacture().getTypeDoc().equals(Constantes.TYPE_FV)) {
                            if (selectOnglet.getNetAPayer() > getMontantAffiche()) {
                                LymytzService.openAlertDialog("Le montant reçu n'est pas conforme !", "Erreur montant", "Erreur !", Alert.AlertType.ERROR);
                                return;
                            }
                        }
                    }
                    if (source.equals("F")) {
                        /**
                         * lance la validation dans une thread*
                         */
                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (page.confirmValideFacture(selectOnglet, montantAvance, getMontantAffiche(), source)) {
                                    if (UtilsProject.paramConnection.getUsePrinter()) {
                                        if (UtilsProject.paramConnection.getTypeRapport().equals(UtilsProject.TYPE_RAPPORT_TICKET)) {
                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    PrintTiket pt = new PrintTiket(page, selectOnglet, montantAvance, "XX");
                                                    pt.setFacture(new YvsComDocVentes(selectOnglet.getFacture()));
                                                    pt.setMontantAvance(selectOnglet.getFacture().getMontantAvance());
                                                    pt.setMontantRecu(selectOnglet.getMontantRecu());
                                                    pt.setMontantTotal(selectOnglet.getFacture().getMontantTotal());
                                                    pt.setNetAPayer(selectOnglet.getFacture().getMontantTotal());
                                                    pt.run();
                                                }
                                            });
                                        } else {
                                            Platform.runLater(new Runnable() {

                                                @Override
                                                public void run() {
                                                    PrintFacture preview = new PrintFacture(page, selectOnglet);
                                                    preview.loadFactureToPrint(selectOnglet.getFacture());
                                                }
                                            });
                                        }
                                    }
//                                    if (UtilsProject.paramConnection.getUsePrinter()) {
                                    //crée immédiatement une nouvelle facture avec le client divers
                                    Platform.runLater(new Runnable() {

                                        @Override
                                        public void run() {

                                            VBox root = null;
                                            FactureController controler = LymytzService.openWindow("main/form_create_facture.fxml", "Lymytz /Nouvelle Facture", root, 600.0, 305.0, false, page);
                                            if (controler != null) {
                                                controler.initDataForm(page);
                                            }
                                        }
                                    });
//                                    }
                                }
                            }
                        });
                        t.start();
                        fenetre.close();
                        /**
                         * end*
                         */
                    } else if (source.equals("A")) {
                        //Enregistrer l'avance sur commande                
                        page.saveReglement(selectOnglet.getFacture(), montantAvance, getMontantAffiche());
                        selectOnglet.displayMontantsBean();
                        fenetre.close();
                    }
                    break;
                case "SET_QTE":
                    if (this.lineContent != null) {
                        //recupère la valseur affiché.
                        this.lineContent.setQuantite(getMontantAffiche());
                        //Reévalue les prix
                        lineContent = selectOnglet.evaluePrix(lineContent);
                        //exécute la méthode de modif de la vue
                        selectOnglet.addLineContent(lineContent, true);

                    }
                    fenetre.close();
                    break;
                case "SET_PRIX":
                    if (this.lineContent != null) {
                        //Modifie le prix seulement si l'article l'autorise
                        if (lineContent.getConditionnement().getArticle().getChangePrix()) {
                            //recupère la valseur affiché.
                            this.lineContent.setPrix(getMontantAffiche());
                            //Reévalue les prix
                            lineContent = selectOnglet.evaluePrix(lineContent);
                            //exécute la méthode de modif de la vue
                            selectOnglet.addLineContent(lineContent, true);
                        }
                    }
                    fenetre.close();
                    break;
            }
        }
    }

    @Override
    public void freeMemoryController() {
    }

}
