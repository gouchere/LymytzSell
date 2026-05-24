/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.utils;

import com.lymytz.lymytzsell.dao.Options;
import com.lymytz.lymytzsell.dao.UtilsBean;
import com.lymytz.lymytzsell.dao.entity.YvsAgences;
import com.lymytz.lymytzsell.dao.entity.YvsBaseCaisse;
import com.lymytz.lymytzsell.dao.entity.YvsBaseConditionnement;
import com.lymytz.lymytzsell.dao.entity.YvsBaseDepots;
import com.lymytz.lymytzsell.dao.entity.YvsBaseExercice;
import com.lymytz.lymytzsell.dao.entity.YvsBaseModeReglement;
import com.lymytz.lymytzsell.dao.entity.YvsBaseModelReglement;
import com.lymytz.lymytzsell.dao.entity.YvsComClient;
import com.lymytz.lymytzsell.dao.entity.YvsComEnteteDocVente;
import com.lymytz.lymytzsell.dao.entity.YvsComParametreVente;
import com.lymytz.lymytzsell.dao.entity.YvsDictionnaire;
import com.lymytz.lymytzsell.dao.entity.YvsGrhTrancheHoraire;
import com.lymytz.lymytzsell.dao.entity.YvsSocietes;
import com.lymytz.lymytzsell.dao.entity.YvsUsersAgence;
import com.lymytz.lymytzsell.dao.entity.service.EntityColumn;
import com.lymytz.lymytzsell.dao.query.LocalQueryFactories;
import com.lymytz.lymytzsell.dao.query.RQueryFactories;
import com.lymytz.lymytzsell.service.application.config.Properties;
import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import com.lymytz.lymytzsell.service.application.synchro.UtilEntityBase;
import com.lymytz.lymytzsell.synchro.ws.WsSynchro;
import com.lymytz.lymytzsell.view.LocalLoader;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ServerSocket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author LENOVO Regroupe les fonctionnalité statiques partagé de
 * l'application. Les actions sollicités par les autres classes
 */
public class UtilsProject {
    private static final Logger LOGGER = LogManager.getLogger(UtilsProject.class);

    private UtilsProject() {
        // no implementation
    }

    public static final AtomicLong localId = new AtomicLong(-9999);
    public static ServerSocket server;
    public static HomeCaisseController currentPage;
    public static Properties properties;
    public static Stage primaryStage;
    public static Stage stageConnect;
    public static YvsSocietes currentSociete;   //société locale
    public static YvsAgences currentAgence;              //agence locale
    public static YvsSocietes RcurrentSociete; // société distantes
    public static YvsAgences RcurrentAgence;    //agence distante
    public static YvsComEnteteDocVente headerDoc;
    public static YvsComParametreVente paramVente;
    public static List<YvsComEnteteDocVente> currentsHeaderDoc;
    public static YvsUsersAgence currentUser;
    public static Long remoteAuthor;
    public static Long ID_SERVEUR;
    public static boolean REPLICATION = false;
    public static Boolean APPLICATION_IHM = true;
    public static Boolean STOP_LISTEN = false;
    public static boolean modeAdmin = false;
    public static YvsBaseDepots depotLivraison;
    public static YvsGrhTrancheHoraire trancheLivraison;
    public static YvsBaseModeReglement modeReg;// ("ESPECE, CHEQUE, etc.")
    public static YvsBaseModelReglement modelReg;// (Nombre de tranche de paiement)
    public static YvsBaseCaisse caisse;
    public static YvsComClient clientDivers;
    public static List<YvsComClient> listClients;
    public static List<YvsDictionnaire> villes;
    public static List<YvsDictionnaire> secteurs;
    public static YvsDictionnaire defaultAdresse;
    public static String TYPE_RAPPORT_TICKET = "TICKET";
    public static String TYPE_RAPPORT_A5 = "A5";
    public static String TYPE_RAPPORT_A4 = "A4";
    public static String ENVIRONNEMENT;

