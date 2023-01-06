/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.print.attribute.standard.Severity;
import lymytz.dao.Options;
import lymytz.dao.ParamConnection;
import lymytz.dao.UtilsBean;
import lymytz.dao.entity.YvsAgences;
import lymytz.dao.entity.YvsBaseCaisse;
import lymytz.dao.entity.YvsBaseConditionnement;
import lymytz.dao.entity.YvsBaseDepots;
import lymytz.dao.entity.YvsBaseExercice;
import lymytz.dao.entity.YvsBaseModeReglement;
import lymytz.dao.entity.YvsBaseModelReglement;
import lymytz.dao.entity.YvsComClient;
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.dao.entity.YvsComParametreVente;
import lymytz.dao.entity.YvsDictionnaire;
import lymytz.dao.entity.YvsGrhTrancheHoraire;
import lymytz.dao.entity.YvsSocietes;
import lymytz.dao.entity.YvsUsersAgence;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.application.synchro.UtilEntityBase;
import lymytz.service.start.StartController;
import static lymytz.service.utils.UtilsProject.REPLICATION;
import static lymytz.service.utils.UtilsProject.RcurrentSociete;
import lymytz.service.utils.log.LogFiles;
import lymytz.synchro.ws.WsSynchro;
import lymytz.view.LocalLoader;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO Regroupe les fonctionnalité statiques partagé de
 * l'application. Les actions sollicités par les autres classes
 */
public class UtilsProject {

    LQueryFactories dao = new LQueryFactories();

    public UtilsProject() {
    }

    public static ServerSocket server;
    public static HomeCaisseController currentPage;
    public static ParamConnection paramConnection;
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
    public static Boolean REPLICATION = false;
    public static Boolean APPLICATION_IHM = true;
    public static Boolean STOP_LISTEN = false;
    public static Boolean MODE_ADMIN = false;
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
    public static Date dateConnect = new Date();
    public static String TYPE_RAPPORT_TICKET = "TICKET";
    public static String TYPE_RAPPORT_A5 = "A5";
    public static String TYPE_RAPPORT_A4 = "A4";
    public static String ENVIRONNEMENT;

