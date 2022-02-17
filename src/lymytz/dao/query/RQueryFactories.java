/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.query;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.Severity;
import lymytz.dao.LocalDao;
import lymytz.dao.Options;
import lymytz.dao.RemoteDao;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.dao.entity.service.LymytzData;
import lymytz.service.application.bean.Factures;
import lymytz.service.application.exception.CloseConException;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LQuery;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;

/**
 *
 * @author Admin Gestionnaire des requêtes distante
 * @param <T>
 */
public class RQueryFactories<T extends Serializable> {

    public RQueryFactories() {
    }

    public static boolean pingServer() {
        String query = "SELECT 1";
        if (RemoteDao.getInstance() != null) {
            try (Statement st = RemoteDao.getInstance().getConnection().createStatement();
                    ResultSet rs = st.executeQuery(query)) {
                if (!rs.isClosed()) {
                    while (rs.next()) {
                        Object re = rs.getObject(1);
                    }
                }
                return true;
            } catch (SQLException | CloseConException ex) {
                RemoteDao.setInstance(null);
                LogFiles.addLogInFile("", new Exception(ex));
                return false;
            }
        }
        return false;
    }

    public static Long getIdServer() {
        String query = "SELECT id FROM yvs_synchro_serveurs WHERE adresse_ip= ? LIMIT 1";
        if (RemoteDao.getInstance() != null) {
            Long re = null;
            try (PreparedStatement st = RemoteDao.getInstance().getConnection().prepareStatement(query)) {
                if (!st.isClosed()) {
                    st.setString(1, LymytzService.getMacAdress());
                    re = null;
                    try (ResultSet rs = (!st.isClosed()) ? st.executeQuery() : null) {
                        if (rs != null ? !rs.isClosed() : false) {
                            while (rs.next()) {
                                re = rs.getLong(1);
                            }
                        }
                    } catch (SQLException ex) {
                        RemoteDao.setInstance(null);
                        LogFiles.addLogInFile("", new Exception(ex));
                        Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException | CloseConException ex) {
                RemoteDao.setInstance(null);
                LogFiles.addLogInFile("", new Exception(ex));
                Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
            return re;
        }
        return -1L;
    }

    public static Long getIdListenTable(Long idSource, String table) {
        String query = "SELECT id FROM yvs_synchro_listen_table WHERE id_source= ? AND name_table=? LIMIT 1";
        Long re = -1L;
        if (RemoteDao.getInstance() != null) {
            try (PreparedStatement st = RemoteDao.getInstance().getConnection().prepareStatement(query)) {
                if (!st.isClosed()) {
                    st.setLong(1, idSource);
                    st.setString(2, table);
                    re = null;
                    try (ResultSet rs = !st.isClosed() ? st.executeQuery() : null) {
                        if (rs != null ? !rs.isClosed() : false) {
                            while (rs.next()) {
                                re = rs.getLong(1);
                            }
                        }
                    } catch (SQLException ex) {
                        RemoteDao.setInstance(null);
                        Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException | CloseConException ex1) {
                Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex1);
                LogFiles.addLogInFile("", new Exception(ex1));
                return -1L;
            }
            return re;
        }
        return -1L;
    }

    public Long countElement(String table, List<EntityColumn> colonnes) {
        if (UtilsProject.RcurrentSociete != null) {
            String query = UtilsProject.buildQueryCount(table, colonnes);
            if (RemoteDao.getInstance() != null) {
                try (PreparedStatement st = RemoteDao.getInstance().getConnection().prepareCall(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                    if (!table.equals(Constantes.TABLE_ELEMENT_REFERENCE_CODE) && !table.equals(Constantes.TABLE_DICTIONNAIRES_CODE)) {
                        switch (table) {
                            case Constantes.TABLE_ARTICLE_DEPOT_CODE:
                                st.setLong(1, UtilsProject.RcurrentAgence.getId());
                                break;
                            case Constantes.TABLE_CRENEAU_HORAIRE_USER_CODE:
                                st.setLong(1, UtilsProject.RcurrentAgence.getId());
                                break;

                            case Constantes.TABLE_ARTICLE_POINT_CODE:
                                st.setLong(1, UtilsProject.RcurrentAgence.getId());
                                break;
                            case Constantes.TABLE_CONDITIONNEMENT_POINT_CODE:
                                st.setLong(1, UtilsProject.RcurrentAgence.getId());
                                break;
                            default:
                                st.setLong(1, UtilsProject.RcurrentSociete.getId());
                        }
                    }
                    try (ResultSet rs = !st.isClosed() ? st.executeQuery() : null) {
                        if (rs != null ? !rs.isClosed() : false) {
                            rs.first();
                            Long re = rs.getLong(1);
                            st.close();
                            return re;
                        }
                    } catch (SQLException ex) {
                        RemoteDao.setInstance(null);
                        LogFiles.addLogInFile("", new Exception(ex));
                        Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SQLException | CloseConException ex) {
                    RemoteDao.setInstance(null);
                    LogFiles.addLogInFile("", new Exception(ex));
                    Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 0L;
    }

    public PreparedStatement buildeGenericRemoteQuery(String table, List<EntityColumn> colonnes, String colFilter[], Object valueFilter) {
        return buildeGenericRemoteQuery(table, colonnes, colFilter, (valueFilter != null ? new Object[]{valueFilter} : null));
    }

    public PreparedStatement buildeGenericRemoteQuery(String table, List<EntityColumn> colonnes, String colFilter[], Object[] param) {
        boolean withDefaultFilter = ((colFilter != null ? (colFilter.length <= 0) : true));
        LQuery query = UtilsProject.buildQueryRemote(table, colonnes, colFilter, withDefaultFilter, null);
        PreparedStatement st = null;
        if (RemoteDao.getInstance() != null) {
            try {
                st = RemoteDao.getInstance().getConnection().prepareCall(query.getQuery(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                int indice = 1;
                if (withDefaultFilter) {
                    if (query.getParam().contains("societe")) {
                        st.setLong(1, UtilsProject.RcurrentSociete.getId());
                    } else if (query.getParam().contains("agence")) {
                        st.setLong(1, UtilsProject.RcurrentAgence.getId());
                    }
                    indice++;
                }
                if (param != null ? param.length > 0 : false) {
                    st = UtilsProject.applyParam(st, param, indice);
                }
            } catch (SQLException | CloseConException ex) {
                RemoteDao.setInstance(null);
                LogFiles.addLogInFile("", new Exception(ex));
                Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return st;
    }

    public List<LymytzData> loadDataBySQLQuery(String query, Options[] params, List<EntityColumn> colonnes) {
        List<LymytzData> result = new ArrayList<>();
        if (RemoteDao.getInstance() != null) {
            try (PreparedStatement ps = RemoteDao.getInstance().getConnection().prepareCall(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                int i = 1;
                for (Options o : params) {
                    ps.setObject(o.getPosition(), o.getValeur());
                }
                try (ResultSet rs = !ps.isClosed() ? ps.executeQuery() : null) {
                    LymytzData line;
                    if (rs != null ? !rs.isClosed() : false) {
                        while (rs.next()) {
                            line = new LymytzData(new EntityColumn[colonnes.size()]);
                            int idx = 0;
                            line.setIdListen(rs.getLong(idx + 1));  // récupère d'abord id_listen
                            idx++;
                            for (EntityColumn c : colonnes) {
                                line.getValue()[idx - 1] = UtilsProject.fillColumn(rs.getMetaData().getColumnTypeName(idx + 1), c, rs, (idx + 1));
                                idx++;
                            }
                            result.add(line);
                        }
                    }
                } catch (SQLException ex) {
                    //Logging de l'erreur
                    LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                    RemoteDao.setInstance(null);
                    Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                }
                return result;
            } catch (SQLException | CloseConException ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                RemoteDao.setInstance(null);
                Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public List<Object[]> loadBySQLQuery(String query, Options[] params) {
        List<Object[]> result = new ArrayList<>();
        if (RemoteDao.getInstance() != null) {
            try (PreparedStatement ps = RemoteDao.getInstance().getConnection().prepareCall(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                int i = 1;
                for (Options o : params) {
                    if (!o.getValeur().getClass().getName().equals("java.util.Date")) {
                        ps.setObject(o.getPosition(), o.getValeur());
                    } else {
                        ps.setDate(o.getPosition(), new java.sql.Date(((Date) o.getValeur()).getTime()));
                    }
                }
                try (ResultSet rs = !ps.isClosed() ? ps.executeQuery() : null) {
                    if (rs != null ? !rs.isClosed() : false) {
                        int columns = rs.getMetaData().getColumnCount();
                        Object[] line;
                        while (rs.next()) {
                            line = new Object[columns];
                            for (int idx = 0; idx < columns; idx++) {
                                line[idx] = UtilsProject.fillColumn(rs.getMetaData().getColumnTypeName(idx + 1), rs, (idx + 1));
                            }
                            result.add(line);
                        }
                    }
                } catch (Exception e) {
                    LogFiles.addLogInFile("Echec de lecture du résultat de la requête: " + query, e);
                    RemoteDao.setInstance(null);
                    Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, e);
                }
            } catch (SQLException | CloseConException ex) {
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                RemoteDao.setInstance(null);
                Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return result;
    }

    public Object loadOneBySQLQuery(String query, Options[] params) {
        Object result = null;
        if (RemoteDao.getInstance() != null) {
            try (PreparedStatement ps = RemoteDao.getInstance().getConnection().prepareCall(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                int i = 1;
                for (Options o : params) {
                    ps.setObject(o.getPosition(), o.getValeur());
                }
                if (!ps.isClosed()) {
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.isClosed()) {
                            rs.first();
                            result = rs.getObject(1);
                        }
                    } catch (Exception e) {
                        LogFiles.addLogInFile("Echec de lecture du résultat de la requête: " + query, e);
                        RemoteDao.setInstance(null);
                        Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
                return result;
            } catch (SQLException | CloseConException ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                RemoteDao.setInstance(null);
                Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public boolean insertIntoDataSynchro(Long idListen, Long idDistant, Long server) {
        StringBuilder sb1 = new StringBuilder("INSERT INTO yvs_synchro_data_synchro(id_listen, date_save, id_distant, serveur)");
        sb1.append("VALUES (?,?,?,?)");
        try {
            if (UtilsProject.ID_SERVEUR > 0) {
                if (RemoteDao.getInstance() != null) {
                    try (PreparedStatement ps = RemoteDao.getInstance().getConnection().prepareCall(sb1.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                        RemoteDao.getInstance().getConnection().setAutoCommit(false);
                        ps.setLong(1, idListen);
                        ps.setDate(2, new java.sql.Date(new Date().getTime()));
                        ps.setLong(3, idDistant);
                        ps.setLong(4, server);
                        if (!ps.isClosed()) {
                            ps.executeUpdate();
                        }
                        RemoteDao.getInstance().getConnection().commit();
                    } catch (SQLException | CloseConException ex) {
                        LogFiles.addLogInFile("Echec de l'execution de la requete ", ex);
                        Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                        if (!RemoteDao.getInstance().getConnection().isClosed()) {
                            RemoteDao.getInstance().getConnection().rollback();
                        }
                    }
                }
            } else {
                LogFiles.addLogInFile("Votre serveur n'est pas enregistré sur l'ordinateur distant", Severity.ERROR);
            }
        } catch (SQLException | CloseConException ex) {
            //Logging de l'erreur
            LogFiles.addLogInFile("Echec de l'execution de la requete " + sb1.toString(), ex);
            RemoteDao.setInstance(null);
            Logger.getLogger(LocalDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean insertIntoDataSynchroByIdSource(Long id_distant, Long id_local, Long server, String table) {
        String query = "INSERT INTO yvs_synchro_data_synchro(id_listen, serveur, date_save, id_distant) "
                + "SELECT l.id, ?, current_timestamp, ? FROM yvs_synchro_listen_table l "
                + "LEFT JOIN yvs_synchro_data_synchro s ON (s.id_listen = l.id AND s.serveur = ?) "
                + "WHERE s.id IS NULL AND l.id_source = ? AND l.name_table = ? ";
        try {
            if (UtilsProject.ID_SERVEUR > 0) {
                if (RemoteDao.getInstance() != null) {
                    try (PreparedStatement ps = RemoteDao.getInstance().getConnection().prepareCall(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                        RemoteDao.getInstance().getConnection().setAutoCommit(false);
                        ps.setLong(1, server);
                        ps.setLong(2, id_local);
                        ps.setLong(3, server);
                        ps.setLong(4, id_distant);
                        ps.setString(5, table);
                        if (!ps.isClosed()) {
                            ps.executeUpdate();
                        }
                        RemoteDao.getInstance().getConnection().commit();
                    } catch (SQLException | CloseConException ex) {
                        LogFiles.addLogInFile("Echec de l'execution de la requete ", ex);
                        Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                        if (!RemoteDao.getInstance().getConnection().isClosed()) {
                            RemoteDao.getInstance().getConnection().rollback();
                        }
                    }
                }
            } else {
                LogFiles.addLogInFile("Votre serveur n'est pas enregistré sur l'ordinateur distant", Severity.ERROR);
            }
        } catch (SQLException | CloseConException ex) {
            //Logging de l'erreur
            LogFiles.addLogInFile("Echec de l'execution de la requete " + query, ex);
            RemoteDao.setInstance(null);
            Logger.getLogger(LocalDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public static Long insertInfoServeur() {
        StringBuilder sb1 = new StringBuilder("INSERT INTO yvs_synchro_serveurs(nom_serveur, adresse_ip, actif, online) ");
        sb1.append(" VALUES (?,?,?,?) ");
        try {
            if (RemoteDao.getInstance() != null) {
                try (PreparedStatement ps = RemoteDao.getInstance().getConnection().prepareCall(sb1.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                    RemoteDao.getInstance().getConnection().setAutoCommit(false);
                    ps.setString(1, LymytzService.getHostName());
                    ps.setString(2, LymytzService.getMacAdress());
                    ps.setBoolean(3, true);
                    ps.setBoolean(4, true);
                    if (!ps.isClosed()) {
                        ps.executeUpdate();
                    }
                    Long idListen;
                    try (Statement st = RemoteDao.getInstance().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            ResultSet rs = st.executeQuery("SELECT currval('yvs_synchro_serveurs_id_seq')")) {
                        rs.first();
                        idListen = rs.getLong(1);
                        RemoteDao.getInstance().getConnection().commit();
                    }
                    ps.close();
                    return idListen;
                } catch (SQLException | CloseConException ex) {
                    LogFiles.addLogInFile("Echec de l'execution de la requete ", ex);
                    Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                    if (!RemoteDao.getInstance().getConnection().isClosed()) {
                        RemoteDao.getInstance().getConnection().rollback();
                    }
                }
            }
        } catch (SQLException | CloseConException ex) {
            //Logging de l'erreur
            LogFiles.addLogInFile("Echec de l'execution de la requete " + sb1.toString(), ex);
            RemoteDao.setInstance(null);
            Logger.getLogger(LocalDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1L;
    }

    public void loadSqlDataToXmlFile() {

    }

    public Factures actualiseStatut(Factures facture) {
        String query = "SELECT * FROM equilibre_vente(?)";
        try (PreparedStatement pst = RemoteDao.getInstance().getConnection().prepareCall(query)) {
            pst.setLong(1, facture.getIdDistant());
            try (ResultSet rs = pst.executeQuery()) {
//                rs.first();
                while (rs.next()) {
                    String sL = rs.getString(1);
                    String sR = rs.getString(2);
                    facture.setStatutLivraison(sL);
                    facture.setStatutReglement(sR);
                }
            } catch (SQLException ex) {
                Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException e) {
        } catch (CloseConException ex) {
            Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
        }
        return facture;
    }
}
