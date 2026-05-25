/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.service;

import com.lymytz.lymytzsell.business.helpers.KeyBoardAction;
import com.lymytz.lymytzsell.persistence.dao.Options;
import com.lymytz.lymytzsell.persistence.entity.YvsBasePointVente;
import com.lymytz.lymytzsell.persistence.entity.YvsComClient;
import com.lymytz.lymytzsell.persistence.entity.YvsComComerciale;
import com.lymytz.lymytzsell.persistence.entity.YvsComCommercialPoint;
import com.lymytz.lymytzsell.persistence.entity.YvsComCommercialVente;
import com.lymytz.lymytzsell.persistence.entity.YvsComDocVentes;
import com.lymytz.lymytzsell.persistence.dao.LocalQueryFactories;
import com.lymytz.lymytzsell.view.component.Onglets;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.synchro.ws.LivrerFactureHttpService;
import com.lymytz.lymytzsell.view.component.ToastService;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author LYMYTZ
 */
public class ServiceCreateFacture {

    LocalQueryFactories dao = new LocalQueryFactories();
    @Setter
    @Getter
    private Long result;
    @Setter
    @Getter
    private YvsComDocVentes facture;
    HomeCaisseController page;

    public ServiceCreateFacture(HomeCaisseController page) {
        this.page = page;
    }