    public static String generatedNumDoc(String type) {
        UtilsBean util = new UtilsBean();
        if (headerDoc != null ? headerDoc.getCreneau() != null : false) {
            String reference = util.genererReference(type, headerDoc.getDateEntete(), headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), Constantes.POINTVENTE, "", currentAgence);
            return reference;
        }
        return null;
    }

    public static String generatedNumDocBL() {
        UtilsBean util = new UtilsBean();
        if (headerDoc != null ? headerDoc.getCreneau() != null : false) {
            String reference = util.genererReference(Constantes.TYPE_BLV_NAME, headerDoc.getDateEntete(), headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), Constantes.DEPOT, "", currentAgence);
            return reference;
        }
        return null;
    }

    public static boolean verifyDateVente(Date date) {
        LQueryFactories dao = new LQueryFactories();
        int ecart = -1;
        int nbFiches = -1;
        if (date != null ? date.after(new Date()) : true) {
            LymytzService.openAlertDialog("Vous ne pouvez enregistrer une fiche dans le future !", "Date fiche incorrecte", "Date fiche incorrecte !", Alert.AlertType.ERROR);
            return false;
        }
        List<YvsComParametreVente> lp = dao.loadByNamedQuery("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{UtilsProject.currentAgence}, 0, 1);
        if ((lp != null) ? !lp.isEmpty() : false) {
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
        LQueryFactories dao = new LQueryFactories();
//        if (autoriser("com_save_hors_limit")) {
//            return true;
//        }
        String[] champ = new String[]{"dateJour"};
        Object[] val = new Object[]{date};
        YvsBaseExercice exo = (YvsBaseExercice) dao.findOneByNQ("YvsBaseExercice.findActifByDate", champ, val);
        if (exo != null ? exo.getId() < 1 : true) {
            LymytzService.openAlertDialog("Le document doit etre enregistré dans un exercice actif", "Erreur facture", "Aucun exercice actif trouvé", Alert.AlertType.ERROR);
            return false;
        }
        if (exo.getCloturer()) {
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

    public static String generatedNumDocCaisse() {
        UtilsBean util = new UtilsBean();
        if (headerDoc != null ? headerDoc.getCreneau() != null : false) {
            String reference = util.genererReference(Constantes.TYPE_PT_NAME, headerDoc.getDateEntete(), headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), Constantes.DEPOT, "", currentAgence);
            return reference;
        } else {
            return null;
        }
    }

    public static double getStocks(YvsBaseConditionnement c, long depot) {
        Double re = 0d;
        if (!UtilsProject.REPLICATION) {
            LQueryFactories rq = new LQueryFactories();
            re = (Double) (rq.findOneObjectBySQLQ("select public.get_stock_reel(?,?,?,?,?,?::date,?,?)", new Options[]{
                new Options(c.getArticle().getId(), 1), new Options(0, 2), new Options(depot, 3), new Options(0, 4), new Options(0, 5),
                new Options(UtilsProject.headerDoc.getDateEntete(), 6), new Options(c.getId(), 7), new Options(0, 8)
            }));
        } else {
            //récupère à partir d'une web service
            Long Rdepot = UtilEntityBase.findIdRemoteData(Constantes.TABLE_DEPOT_CODE, depot);
            Long cond = UtilEntityBase.findIdRemoteData(Constantes.TABLE_CONDITIONNEMENT_CODE, c.getId());
            Long art = UtilEntityBase.findIdRemoteData(Constantes.TABLE_ARTICLE_CODE, c.getArticle().getId());
            re = WsSynchro.getStock(art, cond, Rdepot, Constantes.dfD.format(UtilsProject.headerDoc.getDateEntete()));
        }
        return re;
    }
    
    public static double getPr(YvsBaseConditionnement c, long depot) {
        Double re = 0d;
        if (!UtilsProject.REPLICATION) {
            LQueryFactories rq = new LQueryFactories();
            re = (Double) (rq.findOneObjectBySQLQ("select public.get_pr(?,?,?,?::date,?)", new Options[]{
                new Options(c.getArticle().getId(), 1), new Options(depot, 2), new Options(0, 3), 
                new Options(UtilsProject.headerDoc.getDateEntete(), 4), new Options(c.getId(), 5)
            }));
        } else {
            //récupère à partir d'une web service
            Long Rdepot = UtilEntityBase.findIdRemoteData(Constantes.TABLE_DEPOT_CODE, depot);
            Long cond = UtilEntityBase.findIdRemoteData(Constantes.TABLE_CONDITIONNEMENT_CODE, c.getId());
            Long art = UtilEntityBase.findIdRemoteData(Constantes.TABLE_ARTICLE_CODE, c.getArticle().getId());
            re = WsSynchro.getPr(art, cond, Rdepot, Constantes.dfD.format(UtilsProject.headerDoc.getDateEntete()));
        }
        return re;
    }

    public static String getVal(String key) {
        try {
            String re = UtilsProject.properties.getProperty(key);
            return (Constantes.asString(re) ? re : null);
        } catch (Exception ex) {
            LogFiles.addLogInFile("Récupération de la date erronée !", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_USER, ex);
        }
        return null;
    }

    public static void loadFilePropertie() {
        try {
            if (UtilsProject.properties == null) {
                UtilsProject.properties = new Properties();
            }
            try (FileInputStream fis = LymytzService.getPropertiesFileInputStream()) {
                UtilsProject.properties.load(fis);
                if (paramConnection == null) {
                    paramConnection = new ParamConnection();
                }
                paramConnection.setCheminPhotos(getVal(Constantes.KEY_PATH));
                String s = getVal(Constantes.KEY_CLIENT_DIVERS);
                paramConnection.setClientDivers(Constantes.asString(s) ? Long.valueOf(s) : 0l);
                paramConnection.setCodeAgence((Constantes.asString(getVal(Constantes.KEY_LOCAL_AGENCE))) ? Long.valueOf(getVal(Constantes.KEY_LOCAL_AGENCE)) : 0l);
                paramConnection.setCodeSociete((Constantes.asString(getVal(Constantes.KEY_LOCAL_SOCIETE))) ? Long.valueOf(getVal(Constantes.KEY_LOCAL_SOCIETE)) : 0l);
                paramConnection.setDataBase(getVal(Constantes.KEY_LOCAL_DB_NAME));
                paramConnection.setDataBaseRemote(getVal(Constantes.KEY_REMOTE_DB_NAME));
                paramConnection.setHostWeb(getVal(Constantes.KEY_WEB_HOST));
                paramConnection.setIdRemoteScte((Constantes.asString(getVal(Constantes.KEY_REMOTE_SOCIETE))) ? Long.valueOf(getVal(Constantes.KEY_REMOTE_SOCIETE)) : 0l);
                paramConnection.setModeReg((Constantes.asString(getVal(Constantes.KEY_MODE_REGLEMENT))) ? Long.valueOf(getVal(Constantes.KEY_MODE_REGLEMENT)) : 0l);
                paramConnection.setModelReg((Constantes.asString(getVal(Constantes.KEY_MODEL_REGLEMENT))) ? Long.valueOf(getVal(Constantes.KEY_MODEL_REGLEMENT)) : 0l);
                paramConnection.setP_default((Constantes.asString(getVal(Constantes.KEY_USE_PRINTER))) ? Boolean.valueOf(getVal(Constantes.KEY_USE_PRINTER)) : true);
                paramConnection.setP_height((Constantes.asString(getVal(Constantes.KEY_PAPER_HEIGHT))) ? Double.valueOf(getVal(Constantes.KEY_PAPER_HEIGHT)) : 0d);
                paramConnection.setP_width((Constantes.asString(getVal(Constantes.KEY_PAPER_WIDTH))) ? Double.valueOf(getVal(Constantes.KEY_PAPER_WIDTH)) : 0d);
                paramConnection.setP_mb((Constantes.asString(getVal(Constantes.KEY_PAPER_M_BOTOM))) ? Double.valueOf(getVal(Constantes.KEY_PAPER_M_BOTOM)) : 0d);
                paramConnection.setP_ml((Constantes.asString(getVal(Constantes.KEY_PAPER_M_LEFT))) ? Double.valueOf(getVal(Constantes.KEY_PAPER_M_LEFT)) : 0d);
                paramConnection.setP_mr((Constantes.asString(getVal(Constantes.KEY_PAPER_M_RIGHT))) ? Double.valueOf(getVal(Constantes.KEY_PAPER_M_RIGHT)) : 0d);
                paramConnection.setP_mt((Constantes.asString(getVal(Constantes.KEY_PAPER_M_TOP))) ? Double.valueOf(getVal(Constantes.KEY_PAPER_M_TOP)) : 0d);
                paramConnection.setPassword(getVal(Constantes.KEY_LOCAL_PASSWORD));
                paramConnection.setPasswordRemote(getVal(Constantes.KEY_REMOTE_PASSWORD));
                paramConnection.setPort(getVal(Constantes.KEY_LOCAL_PORT));
                paramConnection.setPortRemote(getVal(Constantes.KEY_REMOTE_PORT));
                paramConnection.setPortWeb(getVal(Constantes.KEY_WEB_PORT));
                paramConnection.setSever(getVal(Constantes.KEY_LOCAL_HOST));
                paramConnection.setSeverRemote(getVal(Constantes.KEY_REMOTE_HOST));
                paramConnection.setTypeRapport(getVal(Constantes.KEY_TYPE_PRINT));
                paramConnection.setUseCodeBarre((Constantes.asString(getVal(Constantes.KEY_USE_CODE_BARRE))) ? Boolean.valueOf(getVal(Constantes.KEY_USE_CODE_BARRE)) : true);
                paramConnection.setUsePrinter((Constantes.asString(getVal(Constantes.KEY_USE_PRINTER))) ? Boolean.valueOf(getVal(Constantes.KEY_USE_PRINTER)) : true);
                paramConnection.setUsers(getVal(Constantes.KEY_LOCAL_USERS));
                paramConnection.setUsersRemote(getVal(Constantes.KEY_REMOTE_USERS));
            }
        } catch (IOException ex) {
            Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
            LogFiles.addLogInFile("Fichier d'Environnement non trouvé !", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);

        }
    }

    public static void loadInitDataR() {
        loadFilePropertie();
        try {
            if (UtilsProject.properties != null) {
                if (Constantes.asString(properties.getProperty(Constantes.KEY_REMOTE_SOCIETE))) {
                    LogFiles.addLogInFile("loading remote société !", Severity.REPORT);
                    RcurrentSociete = new YvsSocietes(Long.valueOf(properties.getProperty(Constantes.KEY_REMOTE_SOCIETE)));
                }
                REPLICATION = getReplication();
            }
            ID_SERVEUR = RQueryFactories.getIdServer();
            if (ID_SERVEUR != null ? ID_SERVEUR <= 0 : true) {
                //save adresse ip serveur
                ID_SERVEUR = RQueryFactories.insertInfoServeur();
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
            LogFiles.addLogInFile("Fichier d'Environnement non trouvé !", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
        }
    }

    public static void initDataR() {
        if (Constantes.asString((String) properties.get(Constantes.KEY_REMOTE_SOCIETE))) {
            RcurrentSociete = new YvsSocietes(Long.valueOf(properties.getProperty(Constantes.KEY_REMOTE_SOCIETE)));
        }
        ID_SERVEUR = RQueryFactories.getIdServer();
        if (ID_SERVEUR != null ? ID_SERVEUR <= 0 : true) {
            //save adresse ip serveur
            ID_SERVEUR = RQueryFactories.insertInfoServeur();
        }
    }

    public static void loadInitData() {
        /**
         * Exécuté une action qui controle la conformité des propriétés
         * necessaire dans le fichier .properties*
         */
        LQueryFactories dao = new LQueryFactories();
        if (paramConnection == null) {
            paramConnection = new ParamConnection();
        }
        loadFilePropertie();
        if (Constantes.asString((String) properties.get(Constantes.KEY_LOCAL_AGENCE))) {
            paramConnection.setCodeAgence(Long.valueOf(properties.get(Constantes.KEY_LOCAL_AGENCE).toString()));
        }
        if (Constantes.asString((String) properties.get(Constantes.KEY_LOCAL_SOCIETE))) {
            paramConnection.setCodeSociete(Long.valueOf(properties.get(Constantes.KEY_LOCAL_SOCIETE).toString()));
        }
        //Charge la liste des villes
        villes = dao.loadByNamedQuery("YvsDictionnaire.findVilles", new String[]{}, new Object[]{});
        //charge l'agence par defaut
        if (currentAgence != null ? Constantes.asLong(currentAgence.getId()) : false) {
          paramVente = (YvsComParametreVente) dao.findOneByNQ("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{new YvsAgences(paramConnection.getCodeAgence())});
            if (currentAgence == null) {
                LymytzService.openAlertDialog("Impossible de trouver l'agence locale", "Erreur au demarrage", "Aucune Agence n'a été trouvé !", Alert.AlertType.ERROR);
            }
        }
        if (Constantes.asLong(paramConnection.getCodeSociete())) {
            currentSociete = (YvsSocietes) dao.findOneByNQ("YvsSocietes.findById", new String[]{"id"}, new Object[]{paramConnection.getCodeSociete()});
            if (currentSociete == null) {
                LymytzService.openAlertDialog("Impossible de trouver la société", "Erreur au demarrage", "Aucune société n'a été trouvé !", Alert.AlertType.ERROR);
            }
        }
        if (Constantes.asLong(paramConnection.getClientDivers())) {
            clientDivers = (YvsComClient) dao.findOneByNQ("YvsComClient.findById", new String[]{"id"}, new Object[]{paramConnection.getClientDivers()});
        }
        if (Constantes.asLong(paramConnection.getSecteur())) {
            defaultAdresse = (YvsDictionnaire) dao.findOneByNQ("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{paramConnection.getSecteur()});
        }
        if (Constantes.asLong(paramConnection.getModeReg())) {
            modeReg = (YvsBaseModeReglement) dao.findOneByNQ("YvsBaseModeReglement.findById", new String[]{"id"}, new Object[]{paramConnection.getModeReg()});
        }
        if (Constantes.asLong(paramConnection.getModelReg())) {
            modelReg = (YvsBaseModelReglement) dao.findOneByNQ("YvsBaseModelReglement.findById", new String[]{"id"}, new Object[]{paramConnection.getModelReg()});
        }
        REPLICATION = getReplication();
       if (REPLICATION) {
            if (currentAgence != null ? currentAgence.getId() > 0 : false) {
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
        String host = UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_HOST);
        String hostR = UtilsProject.properties.getProperty(Constantes.KEY_REMOTE_HOST);
        String db = UtilsProject.properties.getProperty(Constantes.KEY_LOCAL_DB_NAME);
        String dbR = UtilsProject.properties.getProperty(Constantes.KEY_REMOTE_DB_NAME);
        return !host.equals(hostR) || !db.equals(dbR);
    }

    public static ParamConnection readProperty() {
        FileInputStream fis = null;
        try {
            //ouvre le fichier initialisé
            fis = new FileInputStream(new File("conf/servConfig.ltz"));
            ParamConnection param = new ParamConnection();
            param.readFile(fis);
            return param;
        } catch (FileNotFoundException ex) {
            LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
            Logger.getLogger(UtilsProject.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                LogFiles.addLogInFile("", Severity.ERROR, ConsUtil.SOURCE_LOG_FILE_EXCEPTION, ex);
                Logger.getLogger(UtilsProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    
    private static String getOpenDiv() {
        StringBuilder sb = new StringBuilder("<div style=")
                .append("font-size:7pt").append(">");
        return sb.toString();
    }

    private static String getOpenDivfoot() {
        StringBuilder sb = new StringBuilder("<div style=")
                .append("font-size:5pt; white-space: initial;white-space: nowrap;").append(">");
        return sb.toString();
    }

    private static String getSpan(String text) {
        StringBuilder sb = new StringBuilder("<span style=")
                .append("font-size:6pt; witdh:110px;display:inline-block; float:left").append(">").append(text).append("</span>");
        return sb.toString();
    }

    public static ImageView buildImageProduit(String path) {
        ImageView img = new ImageView(new Image(LocalLoader.class.getResource("icones/" + path).toExternalForm()));
        img.setFitHeight(80);
        img.setFitWidth(70);
        return img;
    }

    public static String fillColumn(String type, ResultSet rs, int colIndex) {
        String re = null;
        try {
            if (type != null) {
                switch (type) {
                    case "bigint":
                    case "integer":
                    case "bigserial":
                        re = ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getLong(colIndex)) : "");
                        break;
                    case "double precision":
                        re = ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getDouble(colIndex)) : "");
                        break;
                    case "character varying":
                        re = ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getString(colIndex)) : "");
                        break;
                    case "timestamp":
                        re = ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getString(colIndex)) : "");
                        break;
                    default:
                        re = ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getObject(colIndex)) : "");
                        break;

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
        }
        return re;
    }

    public static LQuery buildQueryRemote(String table, List<EntityColumn> colonnes, String[] colFilter, Long idListen) {
        return buildQueryRemote(table, colonnes, colFilter, true, idListen);
    }

    public static LQuery buildQueryRemote(String table, List<EntityColumn> colonnes, String[] colFilter, boolean withDefaultFilter, Long idListen) {
        String param = "";
        String query = "SELECT l.id, ";
        int i = 0;
        boolean hasAgence = false;
        boolean hasSociete = false;
        for (EntityColumn c : colonnes) {
            query += ((i == 0) ? "y." : ", y.") + c.getColumnName();
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
                    query += " FROM " + table + " y INNER JOIN yvs_users u ON u.id=y.users INNER JOIN yvs_agences a ON a.id=u.agence INNER JOIN yvs_societes s ON s.id=a.societe LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE s.id=?";
                    param += "societe";
                } else {
                    query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "')";
                }
                break;
            case Constantes.TABLE_ARTICLE_CODE:
                if (withDefaultFilter) {
                    query += " FROM " + table + " y INNER JOIN yvs_base_famille_article f ON f.id=y.famille LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE f.societe=?";
                    if (!param.contains("societe")) {
                        param += "societe";
                    }
                } else {
                    query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "')";
                }
                break;
            case Constantes.TABLE_ARTICLE_DEPOT_CODE:
                if (withDefaultFilter) {
                    query += " FROM " + table + " y INNER JOIN yvs_base_depots d ON d.id=y.depot LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE d.agence=?";
                    if (!param.contains("agence")) {
                        param += "agence";
                    }
                } else {
                    query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "')";
                }
                break;
            case Constantes.TABLE_ARTICLE_POINT_CODE:
                if (withDefaultFilter) {
                    query += " FROM " + table + " y INNER JOIN yvs_base_point_vente p ON p.id=y.point LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE p.agence=?";
                    if (!param.contains("agence")) {
                        param += "agence";
                    }
                } else {
                    query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "')";
                }
                break;
            case Constantes.TABLE_CONDITIONNEMENT_POINT_CODE:
                if (withDefaultFilter) {
                    query += " FROM " + table + " y INNER JOIN yvs_base_article_point ap ON ap.id=y.article INNER JOIN yvs_base_point_vente p ON p.id=ap.point LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE p.agence=?";
                    if (!param.contains("agence")) {
                        param += "agence";
                    }
                } else {
                    query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "')";
                }
                break;
            case Constantes.TABLE_ELEMENT_REFERENCE_CODE:
            case Constantes.TABLE_DICTIONNAIRES_CODE:
                query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT')";
                break;
            case Constantes.TABLE_CRENEAU_HORAIRE_USER_CODE:
                if (withDefaultFilter) {
                    query += " FROM " + table + " y INNER JOIN yvs_users_agence ua ON ua.id=y.author INNER JOIN yvs_agences a ON a.id=ua.agence LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE y.creneau_point IS NOT NULL AND a.id=? ";
                    if (!param.contains("agence")) {
                        param += "agence";
                    }
                } else {
                    query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "')";
                }
                break;
            default:
                if (withDefaultFilter) {
                    if (hasSociete) {
                        query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE y.societe=? ";
                        if (!param.contains("societe")) {
                            param += "societe";
                        }
                    } else if (hasAgence) {
                        query += " FROM " + table + " y INNER JOIN yvs_agences a ON a.id=y.agence LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE a.societe=? ";
                        if (!param.contains("societe")) {
                            param += "societe";
                        }
                    } else {
                        query += " FROM " + table + " y INNER JOIN yvs_users_agence ua ON ua.id=y.author INNER JOIN yvs_agences a ON a.id=ua.agence LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "' AND l.action_name='INSERT') WHERE a.societe=? ";
                        if (!param.contains("societe")) {
                            param += "societe";
                        }
                    }
                } else {
                    query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table='" + table + "')";
                }
        }
        if (colFilter != null ? colFilter.length > 0 : false) {
            if (query.contains("WHERE")) {
                if (Constantes.asLong(idListen)) {
                    query += "  AND l.id=" + idListen;
                }
                for (String s : colFilter) {
                    query += " AND " + s + " =? ";
                }
            } else {
                if (Constantes.asLong(idListen)) {
                    query += " WHERE l.id=" + idListen;
                } else {
                    query += " WHERE y.id=y.id ";
                }
                for (String s : colFilter) {
                    query += " AND " + s + " =? ";
                }
            }
            param += "-" + colFilter;
        }
        return new LQuery(query, param);
    }

    public static String buildQueryCount(String table, List<EntityColumn> colonnes) {
        String query = "SELECT COUNT(*)";
        int i = 0;
        boolean hasAgence = false;
        boolean hasSociete = false;
        for (EntityColumn c : colonnes) {
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
        String query = "SELECT MAX(ds.id_distant), ";
        int i = 0;
        for (EntityColumn c : colonnes) {
            query += ((i == 0) ? "y." : ", y.") + c.getColumnName();
            i++;
        }
        query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (y.id=l.id_source AND l.name_table='" + table + "') "
                + "LEFT JOIN yvs_synchro_data_synchro ds ON ds.id_listen=l.id ";

        if (colFilter != null) {
            query += " WHERE y." + colFilter + " =? GROUP BY y.id ORDER BY y.id";
        } else {
            query += "GROUP BY y.id ORDER BY y.id";
        }
        return query;
    }

    public static String buildQueryToSynchro(String table, List<EntityColumn> colonnes, String colFilter) {
        String query = "SELECT ds.id id_ds, ds.id_distant,l.id id_listen, l.action_name, ";
        int i = 0;
        for (EntityColumn c : colonnes) {
            query += ((i == 0) ? "y." : ", y.") + c.getColumnName();
            i++;
        }
        query += " FROM " + table + " y LEFT JOIN yvs_synchro_listen_table l ON (l.id_source=y.id AND l.name_table=?) "
                + " LEFT JOIN  yvs_synchro_data_synchro ds ON ds.id_listen=l.id LIMIT 2000";
//        if (colFilter != null) {
//            query += " WHERE " + colFilter + " =?";
//        }
        return query;
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
            if (type.toLowerCase() != null) {
                re = new EntityColumn(c);
                re.setTypeDatabase(type);
                switch (type) {
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
            Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
        }
        return re;
    }

    public static PreparedStatement applyParam(PreparedStatement st, Object[] params, int indice) {
        for (Object o : params) {
            try {
                switch (o.getClass().getSimpleName()) {
                    case "Long":
                        st.setLong(indice, Long.valueOf(o.toString()));
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
                Logger.getLogger(UtilsProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return st;
    }
}