    public static String generatedNumDoc(String type) {
        UtilsBean util = new UtilsBean();
        if (headerDoc != null && headerDoc.getCreneau() != null) {
            return util.genererReference(type,
                    headerDoc.getDateEntete(),
                    headerDoc.getCreneau().getCreneauPoint().getPoint().getId(),
                    Constantes.POINTVENTE,
                    "",
                    currentAgence
            );
        }
        return null;
    }

    public static boolean isReplicationMode() {
        return UtilsProject.REPLICATION && Constantes.APPS_MODE_BOTH.equals(PropertiesManager.getInstance().getVal(Constantes.KEY_MODE));
    }

    public static boolean isProductionEnv() {
        return (PropertiesManager.getInstance().getVal(Constantes.KEY_ENVIRONNEMENT).equals("PRODUCTION"));
    }

    public static boolean verifyDateVente(Date date) {
        LocalQueryFactories dao = new LocalQueryFactories();
        int ecart = -1;
        int nbFiches = -1;
        if (date == null || date.after(new Date())) {
            LymytzService.openAlertDialog("Vous ne pouvez enregistrer une fiche dans le future !", "Date fiche incorrecte", "Date fiche incorrecte !", Alert.AlertType.ERROR);
            return false;
        }
        List<YvsComParametreVente> lp = dao.loadByNamedQuery("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{UtilsProject.currentAgence}, 0, 1);
        if (lp != null && !lp.isEmpty()) {
            ecart = lp.get(0).getJourAnterieur();
            nbFiches = lp.get(0).getNbFicheMax();
        }
        // Vérifie le nombre de fiche non cloturé
        if (nbFiches > 0) {
            Long nb = (Long) dao.findOneObjectByNQ("YvsComEnteteDocVente.countFicheOpenByUsers", new String[]{"users"}, new Object[]{UtilsProject.currentUser.getUsers()});
            nb = nb != null ? nb : 0;
            if (nb >= nbFiches) {
                LymytzService.openAlertDialog("Vous avez trop de fiches non clôturées !", "Trop de fiches non clôturées", "Erreur d'ouverture de fiche", Alert.AlertType.ERROR);
                return false;
            }
        }
        return verifyDate(date, ecart);
    }

    public static boolean verifyDate(Date date, int ecart) {
        LocalQueryFactories dao = new LocalQueryFactories();
        String[] champ = new String[]{"dateJour"};
        Object[] val = new Object[]{date};
        YvsBaseExercice exo = dao.findOneByNQ("YvsBaseExercice.findActifByDate", champ, val);
        if (exo == null || exo.getId() < 1) {
            LymytzService.openAlertDialog("Le document doit etre enregistré dans un exercice actif", "Erreur facture", "Aucun exercice actif trouvé", Alert.AlertType.ERROR);
            return false;
        }
        if (Boolean.TRUE.equals(exo.getCloturer())) {
            LymytzService.openAlertDialog("Le document ne peut pas etre enregistré dans un exercice cloturé", "Erreur facture", "Exercice clôturé", Alert.AlertType.ERROR);
            return false;
        }
        if (ecart > 0) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Calendar d = Calendar.getInstance();
            d.setTime(date);
            d.set(Calendar.HOUR_OF_DAY, 0);
            d.set(Calendar.MINUTE, 0);
            d.set(Calendar.SECOND, 0);
            d.set(Calendar.MILLISECOND, 0);
            if (d.after(c)) {
                LymytzService.openAlertDialog("La date ne doit pas superieur à la date du jour", "Erreur facture", "Date de a fiche incorrecte", Alert.AlertType.ERROR);
                return false;
            }
            if (ecart > 0) {
                c.add(Calendar.DATE, -ecart);
                if (d.before(c)) {
                    LymytzService.openAlertDialog("La date ne doit pas excedé le nombre de jour de retrait prévu", "Erreur facture", "Date de a fiche incorrecte", Alert.AlertType.ERROR);
                    return false;
                }
            }
        }
        return true;
    }

