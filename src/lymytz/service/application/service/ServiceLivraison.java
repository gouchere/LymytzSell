/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsBaseArticleDepot;
import lymytz.dao.entity.YvsComContenuDocVente;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.entity.YvsComptaCaissePieceVente;
import lymytz.service.application.synchro.export.UtilExport;
import lymytz.service.utils.ConsUtil;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import static lymytz.service.utils.UtilsProject.getStocks;
import lymytz.service.utils.log.LogFiles;
import lymytz.synchro.ws.ResultatAction;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.main.HomeCaisseController;
import org.json.JSONObject;

/**
 *
 * @author LENOVO
 */
public class ServiceLivraison {

    HomeCaisseController mainPage;

    public ServiceLivraison() {
    }

    public ServiceLivraison(HomeCaisseController mainPage) {
        this.mainPage = mainPage;
    }

    public boolean saveLivraison(YvsComDocVentes facture, boolean message) {
        WsSynchro ws = new WsSynchro();
        //Construction de l'objet avec ses liaisons sur le serveur distant
        JSONObject entityJson = UtilExport.exportDocVente(facture, false, 0L);
        ResultatAction<YvsComDocVentes> result = ws.livraisonDocVente(entityJson, "livrer_facture_vente_caisse");
        if (result != null ? result.isResult() : false) {
            //met à jour le statut livré de la facture
            String query = "UPDATE yvs_com_doc_ventes SET statut_livre='L' WHERE id=? ";
            mainPage.dao.executeSqlQuery(query, new Options[]{new Options(facture.getId(), 1)});
            if (message) {
                Platform.runLater(() -> {
                    LymytzService.success();
                });
            }
        } else {
            Platform.runLater(() -> {
                LymytzService.openAlertDialog("", "", (result != null ? result.getMessage() : ""), Alert.AlertType.ERROR);
            });
        }
        return result != null ? result.isResult() : false;
    }

