/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro.export;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.Severity;
import lymytz.dao.entity.YvsAgences;
import lymytz.dao.entity.YvsBaseArticles;
import lymytz.dao.entity.YvsBaseCaisse;
import lymytz.dao.entity.YvsBaseCategorieComptable;
import lymytz.dao.entity.YvsBaseConditionnement;
import lymytz.dao.entity.YvsBaseDepots;
import lymytz.dao.entity.YvsBaseModeReglement;
import lymytz.dao.entity.YvsBaseModelReglement;
import lymytz.dao.entity.YvsBaseTaxes;
import lymytz.dao.entity.YvsComClient;
import lymytz.dao.entity.YvsComContenuDocVente;
import lymytz.dao.entity.YvsComCreneauHoraireUsers;
import lymytz.dao.entity.YvsComCreneauPoint;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.dao.entity.YvsComTaxeContenuVente;
import lymytz.dao.entity.YvsComptaAcompteClient;
import lymytz.dao.entity.YvsComptaCaissePieceVente;
import lymytz.dao.entity.YvsComptaCaissePieceVirement;
import lymytz.dao.entity.YvsComptaNotifReglementVente;
import lymytz.dao.entity.YvsDictionnaire;
import lymytz.dao.entity.YvsGrhTrancheHoraire;
import lymytz.dao.entity.YvsSocietes;
import lymytz.dao.entity.YvsSynchroServeurs;
import lymytz.dao.entity.YvsUsers;
import lymytz.dao.entity.YvsUsersAgence;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.synchro.UtilEntityBase;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;
import org.json.JSONObject;

/**
 *
 * @author LYMYTZ
 */
public class UtilExport {

    public static LQueryFactories dao = new LQueryFactories();

    public UtilExport() {
    }

    public static YvsComCreneauHoraireUsers findCreneau(YvsUsers user, YvsComCreneauPoint point, Date date) {
        return (YvsComCreneauHoraireUsers) dao.findOneByNQ("YvsComCreneauHoraireUsers.findOne", new String[]{"users", "creneauPoint", "date"}, new Object[]{user, point, date});
    }

    /**
     * Vérifie que le header soit synchronisé et le faire le cas échéant
     *
     */
    private static Long findHeader(Long header) {
        if (Constantes.asLong(header)) {
            Long re = UtilEntityBase.findIdRemoteData(Constantes.TABLE_HEADER_DOC_CODE, header);
//            if (!Constantes.asLong(re)) {
//                //exporte le header
//                //trouve la ligne de header dans la table synchro_listen_table
//                YvsSynchroListenTable y = (YvsSynchroListenTable) dao.findOneByNQ("YvsSynchroListenTable.findByActionSource", new String[]{"nameTable", "idSource", "action"}, new Object[]{Constantes.TABLE_HEADER_DOC_CODE, header, Constantes.INSERT_ACTION});
//                if (y != null) {
//                    YvsComEnteteDocVente remoteHeader = new ExportService<YvsComEnteteDocVente>().synchroniseData(Constantes.TABLE_HEADER_DOC_CODE, y.getId(), header, Constantes.INSERT_ACTION, false);
//                    if (remoteHeader != null) {
//                        re = remoteHeader.getId();
//                    }
//                }
//            }
            return re;
        }
        return null;
    }