    public static double getStocks(YvsBaseConditionnement c, long depot) {
        Double re;
        if (!UtilsProject.REPLICATION) {
            LocalQueryFactories rq = new LocalQueryFactories();
            re = (Double) (rq.findOneObjectBySQLQ("select public.get_stock_reel(?,?,?,?,?,?::date,?,?)", new Options[]{
                    new Options(c.getArticle().getId(), 1), new Options(0, 2), new Options(depot, 3), new Options(0, 4), new Options(0, 5),
                    new Options(UtilsProject.headerDoc.getDateEntete(), 6), new Options(c.getId(), 7), new Options(0, 8)
            }));
        } else {
            //récupère à partir d'une web service
            Long remoteDepot = UtilEntityBase.findIdRemoteData(Constantes.TABLE_DEPOT_CODE, depot);
            Long cond = UtilEntityBase.findIdRemoteData(Constantes.TABLE_CONDITIONNEMENT_CODE, c.getId());
            Long art = UtilEntityBase.findIdRemoteData(Constantes.TABLE_ARTICLE_CODE, c.getArticle().getId());
            re = WsSynchro.getStock(art, cond, remoteDepot, Constantes.dfD.format(UtilsProject.headerDoc.getDateEntete()));
        }
        return re != null ? re : 0;
    }

    public static double getPr(YvsBaseConditionnement c, long depot) {
        Double prixDeRevient;
        if (!UtilsProject.REPLICATION) {
            var queryFactorie = new LocalQueryFactories();
            prixDeRevient = (Double) (queryFactorie.findOneObjectBySQLQ("select public.get_pr(?,?,?,?::date,?)", new Options[]{
                    new Options(c.getArticle().getId(), 1), new Options(depot, 2), new Options(0, 3),
                    new Options(UtilsProject.headerDoc.getDateEntete(), 4), new Options(c.getId(), 5)
            }));
        } else {
            //récupère à partir d'une web service
            Long remoteIdDepot = UtilEntityBase.findIdRemoteData(Constantes.TABLE_DEPOT_CODE, depot);
            Long cond = UtilEntityBase.findIdRemoteData(Constantes.TABLE_CONDITIONNEMENT_CODE, c.getId());
            Long art = UtilEntityBase.findIdRemoteData(Constantes.TABLE_ARTICLE_CODE, c.getArticle().getId());
            prixDeRevient = WsSynchro.getPr(art, cond, remoteIdDepot, Constantes.dfD.format(UtilsProject.headerDoc.getDateEntete()));
        }
        return prixDeRevient != null ? prixDeRevient : 0;
    }

    public static String getVal(String key) {
        return PropertiesManager.getInstance().getVal(key);
    }

    /**
     * Force le rechargement des propriétés depuis le disque.
     * À appeler uniquement lors d'un rechargement explicite par l'utilisateur.
     */
    public static void reloadFilePropertie() {
        PropertiesManager.getInstance().reload();
        properties = PropertiesManager.getInstance().getProperties();
    }

    /**
     * Initialise les propriétés et {@code paramConnection} depuis le singleton {@link PropertiesManager}.
     * Idempotent : le chargement disque n'a lieu qu'une seule fois (dans le constructeur du singleton).
     */
    public static void loadFilePropertie() {
        PropertiesManager app = PropertiesManager.getInstance();
        properties = app.getProperties();
    }

