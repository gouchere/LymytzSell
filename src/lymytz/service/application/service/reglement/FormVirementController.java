/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.service.reglement;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javax.print.attribute.standard.Severity;
import lymytz.dao.Options;
import lymytz.dao.UtilsBean;
import lymytz.dao.entity.YvsBaseCaisse;
import lymytz.dao.entity.YvsBaseLiaisonCaisse;
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.dao.entity.YvsComptaCaissePieceVirement;
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.application.Controller;
import lymytz.service.application.synchro.UtilEntityBase;
import lymytz.service.application.synchro.export.UtilExport;
import lymytz.service.utils.ConsUtil;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;
import lymytz.synchro.ws.ResultatAction;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.main.HomeCaisseController;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class FormVirementController implements Initializable, Controller {

    LQueryFactories rq = new LQueryFactories();

    HomeCaisseController page;
    Stage fenDialogue;
    private Long idRemoteHeader;
    private Long idHeader;
    private YvsBaseCaisse caisse;
    private ObservableList items = FXCollections.observableArrayList();
    Double soldeCaisse, avanceCmde, totalFacture;

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
//    @FXML
//    private Label LAB_VERSEC;
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
        // TODO
    }

    public void initFormVirement(long idHeader, YvsBaseCaisse caisseSource, HomeCaisseController page, Stage stage) {
        this.page = page;
        this.idHeader = idHeader;
        this.fenDialogue = stage;
        // trouve l'equivalent du header...
        idRemoteHeader = UtilEntityBase.findIdRemoteData(Constantes.TABLE_HEADER_DOC_CODE, idHeader);
        if (caisseSource != null && Constantes.asLong(this.idRemoteHeader)) {
            caisseSource.setCaissesLiees(rq.loadByNamedQuery("YvsBaseLiaisonCaisse.findBySource", new String[]{"source"}, new Object[]{caisseSource}));
            caisseSource.getCaissesLiees().stream().forEach((lc) -> {
                items.add(lc.getCaisseCible());
            });
            CB_CAISS_CIBLE.setItems(items);
            CB_CAISS_CIBLE.setConverter(new StringConverter<YvsBaseCaisse>() {

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
            LAB_COMMANDE.setText(Constantes.nbf.format(avanceCmde != null ? avanceCmde : 0));
            //Montant déjà versé
//            Double versement = (Double) rq.findOneObjectByNQ("YvsComptaNotifVersementVente.findSumByHeader", new String[]{"enteteDoc"}, new Object[]{UtilsProject.headerDoc});
//            Double versementConfirme = (Double) rq.findOneObjectByNQ("YvsComptaNotifVersementVente.findSumByHeaderStatut", new String[]{"enteteDoc", "statut"}, new Object[]{UtilsProject.headerDoc, Constantes.STATUT_DOC_PAYER});
//            versement = versement != null ? versement : 0;
//            versementConfirme = versementConfirme != null ? versement : 0;
            LAB_VERSE.setText(Constantes.nbf.format(avanceCmde + totalFacture));
//            LAB_VERSEC.setText(Constantes.nbf.format(versementConfirme));
            PROGRESS_CLOSE.setVisible(false);
            loadVersementFiche();
        } else {
            LymytzService.openAlertDialog("Aucune caisse de virement n'a été trouvé ou Aucun en-tête de vente", "Erreur d'en-tête", "Erreur de cloture", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void termineClose(ActionEvent event) {
        //effectue un virement de caisse du montant saisie
        try {
            Double montant = Double.valueOf(TXT_MONTANT.getText().trim().replaceAll("[^\\d-\\+]", ""));
            if (montant > 0) {
                YvsComEnteteDocVente header = (YvsComEnteteDocVente) rq.findOneByNQ("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{idHeader});
                if (header != null ? !header.getCloturer() : false) {
                    PROGRESS_CLOSE.setVisible(true);
                    if (CB_CAISS_CIBLE.getValue() != null) {
                        try {
                            WsSynchro ws = new WsSynchro();
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
                            ResultatAction result = ws.saveVirement(UtilExport.exportPieceVirement(entity));
                            if (result != null) {
                                LymytzService.openAlertDialog(result.getMessage(), "Résultat", result.getCodeInfo() + "", (result.isResult() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR));
                                if (result.isResult()) {
                                    TXT_MONTANT.setText("0");
                                    //Change le statut de la ligne
                                }
                            }
                            // Nettoie et clôture la fiche
                            page.cleanVente(header.getId());
                            if (!header.getCloturer()) {
                                // Vérifier s'il y a  des factures non réglés et/ou non encore entièrement livré et demander une confirmation
                                Long nb = (Long) rq.findOneObjectByNQ("YvsComDocVentes.countFactureNonLivreOrNonPayeByHeader", new String[]{"statut", "statutLivre", "statutRegle", "header", "typeDoc"},
                                        new Object[]{Constantes.ETAT_VALIDE, Constantes.ETAT_LIVRE, Constantes.ETAT_REGLE, header, Constantes.TYPE_FV});
                                nb = (nb != null) ? nb : 0;
                                if (nb <= 0) {
                                    header.setCloturer(Boolean.TRUE);
                                    header.setCloturerBy(UtilsProject.currentUser.getUsers());
                                    header.setDateCloturer(new Date());
                                    header.setDateValider(new Date());
                                    header.setValiderBy(UtilsProject.currentUser.getUsers());
                                    header.setDateUpdate(new Date());
                                    rq.update(header);
                                    //Initialiser la vue
                                    page.resetAllView(header);
                                    // Désactiver le planning
                                    if (!header.getCreneau().getPermanent()) {
                                        header.getCreneau().setActif(false);
                                        header.getCreneau().setDateUpdate(new Date());
                                        rq.update(header.getCreneau());
                                    }
                                    // Initialiser la fiche
                                    fenDialogue.close();
                                } else {
                                    LymytzService.openAlertDialog(nb + " Facture(s) non encore livrée(s) et/ou validée(s) pour ce journal de vente", "Impossible de clôturer", "Impossible de clôturer", Alert.AlertType.ERROR);
                                }

                            } else {
                                LymytzService.openAlertDialog("Ce journal est déjà clôturée", "Journal clôturé", "Erreur", Alert.AlertType.WARNING);
                            }
                            PROGRESS_CLOSE.setVisible(false);
                        } catch (Exception ex) {
                            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
                            LymytzService.openExceptionDialog(ex.getMessage(), "Le virement n'a pas pus se réaliser ", "Erreur lors de la clôture ! Le service de virement ne s'est pas exécuté correctemment", Alert.AlertType.ERROR, ex);
                            PROGRESS_CLOSE.setVisible(false);
                        }
                    } else {
                        LymytzService.openAlertDialog("Aucune caisse de versement n'a été trouvé!", "Erreur Caisse", "Caisse de versement introuvable", Alert.AlertType.WARNING);
                    }
                } else {
                    if (header == null) {
                        LymytzService.openAlertDialog("Le Header n'a pas pu être charger", "Erreur fatal", "L'entête du document n'a pas pu être chargé", Alert.AlertType.ERROR);
                    } else {
                        LymytzService.openAlertDialog("Le Header est déjà clôturé", "Erreur lors de la clôture", "L'entete du document est déjà clôturé", Alert.AlertType.ERROR);
                    }
                }
            } else {
                LymytzService.openAlertDialog("Le montant à virer est incorrecte", "Erreur montant", "Montant à virer incorrect", Alert.AlertType.WARNING);
            }

        } catch (NumberFormatException ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            LymytzService.openExceptionDialog(ex.getMessage(), "Exception", "Erreur lors de la clôture !", Alert.AlertType.ERROR, ex);
        }
    }

    private void loadVersementFiche() {
        if (Constantes.asLong(this.idRemoteHeader)) {
            Thread t = new Thread(() -> {
                RQueryFactories dao = new RQueryFactories();
                //charge les versements de la fiche
                if (RQueryFactories.pingServer()) {
                    YvsComEnteteDocVente header = (YvsComEnteteDocVente) rq.findOneByNQ("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{idHeader});
                    String query = "SELECT y.numero_piece, y.montant FROM yvs_compta_caisse_piece_virement y LEFT JOIN yvs_compta_notif_versement_vente h ON h.piece=y.id "
                            + " WHERE h.id=? OR y.date_piece=?::date";
                    List<Object[]> re = dao.loadBySQLQuery(query, new Options[]{new Options(idRemoteHeader, 1), new Options(header.getDateEntete(), 2)});
                    Platform.runLater(() -> {
                        LIST_VER.getItems().clear();
                        Double soe = 0d;
                        for (Object[] l : re) {
                            LIST_VER.getItems().add((String) l[0] + "   --*--   " + ((l[1] != null) ? Constantes.nbf.format(Double.valueOf((String) l[1])) : "0"));
                            soe = soe + ((l[1] != null) ? Double.valueOf((String) l[1]) : 0d);
                        }
                        if ((avanceCmde + totalFacture) - soe > 0) {
                            TXT_MONTANT.setText(Constantes.nbf.format((avanceCmde + totalFacture) - soe));
                            TXT_MONTANT.selectAll();
                        }
                        MESSAGE.setText("");
                    });
                } else {
                    Platform.runLater(() -> {
                        MESSAGE.setText("Impossible de se connecter au serveur distant");
                    });
                }
            });
            t.start();
        }
    }

    @Override
    public void freeMemoryController() {
    }
}
