/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsBaseCaisse;
import lymytz.dao.entity.YvsBaseModeReglement;
import lymytz.dao.entity.YvsBasePointVente;
import lymytz.dao.entity.YvsComClient;
import lymytz.dao.entity.YvsComComerciale;
import lymytz.dao.entity.YvsComCommercialPoint;
import lymytz.dao.entity.YvsComCommercialVente;
import lymytz.dao.entity.YvsComCreneauPoint;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.entity.YvsDictionnaire;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.composant.Onglets;
import lymytz.service.application.synchro.UtilEntityBase;
import lymytz.service.application.synchro.export.UtilExport;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.synchro.ws.ResultatAction;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.main.HomeCaisseController;
import org.json.JSONObject;

/**
 *
 * @author LYMYTZ
 */
public class ServiceCreateFacture implements Runnable {

    LQueryFactories dao = new LQueryFactories();
    YvsComClient client;
    String typeDoc;
    YvsDictionnaire adresse;
    String nameClient;
    Date dateLiv;
    String telephone;
    private Long result;
    private YvsComDocVentes facture;
    HomeCaisseController page;

    public ServiceCreateFacture(HomeCaisseController page) {
        this.page = page;
    }

    public ServiceCreateFacture(YvsComClient client, String typeDoc, YvsDictionnaire adresse, String nameClient, Date dateLiv, String telephone, HomeCaisseController page) {
        this.client = client;
        this.typeDoc = typeDoc;
        this.adresse = adresse;
        this.nameClient = nameClient;
        this.dateLiv = dateLiv;
        this.telephone = telephone;
        this.page = page;
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public YvsComDocVentes getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocVentes facture) {
        this.facture = facture;
    }