    public static YvsComEnteteDocVente buildExportEnteteDoc(YvsComEnteteDocVente localHead, Long idRemote) {
        YvsComEnteteDocVente re = new YvsComEnteteDocVente();
        if (UtilsProject.REPLICATION) {
            if (!Constantes.asLong(idRemote)) {
                re.setId(UtilEntityBase.findIdRemoteData(Constantes.TABLE_HEADER_DOC_CODE, localHead.getId()));
            } else {
                re.setId(idRemote);
            }
            re.setCloturer(localHead.getCloturer());
            re.setDateCloturer(localHead.getDateCloturer());
            re.setDateEntete(localHead.getDateEntete());
            re.setDateSave(localHead.getDateSave());
            re.setDateUpdate(localHead.getDateUpdate());
            re.setDateValider(localHead.getDateValider());
            re.setEtat(localHead.getEtat());
            re.setStatutLivre(localHead.getStatutLivre());
            re.setStatutRegle(localHead.getStatutRegle());
            re.setAgence(new YvsAgences(UtilEntityBase.findIdRemoteData(Constantes.TABLE_AGENCE_CODE, localHead.getAgence().getId())));
            re.getAgence().setSociete(new YvsSocietes(UtilEntityBase.findIdRemoteData(Constantes.TABLE_SOCIETE_CODE, localHead.getAgence().getSociete().getId())));
            re.setCreneau(new YvsComCreneauHoraireUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CRENEAU_HORAIRE_USER_CODE, localHead.getCreneau().getId())));
            re.getCreneau().setUsers(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localHead.getCreneau().getUsers().getId())));
            if (localHead.getValiderBy() != null) {
                re.setValiderBy(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localHead.getValiderBy().getId())));
            }
            if (localHead.getCloturerBy() != null) {
                re.setCloturerBy(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localHead.getCloturerBy().getId())));
            }
            re.setIdDistant(localHead.getId());
            re.setAdresseServeur(LymytzService.getMacAdress());
            re.setAuthor(new YvsUsersAgence(UtilsProject.remoteAuthor));
            re.setSynchroniser(true);
            return re;
        } else {
            return localHead;
        }
    }

    private static YvsComDocVentes buildExportDocVente(YvsComDocVentes localDoc, Long idRemote, boolean withChild) {
        if (UtilsProject.REPLICATION) {
            YvsComDocVentes re = new YvsComDocVentes(localDoc);
            re.setNumeroExterne(localDoc.getNumeroExterne());
            if (!Constantes.asLong(idRemote)) {
                re.setId(UtilEntityBase.findIdRemoteData(Constantes.TABLE_DOC_VENTE_CODE, localDoc.getId()));
            } else {
                re.setId(idRemote);
            }
            //traitement du header
            Long idRemoteHeader = findHeader(localDoc.getEnteteDoc().getId());
            if (!Constantes.asLong(idRemoteHeader)) {
                //arrête la synchro
                LogFiles.addLogInFile("Impossible de trouver l'En-tête de la facture", Severity.ERROR);
                return null;
            } else {
                re.setEnteteDoc(buildExportEnteteDoc(localDoc.getEnteteDoc(), idRemoteHeader));
            }
            if (localDoc.getAdresse() != null) {
                Long id = UtilEntityBase.findIdRemoteData(Constantes.TABLE_DICTIONNAIRES_CODE, localDoc.getAdresse().getId());
                if (Constantes.asLong(id)) {
                    re.setAdresse(new YvsDictionnaire(id));
                } else {
                    re.setAdresse(null);
                }
            }
            //catégorie comptable
            if (localDoc.getCategorieComptable() != null) {
                re.setCategorieComptable(new YvsBaseCategorieComptable(localDoc.getCategorieComptable()));
                re.getCategorieComptable().setId(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CATEGORIE_COMPTABLE_CODE, localDoc.getCategorieComptable().getId()));
            }
            re.setClient(new YvsComClient(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CLIENTS_CODE, localDoc.getClient().getId())));
            re.setCloturer(localDoc.getCloturer());
            if (localDoc.getCloturerBy() != null) {
                re.setCloturerBy(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localDoc.getCloturerBy().getId())));
            }
            if (localDoc.getLivreur() != null) {
                re.setLivreur(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localDoc.getLivreur().getId())));
            }
            if (localDoc.getValiderBy() != null) {
                re.setValiderBy(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localDoc.getValiderBy().getId())));
            }
            if (localDoc.getModelReglement() != null) {
                re.setModelReglement(new YvsBaseModelReglement(UtilEntityBase.findIdRemoteData(Constantes.TABLE_MODELE_DE_REGLEMENT_CODE, localDoc.getModelReglement().getId())));
            }
            re.setOperateur(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localDoc.getOperateur().getId())));
            if (localDoc.getTiers() != null) {
                re.setTiers(new YvsComClient(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CLIENTS_CODE, localDoc.getTiers().getId())));
            }
            if (localDoc.getTrancheLivrer() != null) {
                re.setTrancheLivrer(new YvsGrhTrancheHoraire(UtilEntityBase.findIdRemoteData(Constantes.TABLE_TRANCHE_HORAIRE_CODE, localDoc.getTrancheLivrer().getId())));
            }
            if (localDoc.getDepotLivrer() != null) {
                re.setDepotLivrer(new YvsBaseDepots(UtilEntityBase.findIdRemoteData(Constantes.TABLE_DEPOT_CODE, localDoc.getDepotLivrer().getId())));
            }
            if (localDoc.getAuthor() != null) {
                re.setAuthor(new YvsUsersAgence(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USER_AGENCE_CODE, localDoc.getAuthor().getId())));
            }
            re.setAdresseServeur(LymytzService.getMacAdress());
            re.setSynchroniser(true);
            re.setIdDistant(localDoc.getId());
            //traitement des Contenus
            if (withChild) {
                re.setContenus(new ArrayList<>());
                localDoc.getContenus().stream().forEach((c) -> {
                    re.getContenus().add(buildExportContenuDV(c, null, true));
                });
                //vérifier la conformité des ligne de contenues
                boolean ok = true;
                for (YvsComContenuDocVente c : re.getContenus()) {
                    if (c.getArticle() != null ? !Constantes.asLong(c.getArticle().getId()) : true) {
                        LogFiles.addLogInFile("Le contenu de la facture" + localDoc.getNumDoc() + " ne peut pas être synchronisé, l'id de l'article n'existe pas", Severity.ERROR);
                        ok = false;
                        break;
                    }
                    if (c.getConditionnement() != null ? !Constantes.asLong(c.getConditionnement().getId()) : true) {
                        ok = false;
                        LogFiles.addLogInFile("Le contenu de la facture " + localDoc.getNumDoc() + " ne peut pas être synchronisé, l'id du conditionnement n'existe pas", Severity.ERROR);
                        break;
                    }
                }
                //traitement des règlements
                if (ok) {
                    re.setReglements(new ArrayList<>());
                    localDoc.getReglements().stream().forEach((r) -> {
                        re.getReglements().add(buildExportPieceVente(r, null));
                    });
                    //vérifie la conformité des lignes de règlements
                    for (YvsComptaCaissePieceVente r : re.getReglements()) {
                        if (r.getCaisse() != null ? !Constantes.asLong(r.getCaisse().getId()) : true) {
                            LogFiles.addLogInFile("Le règlement de la facture" + localDoc.getNumDoc() + " ne peut pas être synchronisé, l'id de la caisse n'existe pas", Severity.ERROR);
                            ok = false;
                            break;
                        }
                        if (r.getModel() != null ? !Constantes.asLong(r.getModel().getId()) : true) {
                            LogFiles.addLogInFile("Le règlement de la facture " + localDoc.getNumDoc() + "ne peut pas être synchronisé, l'id du mode de règlement n'existe pas", Severity.ERROR);
                            ok = false;
                            break;
                        }
                    }
                    if (!ok) {
                        re.getReglements().clear();
                    }
                } else {
                    re.getContenus().clear();
                }
            } else {

            }
            return re;
        } else {
            YvsComDocVentes re = new YvsComDocVentes(localDoc, false);
            return re;
        }
    }

    public static YvsComptaCaissePieceVente buildExportPieceVente(YvsComptaCaissePieceVente localDoc, Long idRemote) {
        if (UtilsProject.REPLICATION) {
            YvsComptaCaissePieceVente re = new YvsComptaCaissePieceVente(localDoc);
            re.setId(UtilEntityBase.findIdRemoteData(Constantes.TABLE_PIECE_CAISSE_VENTE_CODE, localDoc.getId()));
            re.setAuthor(new YvsUsersAgence(UtilsProject.remoteAuthor));
            re.setCaisse(new YvsBaseCaisse(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CAISSES_CODE, localDoc.getCaisse().getId())));
            re.setCaissier(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localDoc.getCaissier().getId())));
            re.setModel(new YvsBaseModeReglement(UtilEntityBase.findIdRemoteData(Constantes.TABLE_MODE_DE_REGLEMENT_CODE, localDoc.getModel().getId())));
            re.setIdDistant(localDoc.getId());
            re.setAdresseServeur(LymytzService.getMacAdress());
            if (localDoc.getParent() != null) {
                Long idParent = UtilEntityBase.findIdRemoteData(Constantes.TABLE_PIECE_CAISSE_VENTE_CODE, localDoc.getParent().getId());
                re.setParent(Constantes.asLong(idParent) ? new YvsComptaCaissePieceVente(idParent) : null);
            }
            if (localDoc.getValideBy() != null) {
                re.setValideBy(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localDoc.getValideBy().getId())));
            }
            Long idVente = UtilEntityBase.findIdRemoteData(Constantes.TABLE_DOC_VENTE_CODE, localDoc.getVente().getId());
            re.setVente(Constantes.asLong(idVente) ? new YvsComDocVentes(idVente) : null);
            return re;
        } else {
            return localDoc;
        }
    }

    public static YvsComContenuDocVente buildExportContenuDV(YvsComContenuDocVente localDoc, Long idRemote, boolean insertWithDoc) {
        if (UtilsProject.REPLICATION) {
            YvsComContenuDocVente re = new YvsComContenuDocVente(localDoc);
            re.setArticle(new YvsBaseArticles(UtilEntityBase.findIdRemoteData(Constantes.TABLE_ARTICLE_CODE, localDoc.getArticle().getId())));
            if (localDoc.getArticleBonus() != null) {
                re.setArticleBonus(new YvsBaseArticles(UtilEntityBase.findIdRemoteData(Constantes.TABLE_ARTICLE_CODE, localDoc.getArticleBonus().getId())));
            }
            re.setConditionnement(new YvsBaseConditionnement(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CONDITIONNEMENT_CODE, localDoc.getConditionnement().getId())));
            if (localDoc.getConditionnementBonus() != null) {
                re.setConditionnementBonus(new YvsBaseConditionnement(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CONDITIONNEMENT_CODE, localDoc.getConditionnementBonus().getId())));
            }
            if (localDoc.getDepotLivraisonPrevu() != null) {
                re.setDepotLivraisonPrevu(new YvsBaseDepots(UtilEntityBase.findIdRemoteData(Constantes.TABLE_DEPOT_CODE, localDoc.getDepotLivraisonPrevu().getId())));
            }
            if (!insertWithDoc) {
                Long idDoc = UtilEntityBase.findIdRemoteData(Constantes.TABLE_DOC_VENTE_CODE, localDoc.getDocVente().getId());
                if (Constantes.asLong(idDoc)) {
                    re.setDocVente(new YvsComDocVentes(idDoc));
                } else {
                    re.setDocVente(null);
                    return re;
                }
            } else {
                re.setDocVente(null);
            }
            if (localDoc.getParent() != null) {
                re.setParent(new YvsComContenuDocVente(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CONTENT_DOC_VENTE_CODE, localDoc.getParent().getId())));
            }
            if (localDoc.getAuthor() != null) {
                re.setAuthor(new YvsUsersAgence(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USER_AGENCE_CODE, localDoc.getAuthor().getId())));
            }
            re.setIdDistant(localDoc.getId());
            re.setAdresseServeur(LymytzService.getMacAdress());
            //gérer les taxes
            re.setTaxes(new ArrayList<>());
            localDoc.getTaxes().stream().forEach((ct) -> {
                re.getTaxes().add(buildExportTaxeContent(ct, null));
            });
            return re;
        }
        return localDoc;
    }

    public static YvsComTaxeContenuVente buildExportTaxeContent(YvsComTaxeContenuVente localDoc, Long idRemote) {
        if (UtilsProject.REPLICATION) {
            YvsComTaxeContenuVente re = new YvsComTaxeContenuVente(localDoc);
            re.setContenu(new YvsComContenuDocVente(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CONTENT_DOC_VENTE_CODE, localDoc.getContenu().getId())));
            re.setTaxe(new YvsBaseTaxes(UtilEntityBase.findIdRemoteData(Constantes.TABLE_TAXES_CODE, localDoc.getTaxe().getId())));
            re.setAuthor(new YvsUsersAgence(UtilsProject.remoteAuthor));
            re.setIdDistant(localDoc.getId());
            re.setAdresseServeur(LymytzService.getMacAdress());
            return re;
        } else {
            return localDoc;
        }
    }

    public static YvsComptaAcompteClient buildExportAcompteClient(YvsComptaAcompteClient localDoc, Long idRemote) {
        if (UtilsProject.REPLICATION) {
            YvsComptaAcompteClient re = new YvsComptaAcompteClient(localDoc);
            if (!Constantes.asLong(idRemote)) {
                re.setId(UtilEntityBase.findIdRemoteData(Constantes.TABLE_ACOMPTE_CLIENT_CODE, localDoc.getId()));
            } else {
                re.setId(idRemote);
            }
            re.setCaisse(new YvsBaseCaisse(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CAISSES_CODE, localDoc.getCaisse().getId())));
            re.setClient(new YvsComClient(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CLIENTS_CODE, localDoc.getClient().getId())));
            re.setModel(new YvsBaseModeReglement(UtilEntityBase.findIdRemoteData(Constantes.TABLE_MODE_DE_REGLEMENT_CODE, localDoc.getModel().getId())));
            re.setAuthor(new YvsUsersAgence(UtilsProject.remoteAuthor));
            re.setAdresseServeur(LymytzService.getMacAdress());
            re.setIdDistant(localDoc.getId());
            YvsComptaNotifReglementVente notif;
            for (YvsComptaNotifReglementVente n : localDoc.getNotifications()) {
                notif = new YvsComptaNotifReglementVente(n);
                notif.setAcompte(null);
                notif.setAuthor(new YvsUsersAgence(UtilsProject.remoteAuthor));
                notif.setPieceVente(new YvsComptaCaissePieceVente(UtilEntityBase.findIdRemoteData(Constantes.TABLE_PIECE_CAISSE_VENTE_CODE, n.getPieceVente().getId())));
            }
            return re;
        } else {
            return localDoc;
        }
    }

    public static YvsComptaNotifReglementVente buildExportNotifRegVente(YvsComptaNotifReglementVente localDoc, Long idRemote) {
        if (UtilsProject.REPLICATION) {
            YvsComptaNotifReglementVente re = new YvsComptaNotifReglementVente(localDoc);
            if (!Constantes.asLong(idRemote)) {
                re.setId(UtilEntityBase.findIdRemoteData(Constantes.TABLE_NOTIF_ACOMPTE_CLIENT_CODE, localDoc.getId()));
            } else {
                re.setId(idRemote);
            }
            re.setAcompte(new YvsComptaAcompteClient(UtilEntityBase.findIdRemoteData(Constantes.TABLE_ACOMPTE_CLIENT_CODE, localDoc.getAcompte().getId())));
            re.setPieceVente(new YvsComptaCaissePieceVente(UtilEntityBase.findIdRemoteData(Constantes.TABLE_PIECE_CAISSE_VENTE_CODE, localDoc.getPieceVente().getId())));
            re.setAuthor(new YvsUsersAgence(UtilsProject.remoteAuthor));
            re.setAdresseServeur(LymytzService.getMacAdress());
            re.setIdDistant(localDoc.getId());
            return re;
        } else {
            return localDoc;
        }
    }

    public static YvsComptaCaissePieceVirement buildExportPieceVirement(YvsComptaCaissePieceVirement localDoc, Long idRemote) {
        if (UtilsProject.REPLICATION) {
            YvsComptaCaissePieceVirement re = new YvsComptaCaissePieceVirement(localDoc);
            re.setId(null);
            if (localDoc.getCaissierCible() != null) {
                re.setCaissierCible(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localDoc.getCaissierCible().getId())));
            }
            if (localDoc.getCaissierSource() != null) {
                re.setCaissierSource(new YvsUsers(UtilEntityBase.findIdRemoteData(Constantes.TABLE_USERS_CODE, localDoc.getCaissierSource().getId())));
            }
            re.setCible(new YvsBaseCaisse(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CAISSES_CODE, localDoc.getCible().getId())));
            re.setSource(new YvsBaseCaisse(UtilEntityBase.findIdRemoteData(Constantes.TABLE_CAISSES_CODE, localDoc.getSource().getId())));
            if (localDoc.getModel() != null) {
                re.setModel(new YvsBaseModeReglement(UtilEntityBase.findIdRemoteData(Constantes.TABLE_MODE_DE_REGLEMENT_CODE, localDoc.getModel().getId())));
            }
            re.setAuthor(new YvsUsersAgence(UtilsProject.remoteAuthor));
            re.setAdresseServeur(LymytzService.getMacAdress());
            re.setIdDistant(localDoc.getId() != null ? localDoc.getId() : 0L);
            return re;
        } else {
            return localDoc;
        }
    }

    public static JSONObject exportEnteteDoc(YvsComEnteteDocVente localHead, Long idListen) {
        //récupère la ligne de donnée
        try {
            YvsComEnteteDocVente re = null;
            boolean continue_ = true;
            if (localHead != null) {
                re = buildExportEnteteDoc(localHead, null);
                if (!Constantes.asLong(localHead.getId())) {
                    re.setId(UtilEntityBase.findIdRemoteData(Constantes.TABLE_HEADER_DOC_CODE, localHead.getId()));
                }
                //controle les clés étrangère avant de transformer l'élément JSON
                if (!Constantes.asLong(re.getAgence().getId())) {
                    LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localHead.getClass().getSimpleName() + " [" + localHead.getId() + "]" + " car agence est null", Severity.ERROR);
                    continue_ = false;
                }
                if (!Constantes.asLong(re.getAgence().getSociete().getId())) {
                    LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localHead.getClass().getSimpleName() + " [" + localHead.getId() + "]" + " car société est null", Severity.ERROR);
                    continue_ = false;
                }
                if (!Constantes.asLong(re.getCreneau().getId())) {
                    LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localHead.getClass().getSimpleName() + " " + localHead.getId() + "]" + " car le creneau est null", Severity.ERROR);
                    continue_ = false;
                }
                if (!Constantes.asLong(re.getCreneau().getUsers().getId())) {
                    LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localHead.getClass().getSimpleName() + " [" + localHead.getId() + "]" + " car le user est null", Severity.ERROR);
                    continue_ = false;
                }
                if (!Constantes.asLong(re.getAuthor().getId())) {
                    LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localHead.getClass().getSimpleName() + " [" + localHead.getId() + "]" + " car author est null", Severity.ERROR);
                    continue_ = false;
                }
                if (continue_) {
                    return UtilEntityBase.factoryJsonObject(re, YvsComEnteteDocVente.class);
                } else {
                    //notifie le listen
                    dao.notifyListenTable(idListen, "Le document de vente n'a pas été synchronisé !");
                    return null;
                }
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(UtilExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static JSONObject exportDocVente(YvsComDocVentes localDoc, boolean withChild, Long idListen) {
        //récupère la ligne de donnée
        YvsComDocVentes re = null;
        try {
            if (localDoc != null) {
                re = buildExportDocVente(localDoc, null, withChild);
                System.err.println(" ..... "+re);
                if (!withChild) {
                    if (re.getContenus() != null) {
                        re.getContenus().clear();
                    }
                    if (re.getReglements() != null) {
                        re.getReglements().clear();
                    }
                    if (re.getCommerciaux() != null) {
                        re.getCommerciaux().clear();
                    }
                }
                if (re != null) {
                    boolean continue_ = true;
                    if (!Constantes.asLong(re.getEnteteDoc().getId())) {
                        //notification interne
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car le header est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (!Constantes.asLong(re.getCategorieComptable().getId())) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car la catégorie comptable est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (!Constantes.asLong(re.getTiers().getId())) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car le tiers est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (!Constantes.asLong(re.getClient().getId())) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car le client est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (!Constantes.asLong(re.getAuthor().getId())) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car author est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (continue_) {
                        return UtilEntityBase.factoryJsonObject(re, YvsComDocVentes.class);
                    } else {
                        //notifie le listen
                        dao.notifyListenTable(idListen, "Le document de vente n'a pas été synchronisé !");
                        return null;
                    }
                }
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(UtilExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static JSONObject exportContentVente(YvsComContenuDocVente localDoc, Long idListen) {
        //récupère la ligne de donnée
        YvsComContenuDocVente re = null;
        try {
            if (localDoc != null) {
                re = buildExportContenuDV(localDoc, null, false);
                if (re != null) {
                    boolean continue_ = true;
                    if (re.getDocVente() != null ? !Constantes.asLong(re.getDocVente().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car le doc de vente est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (re.getArticle() != null ? !Constantes.asLong(re.getArticle().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car l'article est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (re.getConditionnement() != null ? !Constantes.asLong(re.getConditionnement().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car le conditionnement est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if ((re.getAuthor() != null) ? !Constantes.asLong(re.getAuthor().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car author est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (continue_) {
                        return UtilEntityBase.factoryJsonObject(re, YvsComContenuDocVente.class);
                    } else {
                        //notifie le listen
                        dao.notifyListenTable(idListen, "La ligne n'a pas été synchronisé !");
                        return null;
                    }
                }
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(UtilExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static JSONObject exportAcompteClient(YvsComptaAcompteClient localDoc, Long idListen) {
        //récupère la ligne de donnée
        YvsComptaAcompteClient re = null;
        try {
            if (localDoc != null) {
                re = buildExportAcompteClient(localDoc, null);
                if (re != null) {
                    boolean continue_ = true;
                    //contrôle des liaison obligatoire
                    if (re.getCaisse() != null ? !Constantes.asLong(re.getCaisse().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car la caisse est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (re.getClient() != null ? !Constantes.asLong(re.getClient().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car le client est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (re.getAuthor() != null ? !Constantes.asLong(re.getAuthor().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car l'auteur est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (continue_) {
                        return UtilEntityBase.factoryJsonObject(re, YvsComptaAcompteClient.class);
                    } else {
                        //notifie le listen
                        dao.notifyListenTable(idListen, "La ligne n'a pas été synchronisé !");
                        return null;
                    }
                }
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(UtilExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static JSONObject exportPieceCaisse(YvsComptaCaissePieceVente localDoc, Long idListen) {
        YvsComptaCaissePieceVente re = null;
        try {
            if (localDoc != null) {
                re = buildExportPieceVente(localDoc, null);
                if (re != null) {
                    boolean continue_ = true;
                    //contrôle des liaison obligatoire
                    if (re.getCaisse() != null ? !Constantes.asLong(re.getCaisse().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car la caisse est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (re.getVente() != null ? !Constantes.asLong(re.getVente().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car le doc de vente est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (re.getModel() != null ? !Constantes.asLong(re.getModel().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car le model doc est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (continue_) {
                        return UtilEntityBase.factoryJsonObject(re, YvsComptaCaissePieceVente.class);
                    } else {
                        //notifie le listen
                        dao.notifyListenTable(idListen, "La ligne n'a pas été synchronisé !");
                        return null;
                    }
                }
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(UtilExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static JSONObject exportNotifReglement(YvsComptaNotifReglementVente localDoc, Long idListen) {
        YvsComptaNotifReglementVente re = null;
        try {
            if (localDoc != null) {
                re = buildExportNotifRegVente(localDoc, null);
                if (re != null) {
                    boolean continue_ = true;
                    //contrôle des liaison obligatoire
                    if (re.getAcompte() != null ? !Constantes.asLong(re.getAcompte().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car la caisse est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (re.getPieceVente() != null ? !Constantes.asLong(re.getPieceVente().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car la caisse est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (re.getAuthor() != null ? !Constantes.asLong(re.getAuthor().getId()) : true) {
                        LogFiles.addLogInFile("Impossible de synchroniser l'objet  " + localDoc.getClass().getSimpleName() + " [" + localDoc.getId() + "]" + " car la caisse est null", Severity.ERROR);
                        continue_ = false;
                    }
                    if (continue_) {
                        return UtilEntityBase.factoryJsonObject(re, YvsComptaNotifReglementVente.class);
                    } else {
                        //notifie le listen
                        dao.notifyListenTable(idListen, "La ligne n'a pas été synchronisé !");
                        return null;
                    }
                }
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(UtilExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static JSONObject exportPieceVirement(YvsComptaCaissePieceVirement localDoc) {
        YvsComptaCaissePieceVirement re = null;
        try {
            if (localDoc != null) {
                re = buildExportPieceVirement(localDoc, null);
                if (re != null) {
                    // contrôle les laisons obligatoire
                    return UtilEntityBase.factoryJsonObject(re, YvsComptaCaissePieceVirement.class);
                }
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(UtilExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static JSONObject exportAdressServer() {
        //récupère la ligne de donnée
        YvsSynchroServeurs server = new YvsSynchroServeurs();
        server.setActif(true);
        server.setAdresseIp(LymytzService.getMacAdress());
        server.setNomServeur(LymytzService.getHostName());
        return UtilEntityBase.factoryJsonObject(server, YvsSynchroServeurs.class);
    }

}