    public void saveCurrentCommercial(YvsComDocVentes facture) {
        Optional.ofNullable(facture).ifPresent(fac -> {
            YvsBasePointVente pointDeVente = dao.findOneByNQ("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getId()});
            YvsComComerciale y = dao.findOneByNQ("YvsComComerciale.findByUser", new String[]{"user"}, new Object[]{UtilsProject.headerDoc.getCreneau().getUsers()});
            if (y == null && pointDeVente != null) { //Commerciale est celui rattaché au user en cours
                YvsComCommercialVente bean;
                double taux = !pointDeVente.getCommerciaux().isEmpty() ? ((double) 100 / pointDeVente.getCommerciaux().size()) : 0;
                for (YvsComCommercialPoint cp : pointDeVente.getCommerciaux()) {
                    bean = new YvsComCommercialVente();
                    bean.setFacture(fac);
                    bean.setTaux(taux);
                    bean.setResponsable(false);
                    bean.setCommercial(cp.getCommercial());
                    saveNewCommercial(bean);
                }
            } else {
                YvsComCommercialVente bean = new YvsComCommercialVente();
                bean.setCommercial(y);
                bean.setFacture(fac);
                bean.setResponsable(true);
                bean.setTaux(100d);
                saveNewCommercial(bean);
            }
        });
    }

    private void saveNewCommercial(YvsComCommercialVente y) {
        try {
            y.setAuthor(UtilsProject.currentUser);
            y.setDateSave(new Date());
            y.setDateUpdate(new Date());
            if (y.getId() == null || y.getId() < 1) {
                y.setId(null);
                y = dao.save1(y);
            } else {
                dao.update(y);
            }
            if (Boolean.TRUE.equals(y.getResponsable()) && y.getCommercial().getTiers() != null && (!y.getFacture().getTiers().getId().equals(y.getCommercial().getTiers().getId()))) {
                YvsComClient tiers = null;
                if (y.getCommercial().getTiers().getId() > 0 && (y.getCommercial().getTiers().getClients() != null && !y.getCommercial().getTiers().getClients().isEmpty())) {
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
        } catch (Exception ex) {
            Logger.getLogger(ServiceCreateFacture.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /*Gestion de la validation*/
    private boolean controleSaveReglement() {
        //2. Contrôle la caisse
        if (UtilsProject.caisse == null) {
            UtilsProject.caisse = dao.findOneByNQ("YvsBaseCaisse.findByCaissier", new String[]{"caissier"}, new Object[]{UtilsProject.currentUser.getUsers()});
            if (UtilsProject.caisse == null) {
                UtilsProject.caisse = dao.findOneByNQ("YvsBaseCaisseUser.findByUser", new String[]{"user"}, new Object[]{UtilsProject.currentUser.getUsers()});
            }
            if (UtilsProject.caisse == null) {
                LymytzService.openAlertDialog("Aucune caisse n'a été trouvé pour ce profil!", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
                return false;
            }
        }
        //3. Controle le mode de paiement
        if (UtilsProject.modeReg == null) {
            UtilsProject.modeReg = dao.findOneByNQ("YvsBaseModeReglement.findByDefault", new String[]{"type", "defaut", "actif"}, new Object[]{Constantes.MODE_PAIEMENT_ESPECE, true, true});
            if (UtilsProject.modeReg == null) {
                LymytzService.openAlertDialog("Aucun mode de paiement n'a été trouvé pour ce profil!", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
                return false;
            }
        }
        return true;
    }

    private boolean controleSaveFacture(YvsComDocVentes bean) {
        if (controleSaveReglement()) {
            //1. la fature dois être ditable, non encore livré, non encore soldé
            if (Constantes.ETAT_VALIDE.equals(bean.getStatut())) {
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
        Onglets onglet = (Onglets) page.TAB_FACTURES.getSelectionModel().getSelectedItem();
        Optional.ofNullable(onglet)
                .filter(ong -> controleSaveFacture(ong.getFacture()))
                .ifPresentOrElse(fac -> {
                            if (fac.getFacture().getTypeDoc().equals(Constantes.TYPE_FV)) {
                                page.openDlgCalculatrice(fac, "F", KeyBoardAction.VALIDER);
                            } else {
                                validationCommande(fac);
                            }
                        },
                        () -> Platform.runLater(() -> ToastService.show(page.getMainStage(), "Assurez-vous de selectionner une facture", 5000, ToastService.ToastType.ERROR)));
    }

    private void validationCommande(Onglets fac) {
        if (!Constantes.ETAT_REGLE.equals(fac.getFacture().getStatutRegle())) {
            if (fac.getFacture().getId() > 0) {
                saveOrGeneratedPaiement_(fac);
            } else {
                page.openDlgCalculatrice(fac, "F", KeyBoardAction.VALIDER);
            }
        } else {
            //TODO il est possible que ce code ne s'exécute jamais.
            //Appelle le service de validation des commandes
            Alert dlg = new Alert(Alert.AlertType.CONFIRMATION, "Confirmez vous la livraison de cette commande ?", new ButtonType("Oui"), new ButtonType("Non"));
            Optional<ButtonType> re = dlg.showAndWait();
            if ("Oui".equals(re.get().getText())) {
                //Vérifie que tout les règlements en rapport avec la commande sont synchronisé.
                if (verifieSynchroCommande(fac.getFacture())) {
                    //Appelle le service de validation des commandes
                    Livraison task = new Livraison(fac.getFacture());
                    task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (EventHandler<Event>) event -> LymytzService.success()
                    );
                    new Thread(task).start();
                } else {
                    LymytzService.openAlertDialog("Veuillez patienter...", "La commande n'est pas encore entièrement synchroniser sur le serveur distant", "Ressayer dans quelques minutes", Alert.AlertType.WARNING);
                }
            }

        }
    }

    public void saveOrGeneratedPaiement_(Onglets onglet) {
        if (controleSaveReglement()) {
            page.openDlgCalculatrice(onglet, onglet.getFacture().getTypeDoc().equals(Constantes.TYPE_BCV) ? "A" : "F", KeyBoardAction.REGLER);
        }
    }
    /*Cette methode vérifie que les élément de la commande ont tous été synchronisé avant*/

    private boolean verifieSynchroCommande(YvsComDocVentes commande) {
        Long re;
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
            if (re != null && re > 0) {
                return false;
            }
            //contenu doc vente
            queryControl = "select COUNT(*) FROM yvs_com_doc_ventes d LEFT JOIN yvs_synchro_listen_table t ON (t.id_source=d.id AND t.name_table=?) "
                    + "LEFT JOIN yvs_synchro_data_synchro s ON s.id_listen=t.id "
                    + "WHERE t.id_source IN (" + queryIds5 + ") AND s.id IS NULL";
            re = (Long) dao.findOneObjectBySQLQ(queryControl, new Options[]{new Options("yvs_com_contenu_doc_vente", 1), new Options(commande.getId(), 2)});
            if (re != null && re > 0) {
                return false;
            }
            //acompte
            queryControl = "select COUNT(*) FROM yvs_com_doc_ventes d LEFT JOIN yvs_synchro_listen_table t ON (t.id_source=d.id AND t.name_table=?) "
                    + "LEFT JOIN yvs_synchro_data_synchro s ON s.id_listen=t.id "
                    + "WHERE t.id_source IN (" + queryIds2 + ") AND s.id IS NULL";
            re = (Long) dao.findOneObjectBySQLQ(queryControl, new Options[]{new Options("yvs_compta_acompte_client", 1), new Options(commande.getId(), 2)});
            if (re != null && re > 0) {
                return false;
            }
            //piece reg
            queryControl = "select COUNT(*) FROM yvs_com_doc_ventes d LEFT JOIN yvs_synchro_listen_table t ON (t.id_source=d.id AND t.name_table=?) "
                    + "LEFT JOIN yvs_synchro_data_synchro s ON s.id_listen=t.id "
                    + "WHERE t.id_source IN (" + queryIds3 + ") AND s.id IS NULL";
            re = (Long) dao.findOneObjectBySQLQ(queryControl, new Options[]{new Options("yvs_compta_caisse_piece_vente", 1), new Options(commande.getId(), 2)});
            if (re != null && re > 0) {
                return false;
            }
            //Notif reg.
            queryControl = "select COUNT(*) FROM yvs_com_doc_ventes d LEFT JOIN yvs_synchro_listen_table t ON (t.id_source=d.id AND t.name_table=?) "
                    + "LEFT JOIN yvs_synchro_data_synchro s ON s.id_listen=t.id "
                    + "WHERE t.id_source IN (" + queryIds4 + ") AND s.id IS NULL";
            re = (Long) dao.findOneObjectBySQLQ(queryControl, new Options[]{new Options("yvs_compta_notif_reglement_vente", 1), new Options(commande.getId(), 2)});
            if (re != null && re > 0) {
                return false;
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceCreateFacture.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public static class Livraison extends Task<Boolean> {

        YvsComDocVentes commande;

        public Livraison(YvsComDocVentes commande) {
            this.commande = commande;
        }

        @Override
        protected Boolean call() throws Exception {
            LivrerFactureHttpService httpService = new LivrerFactureHttpService();
            httpService.livrerCommande(commande.getId());
            return true;
        }
    }

}
