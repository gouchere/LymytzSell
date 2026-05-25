/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.controller;

import com.lymytz.lymytzsell.business.helpers.StatutResponse;
import com.lymytz.lymytzsell.persistence.dao.Options;
import com.lymytz.lymytzsell.persistence.dao.util.UtilsBean;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseCaisse;
import com.lymytz.lymytzsell.persistence.entity.YvsComEnteteDocVente;
import com.lymytz.lymytzsell.persistence.entity.YvsComptaCaissePieceVirement;
import com.lymytz.lymytzsell.persistence.dao.LocalQueryFactories;
import com.lymytz.lymytzsell.persistence.dao.RQueryFactories;
import com.lymytz.lymytzsell.service.application.Controller;
import com.lymytz.lymytzsell.service.application.synchro.UtilEntityBase;
import com.lymytz.lymytzsell.service.application.synchro.export.UtilExport;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.synchro.ws.ResultatAction;
import com.lymytz.lymytzsell.synchro.ws.WsSynchro;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

import static com.lymytz.lymytzsell.service.utils.MessagesConstants.ERREUR;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class FormVirementController implements Initializable, Controller {
    private final Logger LOGGER = LogManager.getLogger(FormVirementController.class);

    LocalQueryFactories rq = new LocalQueryFactories();
    HomeCaisseController page;
    Stage fenDialogue;
    private Long idRemoteHeader;
    private Long idHeader;
    private final ObservableList<YvsBaseCaisse> items = FXCollections.observableArrayList();
    Double avanceCmde;
    Double totalFacture;

    @FXML
    private ListView<String> LIST_VER;
    @FXML
    private Label LAB_CAISSE;
    @FXML
    private Label MESSAGE;
    @FXML
    private Label LAB_CA;
    @FXML
    private Label LAB_COMMANDE;
    @FXML
    private Label LAB_VERSE;
    @FXML
    private CheckBox CHK_CLOTURE;
    @FXML
    private TextField TXT_MONTANT;
    @FXML
    private ComboBox<YvsBaseCaisse> CB_CAISS_CIBLE;
    @FXML
    private ProgressIndicator PROGRESS_CLOSE;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nécessaire pour le composant
    }

    private final Function<YvsComptaCaissePieceVirement, StatutResponse> contole = piece -> {
        if (piece.getHeader() == null) return StatutResponse.ENTETE_NON_TROUVE;
        if (Boolean.TRUE.equals(piece.getHeader().getCloturer())) return StatutResponse.FICHE_DEJA_CLOTURE;
        if (piece.getCible() == null) return StatutResponse.CAISSE_CIBLE_NON_TROUVE;
        if (piece.getMontant() <= 0) return StatutResponse.MAUVAIS_MONTANT;
        return StatutResponse.OK;
    };

    public void initFormVirement(long idHeader, YvsBaseCaisse caisseSource, HomeCaisseController page, Stage stage) {
        this.page = page;
        this.idHeader = idHeader;
        this.fenDialogue = stage;
        // trouve l'equivalent du header...
        idRemoteHeader = (UtilsProject.REPLICATION) ? UtilEntityBase.findIdRemoteData(Constantes.TABLE_HEADER_DOC_CODE, idHeader) : idHeader;
        if (caisseSource != null && Constantes.asLong(this.idRemoteHeader)) {
            caisseSource.setCaissesLiees(rq.loadByNamedQuery("YvsBaseLiaisonCaisse.findBySource", new String[]{"source"}, new Object[]{caisseSource}));
            caisseSource.getCaissesLiees().forEach(lc -> items.add(lc.getCaisseCible()));
            CB_CAISS_CIBLE.setItems(items);
            CB_CAISS_CIBLE.setConverter(new StringConverter<>() {

                @Override
                public String toString(YvsBaseCaisse object) {
                    if (object != null) {
                        return object.getCode();
                    }
                    return "";
                }

                @Override
                public YvsBaseCaisse fromString(String string) {
                    return null;
                }
            });
            LAB_CAISSE.setText(caisseSource.getIntitule());
            //Total facture
            totalFacture = UtilsBean.getTotalFacturesHeader(idHeader);
            LAB_CA.setText(Constantes.nbf.format(totalFacture));
            if (UtilsProject.headerDoc != null) {
                avanceCmde = (Double) rq.findOneObjectByNQ("YvsComptaCaissePieceVente.findSumByCaissier", new String[]{"caissier", "date"}, new Object[]{UtilsProject.currentUser.getUsers(), UtilsProject.headerDoc.getDateEntete()});
            }
            avanceCmde = avanceCmde != null ? avanceCmde : 0;
            //Accompte perçu
            LAB_COMMANDE.setText(Constantes.nbf.format(avanceCmde));
            LAB_VERSE.setText(Constantes.nbf.format(avanceCmde + totalFacture));
            PROGRESS_CLOSE.setVisible(false);
            loadVersementFiche();
        } else {
            LymytzService.openAlertDialog("Aucune caisse de virement n'a été trouvé ou Aucun en-tête de vente", "Erreur d'en-tête", "Erreur de cloture", Alert.AlertType.ERROR);
        }
    }

    private void notificationByStatut(StatutResponse statutResponse) {
        switch (statutResponse) {
            case CAISSE_CIBLE_NON_TROUVE ->
                    LymytzService.openAlertDialog(StatutResponse.CAISSE_CIBLE_NON_TROUVE.getMessage(), ERREUR, "Caisse de versement introuvable", Alert.AlertType.WARNING);
            case ENTETE_NON_TROUVE ->
                    LymytzService.openAlertDialog(StatutResponse.ENTETE_NON_TROUVE.getMessage(), ERREUR, "L'entête du document n'a pas pu être chargé", Alert.AlertType.ERROR);
            case FICHE_DEJA_CLOTURE ->
                    LymytzService.openAlertDialog(StatutResponse.FICHE_DEJA_CLOTURE.getMessage(), ERREUR, "L'entête du document est déjà clôturé", Alert.AlertType.ERROR);
            case MAUVAIS_MONTANT ->
                    LymytzService.openAlertDialog(StatutResponse.MAUVAIS_MONTANT.getMessage(), ERREUR, StatutResponse.MAUVAIS_MONTANT.getMessage(), Alert.AlertType.ERROR);
            default -> LOGGER.info("statut du règlement OK");
        }
    }

    @FXML
    private void termineClose(ActionEvent event) {
        //effectue un virement de caisse du montant saisie
        double montant;
        try {
            montant = Double.parseDouble(TXT_MONTANT.getText().trim().replaceAll("[^\\d-+]", ""));
        } catch (NumberFormatException ex) {
            montant = 0D;
        }
        try {
            YvsComEnteteDocVente header = (YvsComEnteteDocVente) rq.findOneByNQ("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{idHeader});
            Optional.ofNullable(header).filter(YvsComEnteteDocVente::getCloturer)
                    .ifPresent(f -> LymytzService.openAlertDialog(StatutResponse.FICHE_DEJA_CLOTURE.getMessage(), ERREUR, "L'entête du document est déjà clôturé", Alert.AlertType.ERROR));
            saveVirementFromFiche(header, montant);
            closeFicheVente(header);
        } catch (NumberFormatException ex) {
            LOGGER.error("Erreur lors de la clôture", ex);
            LymytzService.openExceptionDialog("Erreur lors de la clôture !", Alert.AlertType.ERROR, ex);
        }
    }

    private void closeFicheVente(YvsComEnteteDocVente header) {
        if (header != null) {
            // Vérifier s'il y a  des factures non réglés et/ou non encore entièrement livré et demander une confirmation
            Long nobreDocNonLivreOuNonRegle = (Long) rq.findOneObjectByNQ("YvsComDocVentes.countFactureNonLivreOrNonPayeByHeader", new String[]{"statut", "statutLivre", "statutRegle", "header", "typeDoc"},
                    new Object[]{Constantes.ETAT_VALIDE, Constantes.ETAT_LIVRE, Constantes.ETAT_REGLE, header, Constantes.TYPE_FV});
            nobreDocNonLivreOuNonRegle = (nobreDocNonLivreOuNonRegle != null) ? nobreDocNonLivreOuNonRegle : 0;
            if (nobreDocNonLivreOuNonRegle <= 0) {

                header.setCloturer(Boolean.TRUE);
                header.setCloturerBy(UtilsProject.currentUser.getUsers());
                header.setDateCloturer(new Date());
                header.setDateValider(new Date());
                header.setValiderBy(UtilsProject.currentUser.getUsers());
                header.setDateUpdate(new Date());
                rq.update(header);
                page.resetAllView(header);
                // Désactiver le planning
                if (Boolean.FALSE.equals(header.getCreneau().getPermanent())) {
                    header.getCreneau().setActif(false);
                    header.getCreneau().setDateUpdate(new Date());
                    rq.update(header.getCreneau());
                }
                fenDialogue.close();
            } else {
                LymytzService.openAlertDialog(nobreDocNonLivreOuNonRegle + " Facture(s) non encore livrée(s) et/ou validée(s) pour ce journal de vente", "Impossible de clôturer", "Impossible de clôturer", Alert.AlertType.ERROR);
            }

        }
    }

    private void saveVirementFromFiche(YvsComEnteteDocVente header, double montant) {
        try {
            YvsComptaCaissePieceVirement entity = buildEntityPieceCaisse(header, montant);
            var statutResponse = contole.apply(entity);
            if (StatutResponse.OK.equals(statutResponse)) {
                ResultatAction<YvsComptaCaissePieceVirement> result = new WsSynchro().saveVirement(UtilExport.exportPieceVirement(entity));
                if (result != null) {
                    LymytzService.openAlertDialog(result.getMessage(), "Résultat", result.getCodeInfo() + "", (result.isResult() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR));
                    if (result.isResult()) {
                        TXT_MONTANT.setText("0");
                        //Change le statut de la ligne
                    }
                }
            } else {
                notificationByStatut(statutResponse);
            }
            PROGRESS_CLOSE.setVisible(false);
        } catch (Exception ex) {
            LOGGER.error(ex);
            LymytzService.openExceptionDialog("Erreur lors de la clôture ! Le service de virement ne s'est pas exécuté correctemment", Alert.AlertType.ERROR, ex);
            PROGRESS_CLOSE.setVisible(false);
        }
    }

    private YvsComptaCaissePieceVirement buildEntityPieceCaisse(YvsComEnteteDocVente header, double montant) {
        YvsComptaCaissePieceVirement entity = new YvsComptaCaissePieceVirement();
        entity.setDatePaiement(header.getDateEntete());
        entity.setDatePaimentPrevu(header.getDateEntete());
        entity.setDatePiece(header.getDateEntete());
        entity.setDateSave(new Date());
        entity.setDateUpdate(new Date());
        entity.setMontant(montant);
        entity.setStatutPiece(Constantes.STATUT_DOC_SOUMIS);
        entity.setNote("Versement " + UtilsProject.currentUser.getUsers().getNomUsers() + " du " + Constantes.dfD.format(header.getDateEntete()));
        entity.setCaissierCible(null);
        entity.setCaissierSource(UtilsProject.currentUser.getUsers());
        entity.setCible(CB_CAISS_CIBLE.getValue());
        entity.setModel(UtilsProject.modeReg);
        entity.setSource(UtilsProject.caisse);
        entity.setHeader(header);
        return entity;
    }

    private void loadVersementFiche() {
        if (UtilsProject.headerDoc != null && UtilsProject.caisse != null) {
            new Thread(() -> {
                Long idHead = (UtilsProject.REPLICATION) ? idRemoteHeader : UtilsProject.headerDoc.getId();
                if (RQueryFactories.pingServer()) {
                    YvsComEnteteDocVente header = (YvsComEnteteDocVente) rq.findOneByNQ("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{idHeader});
                    loadVersementFiche(idHead, header);
                } else {
                    Platform.runLater(() -> MESSAGE.setText("Impossible de se connecter au serveur distant"));
                }
            }).start();
        }
    }

    private void loadVersementFiche(Long idHead, YvsComEnteteDocVente header) {
        final String query = """
                SELECT y.numero_piece, y.montant FROM yvs_compta_caisse_piece_virement y LEFT JOIN yvs_compta_notif_versement_vente h ON h.piece=y.id
                WHERE (h.id=? OR y.date_piece=?::date) AND y.source=?
                """;
        List<Object[]> re = new RQueryFactories<>().loadBySQLQuery(query, new Options[]{new Options(idHead, 1),
                new Options(header.getDateEntete(), 2), new Options(UtilsProject.caisse.getId(), 3)});
        Platform.runLater(() -> {
            LIST_VER.getItems().clear();
            double soe = 0d;
            for (Object[] l : re) {
                LIST_VER.getItems().add(l[0] + "   --*--   " + ((l[1] != null) ? Constantes.nbf.format(Double.valueOf((String) l[1])) : "0"));
                soe = soe + ((l[1] != null) ? Double.parseDouble((String) l[1]) : 0d);
            }
            MESSAGE.setText("");
        });
    }

    @Override
    public void freeMemoryController() {
        //no
    }
}