    @Override
    public void run() {
        if (UtilsProject.headerDoc != null) {
            if (!UtilsProject.headerDoc.getCloturer()) {
                if (!UtilsProject.headerDoc.getDateEntete().after(new Date())) {
                    String numDoc = typeDoc;
                    if (client != null && Constantes.asString(numDoc)) {
                        LQueryFactories rq = new LQueryFactories();
                        final YvsComDocVentes bean = new YvsComDocVentes();
                        bean.setNumDoc(numDoc);
                        bean.setLivraisonAuto(true);
                        bean.setAdresse(adresse);
                        bean.setAuthor(UtilsProject.currentUser);
                        bean.setCategorieComptable(client.getCategorieComptable());
                        bean.setClient(client);
                        bean.setCloturer(Boolean.FALSE);
                        bean.setCommision(0d);
                        bean.setDateSave(new Date());
                        bean.setDateUpdate(new Date());
                        bean.setDateSolder(new Date());
                        bean.setDepotLivrer(UtilsProject.depotLivraison);
                        bean.setTrancheLivrer(UtilsProject.headerDoc.getCreneau().getCreneauDepot().getTranche());
                        bean.setEnteteDoc(UtilsProject.headerDoc);
                        bean.setEtapeTotal(1);
                        bean.setHeureDoc(new Date());
                        bean.setModelReglement(UtilsProject.modelReg);
                        bean.setMouvStock(Boolean.TRUE);
                        bean.setNomClient(nameClient);
                        bean.setStatut(Constantes.ETAT_EDITABLE);
                        bean.setStatutLivre(Constantes.ETAT_ATTENTE);
                        bean.setStatutRegle(Constantes.ETAT_ATTENTE);
                        bean.setTypeDoc(typeDoc);
                        System.err.println(" ... Date Liv"+Constantes.dfD.format(dateLiv));
                        bean.setDateLivraisonPrevu(dateLiv);
                        bean.setTelephone(telephone);
                        bean.setOperateur(UtilsProject.currentUser.getUsers());
                        bean.setNature(Constantes.NATURE_DOC_VENTE_VENTE);
                        if (client.getSuiviComptable()) {
                            bean.setTiers(client);
                        }
                        if (bean.getTiers() == null) {
                            //ne pas enregistrer
                            Platform.runLater(() -> {
                                LymytzService.openAlertDialog("Le tiers rattaché à ce client n'existe pas !", "Action abandonné !", "Erreur ", Alert.AlertType.ERROR);
                            });
                            result = -1L;
                        }
                        bean.setId(Constantes.localId++);
                        setFacture(bean);
                        Platform.runLater(() -> {
                            page.BTN_REGLER.setVisible(bean.getStatutRegle().equals(Constantes.ETAT_REGLE));
                            page.BTN_LIVRER.setVisible(bean.getStatutLivre().equals(Constantes.ETAT_LIVRE));
                            page.LAB_REF_FACTURE.setText(numDoc + ":" + client.getCodeClient() + "--" + bean.getId());
                        });
                        result = bean.getId();
                    } else {
                        if (client == null) {
                            result = -2L;
                        } else {
                            result = -3L;
                        }
                    }
                } else {
                    result = -6L;
                }
            } else {
                result = -4L;
            }
        } else {
            result = -5L;
        }
        //display result
        switch (result.intValue()) {
            case -1:
                Platform.runLater(() -> {
                    LymytzService.openAlertDialog("Le tiers rattaché à ce client n'existe pas !", "Action abandonné !", "Erreur ", Alert.AlertType.ERROR);
                });
                break;
            case -2:
                Platform.runLater(() -> {
                    LymytzService.openAlertDialog("Génération de la facture non réussi !", "Erreur ", "Action abandonné !", Alert.AlertType.ERROR);
                });
                break;
            case -3:
                Platform.runLater(() -> {
                    LymytzService.openAlertDialog("Génération de a facture non réussi !", "Erreur ", "Le numéro de référence n'a pas pu être généré !", Alert.AlertType.ERROR);
                });
                break;
            case -4:
                Platform.runLater(() -> {
                    LymytzService.openAlertDialog("Génération de a facture non réussi !", "Erreur ", "Votre fiche de vente est déjà clôturé !", Alert.AlertType.ERROR);
                });
                break;
            case -5:
                Platform.runLater(() -> {
                    LymytzService.openAlertDialog("Génération de a facture non réussi !", "Erreur ", "Aucune entête n'a été trouvé !", Alert.AlertType.ERROR);
                });
                break;
            case -6:
                Platform.runLater(() -> {
                    LymytzService.openAlertDialog("Génération de a facture non réussi !", "Erreur ", "Vérifier la date de votre fiche !", Alert.AlertType.ERROR);
                });
                break;
            default:
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        page.displayDetailFacture(facture);
                        page.initTabPane(facture);
                        page.TEXT_FIND.setText("");
                        page.BTN_REGLER.setVisible(getFacture().getStatutRegle().equals(Constantes.ETAT_REGLE));
                        page.BTN_LIVRER.setVisible(getFacture().getStatutLivre().equals(Constantes.ETAT_LIVRE));
                        page.afterCreateFacture();
                        page.TEXT_FIND.requestFocus();
                        if (page.stageCreateFacture != null) {
                            page.stageCreateFacture.close();
                        }
                    }
                });
                break;
        }
    }

    public void saveCurrentCommercial(YvsComDocVentes facture) {
        if (UtilsProject.headerDoc != null) {
            if (UtilsProject.headerDoc.getCreneau() != null ? (UtilsProject.headerDoc.getCreneau().getId() != null ? UtilsProject.headerDoc.getCreneau().getId() > 0 : false) : false) {
                char commissionFor = 'C';
                YvsBasePointVente pv = null;
                YvsComCreneauPoint cr = UtilsProject.headerDoc.getCreneau().getCreneauPoint();
                if (cr != null ? cr.getId() > 0 : false) {
                    pv = cr.getPoint();
                    if (pv != null ? pv.getId() > 0 : false) {
                        pv = (YvsBasePointVente) dao.findOneByNQ("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{pv.getId()});
                        if (pv != null ? pv.getId() > 0 : false) {
                            commissionFor = pv.getCommissionFor();
                        }
                    }
                }
                YvsComComerciale y = (YvsComComerciale) dao.findOneByNQ("YvsComComerciale.findByUser", new String[]{"user"}, new Object[]{UtilsProject.headerDoc.getCreneau().getUsers()});
                if (y == null) { //Commerciale est celui rattaché au user en cours
                    YvsComCommercialVente bean;
                    double taux = pv.getCommerciaux().size() > 0 ? (100 / pv.getCommerciaux().size()) : 0;
                    for (YvsComCommercialPoint cp : pv.getCommerciaux()) {
                        bean = new YvsComCommercialVente();
                        bean.setFacture(facture);
                        bean.setTaux(taux);
                        bean.setResponsable(false);
                        bean.setCommercial(cp.getCommercial());
                        saveNewCommercial(bean);
                    }
                } else {
                    YvsComCommercialVente bean = new YvsComCommercialVente();
                    bean.setCommercial(y);
                    bean.setFacture(facture);
                    bean.setResponsable(true);
                    bean.setTaux(100d);
                    saveNewCommercial(bean);
                }
            }
        }
    }

    public void saveNewCommercial(YvsComCommercialVente y) {
        try {
            if (y != null ? y.getCommercial() != null : false) {
                y.setAuthor(UtilsProject.currentUser);
                y.setDateSave(new Date());
                y.setDateUpdate(new Date());
                if (y.getId() != null ? y.getId() < 1 : true) {
                    y.setId(null);
                    y = (YvsComCommercialVente) dao.save1(y);
                } else {
                    dao.update(y);
                }
                if (y.getResponsable()) {
                    if (y.getFacture() != null ? y.getCommercial().getTiers() != null : false) {
                        if (y.getFacture().getTiers() != null ? !y.getFacture().getTiers().getId().equals(y.getCommercial().getTiers().getId()) : true) {
                            YvsComClient tiers = null;
                            if (y.getCommercial().getTiers().getId() > 0 ? y.getCommercial().getTiers().getClients() != null ? !y.getCommercial().getTiers().getClients().isEmpty() : false : false) {
                                tiers = y.getCommercial().getTiers().getClients().get(0);
                            }
                            if (tiers != null) {
                                Options[] param = new Options[]{new Options(y.getFacture().getId(), 1), new Options(tiers.getId(), 2)};
                                String query = "update yvs_com_doc_ventes set tiers = null where id = ?";
                                if (tiers.getId() > 0) {
                                    param = new Options[]{new Options(y.getCommercial().getTiers().getId(), 1), new Options(y.getFacture().getId(), 2)};
                                }
                                dao.executeSqlQuery(query, param);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceCreateFacture.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /*Gestion de la validation*/
    private boolean controleSaveReglement(YvsComDocVentes bean) {
        //2. Contrôle la caisse
        if (UtilsProject.caisse == null) {
            UtilsProject.caisse = (YvsBaseCaisse) dao.findOneByNQ("YvsBaseCaisse.findByCaissier", new String[]{"caissier"}, new Object[]{UtilsProject.currentUser.getUsers()});
            if (UtilsProject.caisse == null) {
                UtilsProject.caisse = (YvsBaseCaisse) dao.findOneByNQ("YvsBaseCaisseUser.findByUser", new String[]{"user"}, new Object[]{UtilsProject.currentUser.getUsers()});
            }
            if (UtilsProject.caisse == null) {
                LymytzService.openAlertDialog("Aucune caisse n'a été trouvé pour ce profil!", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
                return false;
            }
        }
        //3. Controle le mode de paiement
        if (UtilsProject.modeReg == null) {
            UtilsProject.modeReg = (YvsBaseModeReglement) dao.findOneByNQ("YvsBaseModeReglement.findByDefault", new String[]{"type", "defaut", "actif"}, new Object[]{Constantes.MODE_PAIEMENT_ESPECE, true, true});
            if (UtilsProject.modeReg == null) {
                LymytzService.openAlertDialog("Aucun mode de paiement n'a été trouvé pour ce profil!", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
                return false;
            }
        }
        return true;
    }

    private boolean controleSaveFacture(YvsComDocVentes bean) {
        if (controleSaveReglement(bean)) {
            //1. la fature dois être ditable, non encore livré, non encore soldé
            if (bean.getStatut().equals(Constantes.ETAT_VALIDE) || bean.getStatut().equals(Constantes.ETAT_VALIDE)) {
                LymytzService.openAlertDialog("Impossible de modifier le statut de la facture !", "Erreur ", "Cette facture est déjà validé", Alert.AlertType.ERROR);
                return false;
            }
            //4. Controle le dépôt de Livraison
            if (UtilsProject.depotLivraison == null) {
                LymytzService.openAlertDialog("Aucun dépôt de livraison n'a été trouvé pour ce point de vente", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    public void valideFacture() {
        Onglets fac = (Onglets) page.TAB_FACTURES.getSelectionModel().getSelectedItem();
        if (fac != null) {
            if (controleSaveFacture(fac.getFacture())) { //1. Enregistrer le contenu
                if (fac.getFacture().getTypeDoc().equals(Constantes.TYPE_FV)) {
                    page.openDlgCalculatrice(fac, "F", "VALIDER");
                } else {
                    //cas de la commande
                    //On vérifie avant tout que la commande soit réglé
                    if (!fac.getFacture().getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                        if (fac.getFacture().getId() > 0) {
                            saveOrGeneratedPaiement_(fac);
                        } else {
                            page.openDlgCalculatrice(fac, "F", "VALIDER");
                        }
                    } else {
                        //Appelle le service de validation des commandes
                        Alert dlg = new Alert(Alert.AlertType.CONFIRMATION, "Confirmez vous la livraison de cette commande ?", new ButtonType("Oui"), new ButtonType("Non"));
                        Optional<ButtonType> re = dlg.showAndWait();
                        if (re.get().getText().equals("Oui")) {
                            //Vérifie que tout les règlements en rapport avec la commande sont synchronisé. 
                            if (verifieSynchroCommande(fac.getFacture())) {
                                //Appelle le service de validation des commandes
                                Livraison task = new Livraison(fac.getFacture());
                                task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<Event>() {

                                    @Override
                                    public void handle(Event event) {
                                        Boolean result = task.getValue();
                                        LymytzService.success();
                                    }
                                }
                                );
                                new Thread(task).start();
                            } else {
                                LymytzService.openAlertDialog("Veuillez patienter...", "La commande n'est pas encore entièrement synchroniser sur le serveur distant", "Ressayer dans quelques minutes", Alert.AlertType.WARNING);
                            }
                        }

                    }
                }
            }
        } else {
            LymytzService.openAlertDialog("Aucune facture selectionné !", "Erreur ", "Aucune facture n'a été initié !", Alert.AlertType.ERROR);
        }
    }

    public void saveOrGeneratedPaiement_(Onglets onglet) {
        if (controleSaveReglement(onglet.getFacture())) {
            page.openDlgCalculatrice(onglet, onglet.getFacture().getTypeDoc().equals(Constantes.TYPE_BCV) ? "A" : "F", "REGLER");
        }
    }
    /*Cette methode vérifie que les élément de la commande ont tous été synchronisé avant*/

    private boolean verifieSynchroCommande(YvsComDocVentes commande) {
        List<Long> ids;
        Long re = null;
        try {
            //doc vente
            String queryIds1 = "SELECT DISTINCT d.id FROM yvs_compta_notif_reglement_vente n INNER JOIN yvs_compta_caisse_piece_vente c ON c.id=n.piece_vente "
                    + "INNER JOIN yvs_com_doc_ventes d ON d.id=c.vente WHERE d.id=? ";
            //doc acompte
            String queryIds2 = "SELECT DISTINCT n.acompte  FROM yvs_compta_notif_reglement_vente n INNER JOIN yvs_compta_caisse_piece_vente c ON c.id=n.piece_vente "
                    + "INNER JOIN yvs_com_doc_ventes d ON d.id=c.vente WHERE d.id=? ";
            //pieces reg
            String queryIds3 = "SELECT DISTINCT n.piece_vente FROM yvs_compta_notif_reglement_vente n INNER JOIN yvs_compta_caisse_piece_vente c ON c.id=n.piece_vente "
                    + "INNER JOIN yvs_com_doc_ventes d ON d.id=c.vente WHERE d.id=? ";
            //Notif
            String queryIds4 = "SELECT DISTINCT n.id FROM yvs_compta_notif_reglement_vente n INNER JOIN yvs_compta_caisse_piece_vente c ON c.id=n.piece_vente "
                    + "INNER JOIN yvs_com_doc_ventes d ON d.id=c.vente WHERE d.id=? ";
            //contenu
            String queryIds5 = "SELECT DISTINCT c.id FROM yvs_com_contenu_doc_vente n WHERE c.doc_vente=? ";

            String queryControl = "select COUNT(*) FROM yvs_com_doc_ventes d LEFT JOIN yvs_synchro_listen_table t ON (t.id_source=d.id AND t.name_table=?) "
                    + "LEFT JOIN yvs_synchro_data_synchro s ON s.id_listen=t.id "
                    + "WHERE t.id_source IN (" + queryIds1 + ") AND s.id IS NULL";
            //doc vente
            re = (Long) dao.findOneObjectBySQLQ(queryControl, new Options[]{new Options("yvs_com_doc_ventes", 1), new Options(commande.getId(), 2)});
            if ((re != null) ? re > 0 : false) {
                return false;
            }
            //contenu doc vente
            queryControl = "select COUNT(*) FROM yvs_com_doc_ventes d LEFT JOIN yvs_synchro_listen_table t ON (t.id_source=d.id AND t.name_table=?) "
                    + "LEFT JOIN yvs_synchro_data_synchro s ON s.id_listen=t.id "
                    + "WHERE t.id_source IN (" + queryIds5 + ") AND s.id IS NULL";
            re = (Long) dao.findOneObjectBySQLQ(queryControl, new Options[]{new Options("yvs_com_contenu_doc_vente", 1), new Options(commande.getId(), 2)});
            if ((re != null) ? re > 0 : false) {
                return false;
            }
            //acompte
            queryControl = "select COUNT(*) FROM yvs_com_doc_ventes d LEFT JOIN yvs_synchro_listen_table t ON (t.id_source=d.id AND t.name_table=?) "
                    + "LEFT JOIN yvs_synchro_data_synchro s ON s.id_listen=t.id "
                    + "WHERE t.id_source IN (" + queryIds2 + ") AND s.id IS NULL";
            re = (Long) dao.findOneObjectBySQLQ(queryControl, new Options[]{new Options("yvs_compta_acompte_client", 1), new Options(commande.getId(), 2)});
            if ((re != null) ? re > 0 : false) {
                return false;
            }
            //piece reg
            queryControl = "select COUNT(*) FROM yvs_com_doc_ventes d LEFT JOIN yvs_synchro_listen_table t ON (t.id_source=d.id AND t.name_table=?) "
                    + "LEFT JOIN yvs_synchro_data_synchro s ON s.id_listen=t.id "
                    + "WHERE t.id_source IN (" + queryIds3 + ") AND s.id IS NULL";
            re = (Long) dao.findOneObjectBySQLQ(queryControl, new Options[]{new Options("yvs_compta_caisse_piece_vente", 1), new Options(commande.getId(), 2)});
            if ((re != null) ? re > 0 : false) {
                return false;
            }
            //Notif reg.
            queryControl = "select COUNT(*) FROM yvs_com_doc_ventes d LEFT JOIN yvs_synchro_listen_table t ON (t.id_source=d.id AND t.name_table=?) "
                    + "LEFT JOIN yvs_synchro_data_synchro s ON s.id_listen=t.id "
                    + "WHERE t.id_source IN (" + queryIds4 + ") AND s.id IS NULL";
            re = (Long) dao.findOneObjectBySQLQ(queryControl, new Options[]{new Options("yvs_compta_notif_reglement_vente", 1), new Options(commande.getId(), 2)});
            if ((re != null) ? re > 0 : false) {
                return false;
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceCreateFacture.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public class Livraison extends Task<Boolean> {

        YvsComDocVentes commande;

        public Livraison(YvsComDocVentes commande) {
            this.commande = commande;
        }

        @Override
        protected Boolean call() throws Exception {
            WsSynchro service = new WsSynchro();
            //construction de l'objet
            JSONObject entityJson = UtilExport.exportDocVente(commande, false, null);
            ResultatAction<YvsComDocVentes> result = service.livraisonDocVente(entityJson, "valide_doc_commande");
            if (result != null) {
                if (result.isResult()) {
                    YvsComDocVentes entity = null;
                    if (result.getData() != null) {
                        Gson gson = UtilEntityBase.createGson();
                        JsonObject jo = gson.toJsonTree(result.getData()).getAsJsonObject();
                        entity = gson.fromJson(jo.toString(), YvsComDocVentes.class);
                        //met à jour le statut livré de la commande
                        String query = "UPDATE yvs_com_doc_ventes SET statut=?, statut_livre=? WHERE id=? ";
                        dao.executeSqlQuery(query, new Options[]{new Options(entity.getStatut(), 1), new Options(entity.getStatutLivre(), 2), new Options(commande.getId(), 3)});
                    }
                }
            }
            return true;
        }

    }
}
