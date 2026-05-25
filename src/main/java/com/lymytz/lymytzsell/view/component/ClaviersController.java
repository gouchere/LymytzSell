/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.component;

import com.lymytz.lymytzsell.service.application.service.ManagedFactureVente;
import com.lymytz.lymytzsell.business.helpers.EtatMontantPayer;
import com.lymytz.lymytzsell.business.helpers.KeyBoardAction;
import com.lymytz.lymytzsell.business.helpers.StatutResponse;
import com.lymytz.lymytzsell.persistence.entity.YvsComDocVentes;
import com.lymytz.lymytzsell.service.application.Controller;
import com.lymytz.lymytzsell.service.application.ManagedApplication;
import com.lymytz.lymytzsell.service.application.bean.ContentPanier;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.application.service.report.PrintTiket;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
import com.lymytz.lymytzsell.view.main.report.PrintFacture;
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

import java.net.URL;
import java.util.ResourceBundle;

import static com.lymytz.lymytzsell.business.helpers.EtatMontantPayer.OK;
import static com.lymytz.lymytzsell.business.helpers.HelperFactureVente.isValideMontantPaye;
import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_FV;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.AUCUN_JOURNAL_DE_VENTE_TROUVE;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.ERREUR;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.GENERATION_DE_LA_FACTURE_NON_REUSSI;
import static com.lymytz.lymytzsell.service.utils.UtilsProject.TYPE_RAPPORT_A4;
import static com.lymytz.lymytzsell.service.utils.UtilsProject.TYPE_RAPPORT_TICKET;

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
    private String sourceOfAction;  //Indique l'action à la source de l'ouverture du claviers (F=facture à valider, A=avance à recevoir)
    private KeyBoardAction action;  //Indique l'action à réaliser à partir du clavier:

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
        // code d'initialisation de la vue si nécessaire
    }

    public void initController(HomeCaisseController page, Onglets fac, Stage fen, String source, KeyBoardAction action, ContentPanier line) {
        this.page = page;
        this.selectOnglet = fac;
        this.fenetre = fen;
        this.sourceOfAction = source;
        this.action = action;
        this.lineContent = line;
        this.BTN_PRINT_ONLY.setVisible(true);
        if (fac != null && fac.getFacture() != null) {
            if (!Constantes.asLong(fac.getFacture().getId())) {
                this.BTN_PRINT_ONLY.setVisible(false);
            }
            switch (action) {
                case VALIDER, REGLER:
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
                case SET_QTE:
                    TITRE_CLAVIER.setText("Entrer la quantité !");
                    LAB_TITRE_REST.setText("");
                    avance = false;
                    break;
                case SET_PRIX:
                    TITRE_CLAVIER.setText("Entrer le prix !");
                    LAB_TITRE_REST.setText("");
                    break;
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
        if (LAB_AFFICH.getText() != null && (!LAB_AFFICH.getText().isEmpty())) {
            LAB_AFFICH.setText(LAB_AFFICH.getText().substring(0, LAB_AFFICH.getText().length() - 1));

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
        // view more
    }

    @FXML
    private void saisieOnClavier(KeyEvent event) {
        if (event.getCode().isDigitKey()) {
            for (Node node : PAN_BUTONS.getChildren()) {
                if (node instanceof Button button && (event.getText().equals(button.getText()))) {
                    button.fire();
                    Platform.runLater(button::requestFocus);
                }
            }
        } else if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.ACCEPT)) {
            BTN_VALID.fire();
            Platform.runLater(() -> BTN_VALID.requestFocus());
        } else if (event.getCode().equals(KeyCode.BACK_SPACE) || event.getCode().equals(KeyCode.BACK_SLASH)) {
            BTN_BACK.fire();
            Platform.runLater(() -> BTN_BACK.requestFocus());
        }
    }

    private void displayReste() {
        //calcule
        double recu = Double.parseDouble((!LAB_AFFICH.getText().isEmpty()) ? LAB_AFFICH.getText() : "0");
        if (avance) {
            LAB_REST.setText(Constantes.nbf.format((selectOnglet.getNetAPayer() - recu)));
        } else {
            if (action.equals(KeyBoardAction.VALIDER) || action.equals(KeyBoardAction.REGLER)) {
                montantAvance = (selectOnglet.getFacture().getTypeDoc().equals(TYPE_FV)) ? selectOnglet.getFacture().getMontantResteApayer() : montantAvance;
                LAB_REST.setText(Constantes.nbf.format((recu - montantAvance)));
            }
        }
        selectOnglet.setMontantRecu(recu);

    }

    private double getMontantAffiche() {
        //calcule
        return Double.parseDouble((!LAB_AFFICH.getText().isEmpty()) ? LAB_AFFICH.getText() : "0");
    }

    @FXML
    private void valideFrappe(ActionEvent event) {
        selectOnglet.getFacture().setContenus(page.buildInfoTableToContentFacure(selectOnglet.getContentFacture()));
        if (avance) {
            enregistreUneAvance();
        } else {
            switch (this.action) {
                case REGLER, VALIDER:
                    page.LAB_T_AVANCE.setText(Constantes.nbf.format(montantAvance));
                    page.LAB_NET_A_PAYER.setText(Constantes.nbf.format(selectOnglet.getNetAPayer() - montantAvance));
                    if (sourceOfAction.equals("F") && TYPE_FV.equals(selectOnglet.getFacture().getTypeDoc()) && selectOnglet.getNetAPayer() > getMontantAffiche()) {
                        LymytzService.openAlertDialog("Le montant reçu n'est pas conforme !", "Erreur montant", "Erreur !", Alert.AlertType.ERROR);
                        return;
                    }
                    switch (sourceOfAction) {
                        case "F" -> {
                            //lance la validation dans un thread
                            saveAndValidateFacture();
                            fenetre.close();
                        }
                        case "A" -> {
                            //Enregistrer l'avance sur commande
                            page.saveReglement(selectOnglet.getFacture(), montantAvance, getMontantAffiche());
                            selectOnglet.displayMontantsBean();
                            fenetre.close();
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + sourceOfAction);
                    }
                    break;
                case SET_QTE:
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
                case SET_PRIX:
                    if (this.lineContent != null && (Boolean.TRUE.equals(lineContent.getConditionnement().getArticle().getChangePrix()))) {
                        //recupère la valseur affiché.
                        this.lineContent.setPrix(getMontantAffiche());
                        //Reévalue les prix
                        lineContent = selectOnglet.evaluePrix(lineContent);
                        //exécute la méthode de modif de la vue
                        selectOnglet.addLineContent(lineContent, true);

                    }
                    fenetre.close();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.action);
            }
        }
    }

    private void enregistreUneAvance() {
        if (selectOnglet.getFacture().getTypeDoc().equals(Constantes.TYPE_BCV)) {
            selectOnglet.getFacture().setMontantAvance(getMontantAffiche());
            avance = false;
            page.LAB_T_AVANCE.setText(Constantes.nbf.format(getMontantAffiche()));
            montantAvance = getMontantAffiche();
            page.LAB_NET_A_PAYER.setText(Constantes.nbf.format(selectOnglet.getNetAPayer() - montantAvance));
            TITRE_CLAVIER.setText("Entrer Le montant reçu du client");
            LAB_AFFICH.setText("");
        }
    }

    private void saveAndValidateFacture() {
        var facture = getDocVenteFromOnglet(selectOnglet);
        var statutMontantPaye = isValideMontantPaye(selectOnglet.getFacture().getTypeDoc(), montantAvance, selectOnglet.getNetAPayer());
        var statut = ManagedFactureVente.controleBeforeSaveFacture.apply(facture);
        if (OK.equals(statutMontantPaye) && StatutResponse.OK.equals(statut)) {
            new Thread(() -> page.confirmValideFacture(facture, montantAvance, getMontantAffiche())).start();
            printTicketFacture(selectOnglet.getFacture(), selectOnglet.getMontantRecu());
            page.closeOngletFacture(selectOnglet);
        } else {
            processResponseIfError(statut, statutMontantPaye);
        }
    }

    private YvsComDocVentes getDocVenteFromOnglet(Onglets onglet) {
        YvsComDocVentes docVente = new YvsComDocVentes(onglet.getFacture());
        String numFacture = UtilsProject.generatedNumDoc((docVente.getTypeDoc().equals(TYPE_FV)) ? Constantes.TYPE_FV_NAME : Constantes.TYPE_BCV_NAME);
        docVente.setNumDoc(numFacture);
        docVente.setEnteteDoc(UtilsProject.headerDoc);
        return docVente;
    }

    private void printTicketFacture(YvsComDocVentes facture, double montantRecu) {
        if (Boolean.TRUE.equals(UtilsProject.properties.getUsePrinter()) && TYPE_RAPPORT_TICKET.equals(UtilsProject.properties.getTypeRapport())) {
            Platform.runLater(() -> {
                PrintTiket pt = new PrintTiket(montantAvance, "XX");
                pt.setFacture(new YvsComDocVentes(facture));
                pt.setMontantAvance(facture.getMontantAvance());
                pt.setMontantRecu(montantRecu);
                pt.setMontantTotal(facture.getMontantTotal());
                pt.setNetAPayer(facture.getMontantTotal());
                new Thread(pt).start();
            });
        } else if (Boolean.TRUE.equals(UtilsProject.properties.getUsePrinter()) && TYPE_RAPPORT_A4.equals(UtilsProject.properties.getTypeRapport())) {
            Platform.runLater(() -> {
                PrintFacture preview = new PrintFacture();
                preview.loadFactureToPrint(facture);
            });
        }
    }

    public void processResponseIfError(StatutResponse response, EtatMontantPayer etatMontantPayer) {
        switch (response) {
            case TIERS_INNEXISTANT ->
                    Platform.runLater(() -> LymytzService.openAlertDialog("Le tiers rattaché à ce client n'existe pas !", "Action abandonné !", ERREUR, Alert.AlertType.ERROR));
            case CLIENT_INNEXISTANT ->
                    Platform.runLater(() -> LymytzService.openAlertDialog(GENERATION_DE_LA_FACTURE_NON_REUSSI, ERREUR, "Action abandonné !", Alert.AlertType.ERROR));
            case NUMERO_DOC_NON_GENERE ->
                    Platform.runLater(() -> LymytzService.openAlertDialog(GENERATION_DE_LA_FACTURE_NON_REUSSI, ERREUR, "Le numéro de référence n'a pas pu être généré !", Alert.AlertType.ERROR));
            case FICHE_DEJA_CLOTURE ->
                    Platform.runLater(() -> LymytzService.openAlertDialog(GENERATION_DE_LA_FACTURE_NON_REUSSI, ERREUR, "Votre fiche de vente est déjà clôturé !", Alert.AlertType.ERROR));
            case ENTETE_FACTURE_NON_TROUVE ->
                    Platform.runLater(() -> LymytzService.openAlertDialog(GENERATION_DE_LA_FACTURE_NON_REUSSI, ERREUR, AUCUN_JOURNAL_DE_VENTE_TROUVE, Alert.AlertType.ERROR));
            case DATE_FICHE_INCORRECT ->
                    Platform.runLater(() -> LymytzService.openAlertDialog(GENERATION_DE_LA_FACTURE_NON_REUSSI, ERREUR, "Vérifier la date de votre fiche !", Alert.AlertType.ERROR));
            default -> {
            }
        }
        if (EtatMontantPayer.KO_NET_FACTURE.equals(etatMontantPayer)) {
            Platform.runLater(() -> LymytzService.openAlertDialog("Incohérence des montants !", "Erreur", "Le montant payé de la facture est différent du TTC !", Alert.AlertType.ERROR));
        } else if (EtatMontantPayer.KO_NET_COMMANDE.equals(etatMontantPayer)) {
            Platform.runLater(() -> LymytzService.openAlertDialog("Incohérence des montants !", "Erreur", "Le montant d'avance de la commande est suppérieure au TTC !", Alert.AlertType.ERROR));
        }
    }

    @Override
    public void freeMemoryController() {
        //comming soon
    }

}