    public static void chargerLesDonneesDistante() {
        loadFilePropertie();
        try {
            if (UtilsProject.properties != null) {
                if (properties.getIdRemoteScte() <= 0) {
                    LOGGER.info("loading remote société !");
                    RcurrentSociete = new YvsSocietes(properties.getIdRemoteScte());
                }
                REPLICATION = getReplication();
                LOGGER.info("Le mode réplication {}", (REPLICATION ? "est activé" : "n'est pas activé"));
                if (REPLICATION && properties.getCodeAgence() > 0) {
                    currentAgence = new YvsAgences(properties.getCodeAgence());
                    RcurrentAgence = new YvsAgences(UtilEntityBase.findIdRemoteData(Constantes.TABLE_AGENCE_CODE, currentAgence.getId()));
                }
            }
            if (REPLICATION) {
                ID_SERVEUR = RQueryFactories.getIdServer();
                if (ID_SERVEUR == null || ID_SERVEUR <= 0) {
                    //save adresse ip serveur
                    ID_SERVEUR = RQueryFactories.insertInfoServeur();
                }
            }
        } catch (NumberFormatException ex) {
            LOGGER.error("Fichier d'Environnement non trouvé !", ex);
        }
    }

    public static void initDataR() {
        if (properties.getIdRemoteScte() > 0) {
            RcurrentSociete = new YvsSocietes(properties.getIdRemoteScte());
        }
        if (currentAgence != null && currentAgence.getId() > 0) {
            RcurrentAgence = new YvsAgences(UtilEntityBase.findIdRemoteData(Constantes.TABLE_AGENCE_CODE, currentAgence.getId()));
        }
        ID_SERVEUR = RQueryFactories.getIdServer();
        if (ID_SERVEUR == null || ID_SERVEUR <= 0) {
            //save adresse ip serveur
            ID_SERVEUR = RQueryFactories.insertInfoServeur();
        }
    }