    public boolean transmisOrder(YvsComDocVentes commande) {
        try {
            List<YvsComContenuDocVente> contenus = mainPage.dao.loadByNamedQuery("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{commande});
            List<YvsComptaCaissePieceVente> reglements = mainPage.dao.loadByNamedQuery("YvsComptaCaissePieceVente.findByFacture", new String[]{"facture"}, new Object[]{commande});
            commande.setContenus(contenus);
            commande.setReglements(reglements);
            YvsComDocVentes facture = validerOrder(commande, true);
            if (facture != null ? facture.getId() > 0 : false) {
                //si la facture a été généré, il faut la comptabiliser
                Thread t = new Thread(() -> {
                    mainPage.comptabilise(facture.getId(), facture.getNumDoc());
                });
                t.start();
                if (controleLivraison(facture, false) && UtilsProject.trancheLivraison != null) {
                    boolean continu = false;
                    String num = UtilsProject.generatedNumDoc(Constantes.TYPE_BLV_NAME);
                    if (num != null ? num.trim().length() < 1 : true) {
                        return false;
                    }
                    YvsComDocVentes y = new YvsComDocVentes(facture);
                    if (y.getClient() == null) {
                        y.setClient(commande.getClient());
                    }
                    if (y.getCategorieComptable() == null) {
                        y.setCategorieComptable(commande.getCategorieComptable());
                    }
                    y.setContenus(new ArrayList<>());
                    y.setReglements(new ArrayList<>());
                    y.setDateSave(new Date());
                    y.setAuthor(UtilsProject.currentUser);
                    y.setTypeDoc(Constantes.TYPE_BLV);
                    y.setNumDoc(num);
                    y.setNumPiece("BLV N° " + facture.getNumDoc());
                    y.setLivreur(UtilsProject.currentUser.getUsers());
                    y.setDateLivraison(UtilsProject.headerDoc.getDateEntete());
                    y.setMouvStock(true);
                    y.setDocumentLie(new YvsComDocVentes(facture.getId()));
                    y.setHeureDoc(new Date());
                    y.setStatut(Constantes.ETAT_VALIDE);
                    y.setStatutLivre(Constantes.ETAT_LIVRE);
                    y.setStatutRegle(Constantes.ETAT_ATTENTE);
                    y.setAnnulerBy(null);
                    y.setCloturerBy(null);
                    y.setDateAnnuler(null);
                    y.setDateCloturer(null);
                    y.setValiderBy(UtilsProject.currentUser.getUsers());
                    y.setDateValider(new Date());
                    y.setEtapeTotal(0);
                    y.setDescription("Livraison Facture N° " + facture.getNumDoc() + " le " + Constantes.dfN1.format(UtilsProject.headerDoc.getDateEntete()) + " à " + Constantes.dfH.format(y.getHeureDoc()));
                    y.setOperateur(UtilsProject.currentUser.getUsers());
                    y.setId(null);
                    y = (YvsComDocVentes) mainPage.dao.save1(y);
                    if (y != null ? y.getId() > 0 : false) {
                        YvsComContenuDocVente c;
                        for (int i = 0; i < commande.getContenus().size(); i++) {
                            c = new YvsComContenuDocVente(commande.getContenus().get(i));
                            c.setDocVente(y);
                            c.setStatut(Constantes.ETAT_VALIDE);
                            c.setAuthor(UtilsProject.currentUser);
                            c.setId(null);
                            c = (YvsComContenuDocVente) mainPage.dao.save1(c);
                            y.getContenus().add(0, c);
                        }
                        if (facture.getReglements() != null ? facture.getReglements().isEmpty() : true) {
                            YvsComptaCaissePieceVente p;
                            for (int i = 0; i < commande.getReglements().size(); i++) {
                                p = commande.getReglements().get(i);
                                p.setVente(facture);
                                mainPage.dao.update(p);
                            }
                        }
                        continu = true;
                    }
                    if (continu) {
                        commande.setStatutLivre(Constantes.ETAT_LIVRE);
                        commande.setDateLivraison(UtilsProject.headerDoc.getDateEntete());
                        mainPage.dao.update(commande);
                    }
                    mainPage.dao.getEquilibreVente(facture.getId());
                    LymytzService.success();
                    return true;
                }
            } else {
                LymytzService.openAlertDialog("La facture n'a pas pu être généré !", "Erreur lors de la génération de la facture", "Erreur !", Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ServiceLivraison.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean controleLivraison(YvsComDocVentes facture, boolean silence) {
        double stock;
        for (YvsComContenuDocVente c : facture.getContenus()) {
            stock = getStocks(c.getConditionnement(), UtilsProject.depotLivraison.getId());
            //quantité déjà livré
            Double qteLivre = (Double) mainPage.dao.findOneObjectByNQ("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{c.getDocVente(), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, c.getArticle(), c.getConditionnement()});
            qteLivre = (qteLivre != null) ? qteLivre : 0;
            //trouve la quantité d'article facturé 
            Double qteFacture = (Double) mainPage.dao.findOneObjectByNQ("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{c.getDocVente(), c.getArticle(), c.getConditionnement()});
            qteFacture = (qteFacture != null) ? qteFacture : 0;
            //trouve la quantité bonus d'article facturé 
            Double qteBonusFacture = (Double) mainPage.dao.findOneObjectByNQ("YvsComContenuDocVente.findQteBonusByFacture", new String[]{"docVente", "article", "unite"}, new Object[]{c.getDocVente().getDocumentLie(), c.getArticle(), c.getConditionnement()});
            qteBonusFacture = (qteBonusFacture != null) ? qteBonusFacture : 0;
            if (c.getDocVente().getDocumentLie() != null ? !c.getDocVente().getDocumentLie().getStatutRegle().equals(Constantes.ETAT_REGLE) : true) {
                //si la facture n'est pas encore réglé, on ne dois pas inclure la quantité bonus dans la quantité à livrer
                if (c.getQuantite() > (qteFacture - qteLivre)) {
                    if (silence) {
                        LogFiles.addLogInFile(facture.getNumDoc() + ": Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !", null, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
                    } else {
                        LogFiles.addLogInFile(facture.getNumDoc() + ": Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !", null, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
                        LymytzService.openAlertDialog("Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !", "Erreur !", "Incohérence des quantités", Alert.AlertType.ERROR);
                    }
                    return false;
                }
            } else {
                if (c.getQuantite() > ((qteFacture + qteBonusFacture) - qteLivre)) {
                    if (silence) {
                        LogFiles.addLogInFile(facture.getNumDoc() + ": Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !", null, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
                    } else {
                        LogFiles.addLogInFile(facture.getNumDoc() + ": Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !", null, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
                        LymytzService.openAlertDialog("Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !", "Erreur !", "Incohérence des quantités", Alert.AlertType.ERROR);
                    }
                    return false;
                }
            }
            if (stock < c.getQuantite()) {
                if (c.getArticle().getMethodeVal() != null) {
                    if (!c.getArticle().getMethodeVal().equals(Constantes.CMP2)) {
                        if (silence) {
                            LogFiles.addLogInFile(facture.getNumDoc() + ": Impossible d'effectuer cette action... Car l'article " + c.getArticle().getDesignation() + " n'a plus un stock suffisant", null, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
                        } else {
                            LymytzService.openAlertDialog("Impossible d'effectuer cette action... Car l'article " + c.getArticle().getRefArt() + " n'a plus un stock suffisant", "Erreur !", "Stock insuffisant", Alert.AlertType.ERROR);
                        }
                        return false;
                    }
                } else {
                    if (silence) {
                        LogFiles.addLogInFile(facture.getNumDoc() + ": Impossible d'effectuer cette action... Car l'article " + c.getArticle().getDesignation() + " n'a plus un stock suffisant", null, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
                    } else {
                        LogFiles.addLogInFile(facture.getNumDoc() + ": Impossible d'effectuer cette action... Car l'article " + c.getArticle().getDesignation() + " n'a plus un stock suffisant", null, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
                        LymytzService.openAlertDialog("Impossible d'effectuer cette action... Car l'article " + c.getArticle().getRefArt() + " n'a plus un stock suffisant", "Erreur !", "Stock insuffisant", Alert.AlertType.ERROR);
                    }
                    return false;
                }
            }
            YvsBaseArticleDepot y = (YvsBaseArticleDepot) mainPage.dao.findOneByNQ("YvsBaseArticleDepot.findByArticleDepot", new String[]{"article", "depot"}, new Object[]{c.getArticle(), UtilsProject.depotLivraison});
            if (y != null ? y.getId() < 1 : true) {
                if (silence) {
                    LogFiles.addLogInFile(facture.getNumDoc() + ": Impossible d'effectuer cette action... Car le dépôt " + UtilsProject.depotLivraison.getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation(), null, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
                } else {
                    LogFiles.addLogInFile(facture.getNumDoc() + ": Impossible d'effectuer cette action... Car le dépôt " + UtilsProject.depotLivraison.getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation(), null, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, null);
                    LymytzService.openAlertDialog("Impossible d'effectuer cette action... Car le dépôt " + UtilsProject.depotLivraison.getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation(), "Erreur !", "Paramétrage dépôt incorrecte", Alert.AlertType.ERROR);
                }
                return false;
            }
        }
        return true;
    }

    /*Gérer la livraison des Bons de commandes*/
    public YvsComDocVentes validerOrder(YvsComDocVentes commande, boolean msg) {

        if (commande == null) {
            if (msg) {
                LymytzService.openAlertDialog("Vous devez selectionner la facture", "Erreur selection", "erreur!!!", Alert.AlertType.ERROR);
            }
            return null;
        }
        if (commande.getReglements().isEmpty()) {
            if (msg) {
                LymytzService.openAlertDialog("Aucun règlement n'a été trouvé pour cette commande", "Impossible de continuer", "Erreur Règlement!!!", Alert.AlertType.ERROR);
            }
            return null;
        }
        YvsComDocVentes y = null;
        try {
            boolean continu = false;
            if (commande.getClient() != null ? commande.getClient().getId() < 1 : true) {
                if (msg) {
                    LymytzService.openAlertDialog("Aucun client n'a été trouvé !", "Erreur selection", "erreur!!!", Alert.AlertType.ERROR);
                }
                return null;
            }
            //la commande doit être réglé
            if (!commande.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                if (msg) {
                    LymytzService.openAlertDialog("La commande n'est pas réglé !", "La commande doit être réglé", "erreur!!!", Alert.AlertType.ERROR);
                }
                return null;
            }
            if (commande.getOnfacture().equals(Constantes.ETAT_VALIDE)) {
                if (msg) {
                    LymytzService.openAlertDialog("Cette commande a déja été facturé", "Erreur selection", "La facture N°" + commande.getDocuments().get(0).getNumDoc() + " a été trouvé !", Alert.AlertType.ERROR);
                }
                for (YvsComDocVentes d : commande.getDocuments()) {
                    if (d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        return d;
                    }
                }
            }
            if (commande.getOnfacture().equals(Constantes.ETAT_ENCOURS)) {
                if (msg) {
                    LymytzService.openAlertDialog("Cette commande est déja rattaché à une facture", "Erreur selection", "erreur!!!", Alert.AlertType.ERROR);
                }
                for (YvsComDocVentes d : commande.getDocuments()) {
                    return d;
                }
            }
            if (commande.getContenus() != null ? !commande.getContenus().isEmpty() : false) {
                if (commande.getEnteteDoc() != null) {
                    String num = UtilsProject.generatedNumDoc(Constantes.TYPE_FV_NAME);
                    if (num != null ? num.trim().length() < 1 : true) {
                        return null;
                    }
                    y = new YvsComDocVentes(commande);
                    y.setContenus(new ArrayList<>());
                    y.setReglements(new ArrayList<>());
                    y.setEnteteDoc(UtilsProject.headerDoc);
                    y.setDateSave(new Date());
                    y.setDateUpdate(new Date());
                    y.setAuthor(UtilsProject.currentUser);
                    y.setTypeDoc(Constantes.TYPE_FV);
                    y.setNumDoc(num);
                    y.setNumPiece("FV N° " + commande.getNumDoc());
                    y.setDepotLivrer(UtilsProject.depotLivraison);
                    y.setTrancheLivrer(UtilsProject.headerDoc.getCreneau().getCreneauDepot().getTranche());
                    y.setLivreur(UtilsProject.currentUser.getUsers());
                    y.setDateLivraison(commande.getDateLivraisonPrevu());
                    y.setDocumentLie(new YvsComDocVentes(commande));
                    y.getDocumentLie().getContenus().clear();
                    y.setHeureDoc(new Date());
                    y.setStatut(Constantes.ETAT_VALIDE);
                    y.setStatutLivre(Constantes.ETAT_ATTENTE);
                    y.setStatutRegle(Constantes.ETAT_ATTENTE);
                    y.setValiderBy(UtilsProject.currentUser.getUsers());
                    y.setDateValider(UtilsProject.headerDoc.getDateEntete());
                    y.setDescription("Facturation de la commande N° " + commande.getNumDoc() + " le " + Constantes.dfN1.format(new Date()) + " à " + Constantes.dfH.format(y.getHeureDoc()));
                    y.setOperateur(UtilsProject.currentUser.getUsers());
                    y.setId(null);
                    y = (YvsComDocVentes) mainPage.dao.save1(y);
                    if (y != null ? y.getId() > 0 : false) {
                        YvsComContenuDocVente c;
                        for (int i = 0; i < commande.getContenus().size(); i++) {
                            c = new YvsComContenuDocVente(commande.getContenus().get(i));
                            c.setDocVente(y);
                            c.setStatut(Constantes.ETAT_VALIDE);
                            c.setAuthor(UtilsProject.currentUser);
                            c.setId(null);
                            mainPage.dao.save1(c);
                            y.getContenus().add(c);
                        }
                        YvsComptaCaissePieceVente p;
                        for (int i = 0; i < commande.getReglements().size(); i++) {
                            p = commande.getReglements().get(i);
                            p.setVente(y);
                            mainPage.dao.update(p);
                            y.getReglements().add(p);
                        }
                        commande.getDocuments().add(y);
                        continu = true;
                    }
                }
            }
            if (continu ? changeStatut_(Constantes.ETAT_VALIDE, y) : false) {
                commande.setCloturer(false);
                commande.setAnnulerBy(null);
                commande.setValiderBy(UtilsProject.currentUser.getUsers());
                commande.setCloturerBy(null);
                commande.setDateAnnuler(null);
                commande.setDateCloturer(null);
                commande.setDateValider(new Date());
                commande.setAuthor(UtilsProject.currentUser);
                //évitons les action crud en cascade
                List<YvsComContenuDocVente> ltemp = new ArrayList<>(commande.getContenus());
                commande.getContenus().clear();
                commande.getReglements().clear();
                commande.getDocuments().clear();
                mainPage.dao.update(commande);
                commande.setContenus(ltemp);
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(ServiceLivraison.class.getName()).log(Level.SEVERE, null, ex);
        }
        return y;
    }

    public boolean changeStatut_(String etat, YvsComDocVentes doc_) {
        if (!etat.equals("")) {
            if (doc_.getCloturer()) {
                LymytzService.openAlertDialog("Ce document est vérouillé", "Erreur selection", "erreur!!!", Alert.AlertType.ERROR);
                return false;
            }
            String rq = "UPDATE yvs_com_doc_ventes SET statut = '" + etat + "'  WHERE id=?";
            Options[] param = new Options[]{new Options(doc_.getId(), 1)};
            mainPage.dao.executeSqlQuery(rq, param);
            return true;
        }
        return false;
    }

}
