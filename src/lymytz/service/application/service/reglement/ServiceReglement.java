/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.service.reglement;

import java.util.Date;
import javafx.scene.control.Alert;
import javax.print.attribute.standard.Severity;
import lymytz.dao.UtilsBean;
import lymytz.dao.entity.YvsBaseCaisse;
import lymytz.dao.entity.YvsBaseModeReglement;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.entity.YvsComptaAcompteClient;
import lymytz.dao.entity.YvsComptaCaissePieceVente;
import lymytz.dao.entity.YvsComptaNotifReglementVente;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class ServiceReglement {

    LQueryFactories dao = new LQueryFactories();
    HomeCaisseController mainPage;

    public ServiceReglement() {
    }

    public ServiceReglement(HomeCaisseController mainPage) {
        this.mainPage = mainPage;
    }

    private boolean controleSaveReglement(YvsComDocVentes bean) {
        //2. Contrôle la caisse
        if (UtilsProject.caisse == null) {
            UtilsProject.caisse = (YvsBaseCaisse) dao.findOneByNQ("YvsBaseCaisse.findByCaissier", new String[]{"caissier"}, new Object[]{UtilsProject.currentUser.getUsers()});
            if (UtilsProject.caisse == null) {
                LymytzService.openAlertDialog("Aucune caisse n'a été trouvé pour ce poste!", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
                return false;
            }
        }
        //3. Controle le mode de paiement
        if (UtilsProject.modeReg == null) {
            UtilsProject.modeReg = (YvsBaseModeReglement) dao.findOneByNQ("YvsBaseModeReglement.findByDefault", new String[]{"type", "defaut", "actif"}, new Object[]{Constantes.MODE_PAIEMENT_ESPECE, true, true});
            LymytzService.openAlertDialog("Aucun mode de paiement n'a été trouvé pour ce poste!", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    public String saveReglementFacture(YvsComDocVentes facture, double montant, double montantRecu) {
        if (controleSaveReglement(facture)) {
            return saveReglement(facture, montant, montantRecu, facture.getMontantResteApayer());
        } else {
            return facture.getStatutRegle();
        }
    }

    public String saveReglementAvance(YvsComDocVentes facture, double montant, double montantRecu) {
        if (facture != null && controleSaveReglement(facture)) {
            double montantTotal = new UtilsBean().setMontantTotalDoc(facture, facture.getContenus());
            if (facture.getMontantResteApayer() > 0 && montant > 0) {
                // vérifier que le reste à payer de la facture sup. à 0
                if (facture.getMontantTotal() - facture.getMontantPlanifier() >= montant) {
                    //1.Save l'avance sur commande
                    //2.Save la pièce de règlement
                    //3.Save la liaison
                    return saveReglement(facture, montant, montantRecu, facture.getMontantResteApayer());
                } else {
                    LymytzService.openAlertDialog("Vous ne pouvez planifier un paiement au délà de la commande", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
                }
            } else {
                LymytzService.openAlertDialog("Le net de la facture ou le montant entré sont incorrect !", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
            }
        } else {
            LymytzService.openAlertDialog("Aucune selection n'a été trouvé", "Erreur ", "Impossible de terminer cette action", Alert.AlertType.ERROR);
        }
        return Constantes.ETAT_ENCOURS;
    }

    private String saveReglement(YvsComDocVentes facture, double montant, double montantRecu, double montantNet) {
        YvsComptaCaissePieceVente piece = new YvsComptaCaissePieceVente();
        YvsComptaAcompteClient avance = null;
        String numDoc = UtilsProject.generatedNumDoc(Constantes.TYPE_PC_NAME);
        if (numDoc != null ? !numDoc.isEmpty() : false) {
            if (facture.getTypeDoc().equals(Constantes.TYPE_BCV)) {
                avance = saveAcompteClient(facture, montant, UtilsProject.headerDoc.getDateEntete());
                if (avance == null ? true : avance.getId() <= 0) {
                    LogFiles.addLogInFile("L'avance de la commande " + facture.getNumDoc() + " n'a pas pue être généré !", Severity.ERROR);
                    return Constantes.ETAT_ATTENTE;
                }
            } 
            montantNet = montantNet - montant;            
            piece.setNumeroPiece(numDoc);
            piece.setAuthor(UtilsProject.currentUser);
            piece.setCaisse(UtilsProject.caisse);
            piece.setCaissier(UtilsProject.currentUser.getUsers());
            piece.setDatePaiement(UtilsProject.headerDoc.getDateEntete());
            piece.setDatePaimentPrevu(UtilsProject.headerDoc.getDateEntete());
            piece.setDateValide(UtilsProject.headerDoc.getDateEntete());
            piece.setDatePiece(UtilsProject.headerDoc.getDateEntete());
            piece.setDateSave(new Date());
            piece.setDateUpdate(new Date());
            piece.setModel(UtilsProject.modeReg);
            piece.setMontant(montant);
            piece.setReferenceExterne(facture.getNumDoc());
            piece.setStatutPiece(Constantes.STATUT_DOC_PAYER);
            piece.setVente(facture);
            piece.setValideBy(UtilsProject.currentUser.getUsers());
            piece.setMontantRecu(montantRecu);
            piece.setMouvement(Constantes.MOUV_CAISS_ENTREE.charAt(0));
            piece = (YvsComptaCaissePieceVente) dao.save1(piece);
            bindNotifReglement(avance, piece);
            System.err.println(" Montant Net... " + montantNet);            
            if (piece.getId() != null) {
                if (montantNet <= 0) {
                    return Constantes.ETAT_REGLE;
                } else {
                    return Constantes.ETAT_ENCOURS;
                }
            }
        }
        LogFiles.addLogInFile("Impossible de générer le numéro de la pièce de paiement!", Severity.ERROR);
        return Constantes.ETAT_ATTENTE;
    }

    private YvsComptaAcompteClient saveAcompteClient(YvsComDocVentes facture, double montant, Date date) {
        YvsComptaAcompteClient bean = new YvsComptaAcompteClient();
        String numDoc = UtilsProject.generatedNumDoc(Constantes.TYPE_PT_AVANCE_VENTE);
        if (numDoc != null ? !numDoc.isEmpty() : false) {
            bean.setAuthor(UtilsProject.currentUser);
            bean.setCaisse(UtilsProject.caisse);
            bean.setClient(facture.getClient());
            bean.setCommentaire("Avance pour la commande " + facture.getNumDoc());
            bean.setDateAcompte(date);
            bean.setDatePaiement(date);
            bean.setNature('A');
            bean.setDateSave(new Date());
            bean.setDateUpdate(new Date());
            bean.setModel(UtilsProject.modeReg);
            bean.setMontant(montant);
            bean.setNumRefrence(numDoc);
            bean.setStatut(Constantes.STATUT_DOC_PAYER);
            bean = (YvsComptaAcompteClient) dao.save1(bean);
            //Intruction du au trigger de la table
//            Long idExt=(Long)rq.loadObjectByNameQueries("YvsComptaMouvementCaisse.findIdExterneById", new String[]{"id"}, new Object[]{bean.getId()});
//            YvsComptaMouvementCaisse mvt = (YvsComptaMouvementCaisse) rq.findOneEntity("YvsComptaMouvementCaisse.findById", new String[]{"id"}, new Object[]{bean.getId()});
//            if (mvt != null) {
//                bean.setId(idExt);
//            }
//            bean=(YvsComptaAcompteClient)rq.findOneEntity("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{idExt});
            return bean;
        } else {
            LogFiles.addLogInFile("Le numéro de pièce d'avance client n'a pu être généré ", Severity.ERROR);
            return null;
        }
    }

    private boolean bindNotifReglement(YvsComptaAcompteClient avance, YvsComptaCaissePieceVente piece) {
        if (avance != null && piece != null) {
            if (avance.getId() > 0 && piece.getId() > 0) {
                // contrôle que le solde d'avance du client permet de créer la liason
                if (piece.getMontant() <= avance.getMontant()) {
                    YvsComptaNotifReglementVente notif = new YvsComptaNotifReglementVente();
                    notif.setAcompte(avance);
                    notif.setPieceVente(piece);
                    notif.setAuthor(UtilsProject.currentUser);
                    notif.setDateSave(new Date());
                    notif.setDateUpdate(new Date());
                    dao.save1(notif);
                } else {
                    LogFiles.addLogInFile("Le montant de l'acompte est inférieure au total de la pièce !", Severity.ERROR);
                }
            }
        }
        return true;
    }
}