    public static void loadInitData() {
        LocalQueryFactories dao = new LocalQueryFactories();
        if (properties == null) {
            properties = new Properties();
        }
        loadFilePropertie();
        //Charge la liste des villes
        villes = dao.loadByNamedQuery("YvsDictionnaire.findVilles", new String[]{}, new Object[]{});
        //charge l'agence par defaut
        if (currentAgence != null && Constantes.asLong(currentAgence.getId())) {
            paramVente = dao.findOneByNQ("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{new YvsAgences(currentAgence.getId())});
            if (currentAgence == null) {
                Platform.runLater(() -> LymytzService.openAlertDialog("Impossible de trouver l'agence locale", "Erreur au demarrage", "Aucune Agence n'a été trouvé !", Alert.AlertType.ERROR));
            }
        }
        if (Constantes.asLong(properties.getCodeSociete())) {
            currentSociete = dao.findOneByNQ("YvsSocietes.findById", new String[]{"id"}, new Object[]{properties.getCodeSociete()});
            if (currentSociete == null) {
                Platform.runLater(() -> LymytzService.openAlertDialog("Impossible de trouver la société", "Erreur au demarrage", "Aucune société n'a été trouvé !", Alert.AlertType.ERROR));
            }
        }
        if (Constantes.asLong(properties.getClientDivers())) {
            clientDivers = dao.findOneByNQ("YvsComClient.findById", new String[]{"id"}, new Object[]{properties.getClientDivers()});
        }
        if (Constantes.asLong(properties.getSecteur())) {
            defaultAdresse = dao.findOneByNQ("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{properties.getSecteur()});
        }
        if (Constantes.asLong(properties.getModeReg())) {
            modeReg = dao.findOneByNQ("YvsBaseModeReglement.findById", new String[]{"id"}, new Object[]{properties.getModeReg()});
        }
        if (Constantes.asLong(properties.getModelReg())) {
            modelReg = dao.findOneByNQ("YvsBaseModelReglement.findById", new String[]{"id"}, new Object[]{properties.getModelReg()});
        }
        REPLICATION = getReplication();
        if (REPLICATION) {
            if (currentAgence != null && currentAgence.getId() > 0) {
                RcurrentAgence = new YvsAgences(UtilEntityBase.findIdRemoteData(Constantes.TABLE_AGENCE_CODE, currentAgence.getId()));
            }
            initDataR();
        } else {
            RcurrentAgence = currentAgence;
            RcurrentSociete = currentSociete;
        }
        listClients = dao.loadByNamedQuery("YvsComClient.findByActif", new String[]{"societe"}, new Object[]{UtilsProject.currentSociete});
    }

    public static Boolean getReplication() {
        //On est en environnement de replication si l'adresse distante est différente de l'adresse locale
        Properties properties = PropertiesManager.getInstance().getProperties();
        String host = properties.getSever();
        String hostR = properties.getSeverRemote();
        String db = properties.getDataBase();
        String dbR = properties.getDataBaseRemote();
        return !host.equals(hostR) || !db.equals(dbR);
    }

    public static ImageView buildImageProduit(String path) {
        ImageView img = new ImageView(new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/" + path))));
        img.setFitHeight(80);
        img.setFitWidth(70);
        return img;
    }

    public static String fillColumn(String type, ResultSet rs, int colIndex) {
        String re = null;
        try {
            if (type != null) {
                re = switch (type.toLowerCase()) {
                    case "bigint", "integer", "bigserial" ->
                            ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getLong(colIndex)) : "");
                    case "double precision" ->
                            ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getDouble(colIndex)) : "");
                    case "character varying", "timestamp" ->
                            ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getString(colIndex)) : "");
                    default -> ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getObject(colIndex)) : "");
                };
            }
        } catch (SQLException ex) {
            LOGGER.error(ex);
        }
        return re;
    }

   /* public static LQuery buildQueryRemote(String table, List<EntityColumn> colonnes, String[] colFilter, Long idListen) {
        return buildQueryRemote(table, colonnes, colFilter, true, idListen);
    }*/

    public static LQuery buildQueryRemote(String table, List<EntityColumn> colonnes, String[] colFilter, boolean withDefaultFilter, Long idListen) {
        String param = "";
        StringBuilder query = new StringBuilder("SELECT l.id, ");
        int i = 0;
        boolean hasAgence = false;
        boolean hasSociete = false;
        for (EntityColumn c : colonnes) {
            query.append((i == 0) ? "y." : ", y.").append(c.getColumnName());
            if (c.getColumnName().equals("agence")) {
                hasAgence = true;
            }
            if (c.getColumnName().equals("societe")) {
                hasSociete = true;
            }
            i++;
        }
        switch (table) {
            case Constantes.TABLE_USER_AGENCE_CODE:
                if (withDefaultFilter) {
                    query.append(" FROM ").append(table).append(" y INNER JOIN yvs_users u ON u.id=y.users INNER JOIN yvs_agences a ON a.id=u.agence INNER JOIN yvs_societes s ON s.id=a.societe LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("' AND l.action_name='INSERT') WHERE s.id=?");
                    param += "societe";
                } else {
                    query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("')");
                }
                break;
            case Constantes.TABLE_ARTICLE_CODE:
                if (withDefaultFilter) {
                    query.append(" FROM ").append(table).append(" y INNER JOIN yvs_base_famille_article f ON f.id=y.famille LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("' AND l.action_name='INSERT') WHERE f.societe=?");
                    if (!param.contains("societe")) {
                        param += "societe";
                    }
                } else {
                    query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("')");
                }
                break;
            case Constantes.TABLE_ARTICLE_DEPOT_CODE:
                if (withDefaultFilter) {
                    query.append(" FROM ").append(table).append(" y INNER JOIN yvs_base_depots d ON d.id=y.depot LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("' AND l.action_name='INSERT') WHERE d.agence=?");
                    if (!param.contains("agence")) {
                        param += "agence";
                    }
                } else {
                    query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("')");
                }
                break;
            case Constantes.TABLE_ARTICLE_POINT_CODE:
                if (withDefaultFilter) {
                    query.append(" FROM ").append(table).append(" y INNER JOIN yvs_base_point_vente p ON p.id=y.point LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("' AND l.action_name='INSERT') WHERE p.agence=?");
                    if (!param.contains("agence")) {
                        param += "agence";
                    }
                } else {
                    query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("')");
                }
                break;
            case Constantes.TABLE_CONDITIONNEMENT_POINT_CODE:
                if (withDefaultFilter) {
                    query.append(" FROM ").append(table).append(" y INNER JOIN yvs_base_article_point ap ON ap.id=y.article INNER JOIN yvs_base_point_vente p ON p.id=ap.point LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("' AND l.action_name='INSERT') WHERE p.agence=?");
                    if (!param.contains("agence")) {
                        param += "agence";
                    }
                } else {
                    query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("')");
                }
                break;
            case Constantes.TABLE_ELEMENT_REFERENCE_CODE:
            case Constantes.TABLE_DICTIONNAIRES_CODE:
                query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("' AND l.action_name='INSERT')");
                break;
            case Constantes.TABLE_CRENEAU_HORAIRE_USER_CODE:
                if (withDefaultFilter) {
                    query.append(" FROM ").append(table).append(" y INNER JOIN yvs_users_agence ua ON ua.id=y.author INNER JOIN yvs_agences a ON a.id=ua.agence LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("' AND l.action_name='INSERT') WHERE y.creneau_point IS NOT NULL AND a.id=? ");
                    param += "agence";
                } else {
                    query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("')");
                }
                break;
            default:
                if (withDefaultFilter) {
                    if (hasSociete) {
                        query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("' AND l.action_name='INSERT') WHERE y.societe=? ");
                        param += "societe";
                    } else if (hasAgence) {
                        query.append(" FROM ").append(table).append(" y INNER JOIN yvs_agences a ON a.id=y.agence LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("' AND l.action_name='INSERT') WHERE a.societe=? ");
                        param += "societe";
                    } else {
                        query.append(" FROM ").append(table).append(" y INNER JOIN yvs_users_agence ua ON ua.id=y.author INNER JOIN yvs_agences a ON a.id=ua.agence LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("' AND l.action_name='INSERT') WHERE a.societe=? ");
                        param += "societe";
                    }
                } else {
                    query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='").append(table).append("')");
                }
        }
        if (colFilter != null && colFilter.length > 0) {
            if (query.toString().contains("WHERE")) {
                if (Constantes.asLong(idListen)) {
                    query.append("  AND l.id=").append(idListen);
                }
                for (String s : colFilter) {
                    query.append(" AND ").append(s).append(" =? ");
                }
            } else {
                if (Constantes.asLong(idListen)) {
                    query.append(" WHERE l.id=").append(idListen);
                } else {
                    query.append(" WHERE y.id=y.id ");
                }
                for (String s : colFilter) {
                    query.append(" AND ").append(s).append(" =? ");
                }
            }
            param += "-" + Arrays.toString(colFilter);
        }
        return new LQuery(query.toString(), param);
    }

    public static String buildQueryCount(String table, List<EntityColumn> colonnes) {
        String query = "SELECT COUNT(*)";
        boolean hasAgence = false;
        boolean hasSociete = false;
        for (EntityColumn c : colonnes) {
            if (c.getColumnName().equals("agence")) {
                hasAgence = true;
            }
            if (c.getColumnName().equals("societe")) {
                hasSociete = true;
            }
        }
        switch (table) {
            case Constantes.TABLE_USER_AGENCE_CODE:
                query += " FROM " + table + " y INNER JOIN yvs_agences a ON a.id=y.agence INNER JOIN yvs_users u ON u.id=y.users INNER JOIN yvs_agences au ON au.id=u.agence INNER JOIN yvs_societes s ON s.id=au.societe WHERE s.id=?";
                break;
            case Constantes.TABLE_ARTICLE_CODE:
                query += " FROM " + table + " y INNER JOIN yvs_base_famille_article f ON f.id=y.famille WHERE f.societe=?";

                break;

            case Constantes.TABLE_ARTICLE_DEPOT_CODE:
                query += " FROM " + table + " y INNER JOIN yvs_base_depots d ON d.id=y.depot LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE d.agence=?";
                break;
            case Constantes.TABLE_ARTICLE_POINT_CODE:
                query += " FROM " + table + " y INNER JOIN yvs_base_point_vente p ON p.id=y.point LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE p.agence=?";
                break;
            case Constantes.TABLE_CONDITIONNEMENT_POINT_CODE:
                query += " FROM " + table + " y INNER JOIN yvs_base_article_point ap ON ap.id=y.article INNER JOIN yvs_base_point_vente p ON p.id=ap.point LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE p.agence=?";
                break;
            case Constantes.TABLE_ELEMENT_REFERENCE_CODE:
            case Constantes.TABLE_DICTIONNAIRES_CODE:
                query += " FROM " + table + " y ";
                break;
            case Constantes.TABLE_CRENEAU_HORAIRE_USER_CODE:
                query += " FROM " + table + " y INNER JOIN yvs_users_agence ua ON ua.id=y.author INNER JOIN yvs_agences a ON a.id=ua.agence WHERE a.id=? AND y.creneau_point IS NOT NULL";
                break;
            default:
                if (hasSociete) {
                    query += " FROM " + table + " y WHERE y.societe=? ";
                } else if (hasAgence) {
                    query += " FROM " + table + " y INNER JOIN yvs_agences a ON a.id=y.agence WHERE a.societe=? ";
                } else {
                    query += " FROM " + table + " y INNER JOIN yvs_users_agence ua ON ua.id=y.author INNER JOIN yvs_agences a ON a.id=ua.agence WHERE a.societe=? ";
                }
        }
        return query;
    }

    public static String buildQueryLocal(String table, List<EntityColumn> colonnes, String colFilter) {
        StringBuilder query = new StringBuilder("SELECT MAX(ds.id_distant), ");
        int i = 0;
        for (EntityColumn c : colonnes) {
            query.append((i == 0) ? "y." : ", y.").append(c.getColumnName());
            i++;
        }
        query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (y.id=l.id_source AND l.name_table='").append(table).append("') ").append("LEFT JOIN yvs_synchro_data_synchro ds ON ds.id_listen=l.id ");

        if (colFilter != null) {
            query.append(" WHERE y.").append(colFilter).append(" =? GROUP BY y.id ORDER BY y.id");
        } else {
            query.append("GROUP BY y.id ORDER BY y.id");
        }
        return query.toString();
    }

    public static String buildQueryToSynchro(String table, List<EntityColumn> colonnes, String colFilter) {
        StringBuilder query = new StringBuilder("SELECT ds.id id_ds, ds.id_distant,l.id id_listen, l.action_name, ");
        int i = 0;
        for (EntityColumn c : colonnes) {
            query.append((i == 0) ? "y." : ", y.").append(c.getColumnName());
            i++;
        }
        query.append(" FROM ").append(table).append(" y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table=?) ").append(" LEFT JOIN  yvs_synchro_data_synchro ds ON ds.id_listen=l.id LIMIT 2000");
        return query.toString();
    }

    public static Options[] buildValueParam(EntityColumn[] colonnes, int length) {
        Options[] parametres = new Options[length];// -1 pour exclure la colonne id
        int i = 0;
        for (EntityColumn c : colonnes) {
            if (!c.getColumnName().equals("id")) {
                parametres[i] = new Options(c.getColumnValue(), (i + 1));
                i++;
            }
        }
        return parametres;
    }

    public static String buildInsertQuery(String table, EntityColumn[] colonnes) {
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(table).append("(");
        int i = 0;
        for (EntityColumn c : colonnes) {
            if (!c.getColumnName().equals("id")) {
                if (i < (colonnes.length - 2)) {
                    sb.append(c.getColumnName()).append(",");
                } else {
                    sb.append(c.getColumnName()).append(")");
                }
                i++;
            }
        }
        sb.append(" VALUES (");
        i = 0;
        for (EntityColumn c : colonnes) {
            if (!c.getColumnName().equals("id")) {
                if (i < (colonnes.length - 2)) {
                    sb.append(getParam(c)).append(",");
                } else {
                    sb.append(getParam(c)).append(");");
                }
                i++;
            }
        }
        return sb.toString();
    }

    public static String buildUpdateQuery(String table, EntityColumn[] colonnes, Long key) {
        StringBuilder sb = new StringBuilder("UPDATE ").append(table).append(" SET ");
        int i = 0;
        Options[] parametres = new Options[colonnes.length];// -1 pour exclure la colonne id
        for (EntityColumn c : colonnes) {
            if (!c.getColumnName().equals("id")) {
                if (i < (colonnes.length - 2)) {
                    sb.append(c.getColumnName()).append("=").append(getParam(c)).append(",");
                } else {
                    sb.append(c.getColumnName()).append("=").append(getParam(c));
                }
                parametres[i] = new Options(c.getColumnValue(), (i + 1));
                i++;
            } else {
                parametres[colonnes.length - 1] = new Options(key, colonnes.length);
            }
        }
        sb.append(" WHERE id =? ;");
        return sb.toString();
    }

    public static Long getValKey(EntityColumn[] row) {
        if (row != null) {
            for (EntityColumn e : row) {
                if (e.getColumnName().equals("id")) {
                    return (Long) e.getColumnValue();
                }
            }
        }
        return null;
    }

    private static String getParam(EntityColumn c) {
        if (c.getTypeDatabase() != null) {
            switch (c.getTypeDatabase()) {
                case "int":
                case "int2":
                case "bigint":
                case "int4":
                case "int8":
                case "bigserial":
                case "serial":
                case "serial4":
                case "serial8":
                    return "?::bigint";
                case "double precision":
                case "float8":
                case "float4":
                    return "?::double precision";
                case "decimal":
                    return "?::decimal";
                case "character varying":
                case "varchar":
                    return "?";
                case "boolean":
                case "bool":
                    return "?::boolean";
                case "timestamp":
                case "timestamp without time zone":
                    return "?::timestamp without time zone";
                case "timestamp with time zone":
                    return "?::timestamp with time zone";
                case "date":
                    return "?::date";
                default:
                    return "?";

            }
        }
        return "?";
    }

    public static EntityColumn fillColumn(String type, EntityColumn c, ResultSet rs, int colIndex) {
        EntityColumn re = null;
        try {
            if (type != null) {
                re = new EntityColumn(c);
                re.setTypeDatabase(type.toLowerCase());
                switch (type.toLowerCase()) {
                    case "int":
                    case "int2":
                    case "bigint":
                    case "int4":
                    case "int8":
                    case "bigserial":
                    case "serial":
                    case "serial4":
                    case "serial8":
                        re.setColumnValue(rs.getLong(colIndex));
                        break;
                    case "double precision":
                        re.setColumnValue(rs.getDouble(colIndex));
                        break;
                    case "character varying":
                    case "varchar":
                        re.setColumnValue(rs.getString(colIndex));
                        break;
                    case "boolean":
                    case "bool":
                        re.setColumnValue(rs.getBoolean(colIndex));
                        break;
                    case "timestamp":
                        re.setColumnValue(rs.getDate(colIndex));
                        break;
                    default:
                        re.setColumnValue(rs.getObject(colIndex));
                        break;

                }
            }
        } catch (SQLException ex) {
            LOGGER.error(ex);
        }
        return re;
    }

    public static PreparedStatement applyParam(PreparedStatement st, Object[] params, int indice) {
        for (Object o : params) {
            try {
                switch (o.getClass().getSimpleName()) {
                    case "Long":
                        st.setLong(indice, Long.parseLong(o.toString()));
                        break;
                    case "String":
                        st.setString(indice, o.toString());
                        break;
                    default:
                        st.setObject(indice, o);
                        break;

                }
                indice++;
            } catch (SQLException ex) {
                LOGGER.error(ex);
            }
        }
        return st;
    }
}
