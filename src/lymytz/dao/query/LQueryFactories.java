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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Table;
import lymytz.dao.LocalDao;
import lymytz.dao.LocalSqlDao;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsEntity;
import lymytz.dao.entity.YvsSynchroDataSynchro;
import lymytz.dao.entity.YvsSynchroListenTable;
import lymytz.dao.entity.YvsSynchroServeurs;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;

/**
 *
 * @author Admin gestionnaire des requêtes locale
 * @param <T>
 */
public class LQueryFactories<T extends Serializable> {

    public LQueryFactories() {
    }

    public static boolean pingServer() {
        String query = "SELECT 1";
        if (LocalDao.getInstance() != null) {
            try {
                try {
                    EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                    Query qr = em.createNativeQuery(query);
                    qr.getSingleResult();
                    em.close();
                    return true;
                } catch (NoResultException ex) {
                    return false;
                }
            } catch (Exception ex) {
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                LocalDao.setInstance(null);
                return false;
            }
        }
        return false;
    }

    public T save1(T entity) {
        return (T) save1(entity, true);
    }

    public T save1(T entity, boolean synchronise) {
        if (LocalDao.getInstance() != null) {
            try {
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                em.setFlushMode(FlushModeType.COMMIT);
                em.getTransaction().begin();
                em.persist(entity);
                em.getTransaction().commit();
                em.close();
                if (synchronise) {
                    afterCRUD(entity, Constantes.INSERT_ACTION);
                }
                return entity;
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete save " + (entity.toString()), ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public T update(T entity) {
        return update(entity, true);
    }

    public T update(T entity, boolean synchronise) {
        if (LocalDao.getInstance() != null) {
            try {
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                em.setFlushMode(FlushModeType.COMMIT);
                em.getTransaction().begin();
                em.merge(entity);
                em.getTransaction().commit();
                em.close();
                if (synchronise) {
                    afterCRUD(entity, Constantes.UPDATE_ACTION);
                }
                return (entity);
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete save " + (entity.toString()), ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public Object findOneObjectByNQ(String query, String[] param, Object[] paramValue) {
        if (LocalDao.getInstance() != null) {
            try {
                try {
                    Object result = null;
                    EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                    Query qr = em.createNamedQuery(query);
                    int i = 0;
                    for (String st : param) {
                        qr.setParameter(st, paramValue[i]);
                        i++;
                    }
                    qr.setMaxResults(1);
                    result = (Object) qr.getSingleResult();
                    em.close();
                    return result;
                } catch (NoResultException ex) {
                    return null;
                }
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public T findOneByNQ(String query, String[] param, Object[] paramValue) {
        if (LocalDao.getInstance() != null) {
            try {
                try {
                    T result = null;
                    EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                    em.clear();
                    Query qr = em.createNamedQuery(query);
                    int i = 0;
                    for (String st : param) {
                        qr.setParameter(st, paramValue[i]);
                        i++;
                    }
                    qr.setMaxResults(1);
                    result = (T) qr.getSingleResult();
                    if (result != null) {
                        em.refresh(result);
                    }
                    em.close();
                    return result;
                } catch (NoResultException ex) {
                    return null;
                }
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public Object findOneObjectBySQLQ(String query, Options[] paramValue) {
        if (LocalDao.getInstance() != null) {
            try {
                try {
                    Object result = null;
                    EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                    Query qr = em.createNativeQuery(query);
                    for (Options o : paramValue) {
                        qr.setParameter(o.getPosition(), o.getValeur());

                    }
                    qr.setMaxResults(1);
                    result = (Object) qr.getSingleResult();
                    em.close();
                    return result;
                } catch (NoResultException ex) {
                    return null;
                }
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public List<T> loadByNamedQuery(String query, String[] param, Object[] paramValue) {
        List<T> result = new ArrayList<>();
        if (LocalDao.getInstance() != null) {
            try {
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                Query qr = em.createNamedQuery(query);
                int i = 0;
                for (String st : param) {
                    qr.setParameter(st, paramValue[i]);
                    i++;
                }
                result = qr.getResultList();
                em.close();
                return result;
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public List<T> loadByNamedQuery(String query, String[] param, Object[] paramValue, int offset, int max) {
        List<T> result = new ArrayList<>();
        if (LocalDao.getInstance() != null) {
            try {
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                Query qr = em.createNamedQuery(query);
                int i = 0;
                for (String st : param) {
                    qr.setParameter(st, paramValue[i]);
                    i++;
                }
                qr.setFirstResult(offset);
                qr.setMaxResults(max);
                result = qr.getResultList();
                em.close();
                return result;
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public List<Object[]> loadBySQLQuery(String query, Options[] params) {
        List<Object[]> result = new ArrayList<>();
        if (LocalDao.getInstance() != null) {
            try {
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                Query qr = em.createNativeQuery(query);
                for (Options o : params) {
                    qr.setParameter(o.getPosition(), o.getValeur());
                }
                result = qr.getResultList();
                em.close();
                return result;
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public List<Object[]> loadBySQLQuery(String query, Options[] params, int offset, int limit) {
        List<Object[]> result = new ArrayList<>();
        if (LocalDao.getInstance() != null) {
            try {
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                Query qr = em.createNativeQuery(query);
                for (Options o : params) {
                    qr.setParameter(o.getPosition(), o.getValeur());
                }
                qr.setFirstResult(offset);
                qr.setMaxResults(limit);
                result = qr.getResultList();
                em.close();
                return result;
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public double arrondi(long societe, double valeur) {
        double re = 0;
        if (LocalDao.getInstance() != null) {
            try {
                try {
                    EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                    Query qr = em.createNativeQuery("select public.arrondi(?,?)");
                    qr.setParameter(1, societe).setParameter(2, valeur);
                    Double dr = (Double) qr.getSingleResult();

                    re = (dr != null) ? dr : 0.0;
                } catch (NoResultException ex) {
                    return 0;
                }
            } catch (Exception ex) {
                try {
                    LogFiles.addLogInFile("Echec de l'execution de la requete: ", ex);
                    LocalDao.setInstance(null);
                    Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex1) {
                    Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return re;
    }

    public PreparedStatement buildeGenericQuery(String table, List<EntityColumn> colonnes, String colFilter, Object valueFilter) {
        String query = UtilsProject.buildQueryLocal(table, colonnes, colFilter);
        PreparedStatement st = null;
        if (LocalDao.getInstance() != null) {
            try {
                st = LocalSqlDao.getInstance().getConnection().prepareCall(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                if (colFilter != null) {
                    st.setObject(1, valueFilter);
                }
            } catch (SQLException ex) {
                LogFiles.addLogInFile("Echec de l'execution de la requete: ", ex);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return st;
    }

    //Cette méthode est utilisé pour la synchro à partir de listen_table
    public PreparedStatement buildQueryToSynchronise(String table, List<EntityColumn> colonnes, String colFilter, Object valueFilter) {
        String query = UtilsProject.buildQueryToSynchro(table, colonnes, colFilter);
        PreparedStatement st = null;
        if (LocalDao.getInstance() != null) {
            try {
                st = LocalSqlDao.getInstance().getConnection().prepareCall(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                if (colFilter != null) {
                    st.setObject(1, valueFilter);
                }
            } catch (SQLException ex) {
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return st;
    }

//    public boolean asString(String valeur) {
//        if (valeur != null ? valeur.trim().length() > 0 : false) {
//            return true;
//        }
//        return false;
//    }
    public Long insertFromSqlQuery(String table, String query, Options[] params) {
        if (query != null) {
            if (LocalDao.getInstance() != null) {
                try {
                    EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                    em.getTransaction().begin();
                    Query qr = em.createNativeQuery(query);
                    for (Options o : params) {
                        qr.setParameter(o.getPosition(), o.getValeur());
                    }
                    qr.executeUpdate();
                    //récupère la séquence
                    qr = em.createNativeQuery("SELECT currval('" + table + "_id_seq')");
                    Long id = (Long) qr.getSingleResult();
                    em.getTransaction().commit();
                    em.close();
                    return id;
                } catch (Exception ex) {
                    //Logging de l'erreur
                    LogFiles.addLogInFile("Echec de l'execution de la requete " + query, ex);
                    LocalDao.setInstance(null);
                    Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 0L;
    }

    public Long executeSqlQuery(String query, Options[] params) {
        if (query != null) {
            if (LocalDao.getInstance() != null) {
                try {
                    EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                    em.getTransaction().begin();
                    Query qr = em.createNativeQuery(query);
                    for (Options o : params) {
                        qr.setParameter(o.getPosition(), o.getValeur());
                    }
                    qr.executeUpdate();
                    em.getTransaction().commit();
                    em.close();
//                    return (Long) params[params.length - 1].getValeur();
                    return 1L;
                } catch (Exception ex) {
                    //Logging de l'erreur
                    LogFiles.addLogInFile("Echec de l'execution de la requete " + query, ex);
                    LocalDao.setInstance(null);
                    Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 0L;
    }

    public void cleanDocWithoutContent(Long header) {
        if (LocalDao.getInstance() != null) {
            if (Constantes.asLong(header)) {
                String query = "DELETE FROM yvs_com_doc_ventes WHERE id IN (SELECT d.id FROM yvs_com_doc_ventes d left join yvs_com_contenu_doc_vente c on d.id=c.doc_vente "
                        + "WHERE c.id is NULL AND d.entete_doc=?)";
                String query2 = "DELETE FROM yvs_synchro_listen_table WHERE id_source IN (SELECT d.id FROM yvs_com_doc_ventes d left join yvs_com_contenu_doc_vente c on d.id=c.doc_vente "
                        + "WHERE c.id is NULL AND d.entete_doc=?) AND name_table='yvs_com_doc_ventes'";
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                Query qr = em.createNativeQuery(query2);
                qr.setParameter(1, header);
                qr.executeUpdate();
                Query qr2 = em.createNativeQuery(query);
                qr2.setParameter(1, header);
                qr2.executeUpdate();
                em.getTransaction().commit();
                em.close();

            }
        }
    }

    /*permet d'annuler le marqueur de synchronisation d'une information sur la base local afin de pouvoir la synchroniser à nouveau*/
    public void majInfosDataSynchro(Long idDistant, String table, Long idLocal) {
        String query = "DELETE FROM yvs_synchro_data_synchro WHERE id IN "
                + "(SELECT ds.id FROM yvs_synchro_data_synchro ds INNER JOIN yvs_synchro_listen_table l ON l.id=ds.id_listen "
                + "WHERE l.name_table=? AND ds.id_distant=?)";
        String query1 = "UPDATE yvs_synchro_listen_table SET message=null, to_listen=true WHERE id_source=? AND name_table=? ";
        if (LocalDao.getInstance() != null) {
            try {
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                Query qr = em.createNativeQuery(query);
                qr.setParameter(1, table);
                qr.setParameter(2, idDistant);
                qr.executeUpdate();
                Query qr1 = em.createNativeQuery(query1);
                qr1.setParameter(1, idLocal);
                qr1.setParameter(2, table);
                qr1.executeUpdate();
                em.close();
                em.getTransaction().commit();
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete: " + query, ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Long insertListenData(Long localId, String table, Long author, Long idDistant, boolean listen) {
        //Vérifie d'abord si l'information se trouve déjà dans la table yvs_synchro_data_synchro
        if (LocalDao.getInstance() != null) {
            Object[] keys = findLocalKey(table, idDistant);
            if ((keys != null ? keys.length > 0 : false) ? Objects.equals((Long) keys[0], localId) : false) {
                String rq = "UPDATE yvs_synchro_listen_table SET id_source=?,date_save=? WHERE id= ?";
                try {
                    EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                    em.getTransaction().begin();
                    Query qr = em.createNativeQuery(rq);
                    qr.setParameter(1, (Long) keys[0]);
                    qr.setParameter(2, new Date());
                    qr.setParameter(3, (Long) keys[1]);
                    qr.executeUpdate();
                    em.getTransaction().commit();
                    em.close();
                    return 0L;
                } catch (Exception ex) {
                    //Logging de l'erreur
                    LogFiles.addLogInFile("Echec de l'execution de la requete " + rq, ex);
                    LocalDao.setInstance(null);
                    Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                }
//            }
            } else {
                StringBuilder sb = new StringBuilder("INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name, ordre, author )");
                sb.append("VALUES (?,?,?,?,?,?,?::bigint)");
                StringBuilder sb1 = new StringBuilder("INSERT INTO yvs_synchro_data_synchro(id_listen, date_save, id_distant)");
                sb1.append("VALUES (?,?,?)");
                if (localId != null ? localId > 0 : false) {
                    try {
                        EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                        em.getTransaction().begin();
                        Query qr = em.createNativeQuery(sb.toString());
                        qr.setParameter(1, table);
                        qr.setParameter(2, localId);
                        qr.setParameter(3, new Date());
                        qr.setParameter(4, listen);
                        qr.setParameter(5, "INSERT");
                        qr.setParameter(6, 0);
                        qr.setParameter(7, author);
                        qr.executeUpdate();
                        //récupère la séquence
                        Query qr1 = em.createNativeQuery("SELECT currval('yvs_synchro_listen_table_id_seq')");
                        Long idListen = (Long) qr1.getSingleResult();
                        if (Constantes.asLong(idDistant)) {
                            Query qr2 = em.createNativeQuery(sb1.toString());
                            qr2.setParameter(1, idListen);
                            qr2.setParameter(2, new Date());
                            qr2.setParameter(3, idDistant);
                            qr2.executeUpdate();
                        }
                        em.getTransaction().commit();
                        em.close();
                        return idListen;
                    } catch (Exception ex) {
                        //Logging de l'erreur
                        LogFiles.addLogInFile("Echec de l'execution de la requete " + sb.toString(), ex);
                        LocalDao.setInstance(null);
                        Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }

    public Long insertNewListenData(Long localId, String table, Long author, Long idDistant) {
        StringBuilder sb = new StringBuilder("INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name, ordre, author )");
        sb.append("VALUES (?,?,?,?,?,?,?::bigint)");
        if (localId != null ? localId > 0 : false) {
            try {
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                Query qr = em.createNativeQuery(sb.toString());
                qr.setParameter(1, table);
                qr.setParameter(2, localId);
                qr.setParameter(3, new Date());
                qr.setParameter(4, false);
                qr.setParameter(5, Constantes.INSERT_ACTION);
                qr.setParameter(6, 0);
                qr.setParameter(7, author);
                qr.executeUpdate();
                //récupère la séquence
                Query qr1 = em.createNativeQuery("SELECT currval('yvs_synchro_listen_table_id_seq')");
                Long idListen = (Long) qr1.getSingleResult();
                em.getTransaction().commit();
                em.close();
                return idListen;
            } catch (Exception ex) {
                //Logging de l'erreur
                LogFiles.addLogInFile("Echec de l'execution de la requete " + sb.toString(), ex);
                LocalDao.setInstance(null);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     *
     * @param idListen: id da la table yvs_synchro_listen_table qui doit être
     * mis à jour
     * @param remoteId: représentant sur le seurveur de l'information local;
     * @param message: message de retour du serveur à inserer dans la table
     * yvs_synchro_listen_table
     * @param insert: determine si on doit inserer dans yvs_synchro_data_synchro
     * juste en cas de succès de l'opération
     */
    public void insertDataSynchro(Long idListen, Long remoteId, String message, boolean insert) {
        //vérifie avant que l'information local ne se trouve pas déjà dans synchro_data
        String sq = "SELECT id FROM yvs_synchro_data_synchro WHERE id_listen=?";
        try {
            Long id = (Long) findOneObjectBySQLQ(sq, new Options[]{new Options(idListen, 1)});
            if (insert && idListen > 0 && remoteId > 0 && (id != null ? id <= 0 : true)) {
                StringBuilder sb1 = new StringBuilder("INSERT INTO yvs_synchro_data_synchro(id_listen, date_save, id_distant)");
                sb1.append("VALUES (?,?,?)");
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                Query qr2 = em.createNativeQuery(sb1.toString());
                qr2.setParameter(1, idListen);
                qr2.setParameter(2, new Date());
                qr2.setParameter(3, remoteId);
                qr2.executeUpdate();
                em.getTransaction().commit();
                em.close();
            }
            if (idListen > 0 && message != null) {
                String sb1 = "UPDATE yvs_synchro_listen_table SET message=?, to_listen=? WHERE id=? ";
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                Query qr2 = em.createNativeQuery(sb1);
                qr2.setParameter(1, message);
                qr2.setParameter(2, !(message.equals("OK") && remoteId > 0));
                qr2.setParameter(3, idListen);
                qr2.executeUpdate();
                em.getTransaction().commit();
                em.close();
            }
        } catch (Exception ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Object[] findLocalKey(String table, Long idDistant) {
        //Vérifie d'abord dans une liste tempon
        //...
        String query = "SELECT l.id_source, l.id FROM yvs_synchro_data_synchro s INNER JOIN yvs_synchro_listen_table l ON l.id=s.id_listen "
                + "WHERE l.name_table=? AND s.id_distant=?::bigint ";
        try {
            try {
                EntityManager em = LocalDao.getInstance().getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                Query qr = em.createNativeQuery(query);
                qr.setParameter(1, table);
                qr.setParameter(2, idDistant);
                qr.setMaxResults(1);
                List<Object[]> re = qr.getResultList();
                em.getTransaction().commit();
                em.close();
                return (!re.isEmpty()) ? re.get(0) : null;
            } catch (NoResultException ex) {
                LogFiles.addLogInFile("Echec de l'execution de la requete " + query, ex);
                Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } catch (Exception ex) {
            //Logging de l'erreur
            LogFiles.addLogInFile("Echec de l'execution de la requete " + query, ex);
            Logger.getLogger(LocalDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public T afterCRUD(T entity, String action) {
        try {
            if (entity != null && (action != null ? !action.isEmpty() : false)) {
                if (entity.getClass().isAnnotationPresent(Table.class) && (entity instanceof YvsEntity)) {
                    YvsEntity instance = (YvsEntity) entity;
                    String name = ((Table) instance.getClass().getAnnotation(Table.class)).name();
                    String serverName = "127.0.0.1";
                    if (instance.getAdresseServeur() != null ? !instance.getAdresseServeur().isEmpty() : false) {
                        serverName = instance.getAdresseServeur();
                    }
                    if (Constantes.asString(name) && Constantes.asString(serverName)) {
                        if (instance.getId() != null ? instance.getId() > 0 : false) {
                            YvsSynchroServeurs serveur = (YvsSynchroServeurs) findOneByNQ("YvsSynchroServeurs.findByAdresseIp", new String[]{"adresseIp"}, new Object[]{serverName});
                            if (serveur != null ? serveur.getId() < 1 : true) {
                                serveur = null;
                            }
                            YvsSynchroListenTable listen = (YvsSynchroListenTable) findOneByNQ("YvsSynchroListenTable.findByActionSource", new String[]{"idSource", "nameTable", "action"}, new Object[]{instance.getId(), name, "DELETE"});;
                            YvsSynchroDataSynchro synchro;
                            if (listen != null ? listen.getId() < 1 : true) {
                                listen = new YvsSynchroListenTable();
                                listen.setActionName(action);
                                listen.setIdSource(instance.getId());
                                listen.setNameTable(name);
                                listen.setToListen(true);
                                listen.setServeur(serveur);
                                listen.setDateSave(new Date());
                                listen.setAuthor(instance.getAuthor() != null ? instance.getAuthor().getId() > 0 ? instance.getAuthor().getId() : null : null);
                                listen = (YvsSynchroListenTable) save1((T) listen, false);
                            } else {
                                listen.setToListen(true);
                                listen.setServeur(serveur);
                                listen.setAuthor(instance.getAuthor() != null ? instance.getAuthor().getId() > 0 ? instance.getAuthor().getId() : null : null);
                                update((T) listen, false);
                            }
                            if (serveur != null ? serveur.getId() > 0 : false) {
                                synchro = (YvsSynchroDataSynchro) findOneByNQ("YvsSynchroDataSynchro.findOne", new String[]{"listen", "distant", "serveur"}, new Object[]{listen, instance.getIdDistant(), serveur});
                                if (synchro != null ? synchro.getId() < 1 : true) {
                                    synchro = new YvsSynchroDataSynchro();
                                    synchro.setIdListen(listen);
                                    synchro.setServeur(serveur);
                                    synchro.setIdDistant(instance.getIdDistant());
                                    save1((T) synchro, false);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IllegalArgumentException | SecurityException ex) {
            LogFiles.addLogInFile("", ex);
            Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entity;
    }

    public void incrementNbFailed(Long idListenn, int nbFailed) {
        String query = "UPDATE yvs_synchro_listen_table SET nb_failed=?  WHERE id=? ";
        try {
            executeSqlQuery(query, new Options[]{new Options(nbFailed, 1), new Options(idListenn, 2)});
        } catch (NoResultException ex) {
            LogFiles.addLogInFile("Echec de l'execution de la requete " + query, ex);
            Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void notifyListenTable(Long idListenn, String message) {
        String query = "UPDATE yvs_synchro_listen_table SET message=?  WHERE id=? ";
        try {
            executeSqlQuery(query, new Options[]{new Options(message, 1), new Options(idListenn, 2)});
        } catch (NoResultException ex) {
            LogFiles.addLogInFile("Echec de l'execution de la requete " + query, ex);
            Logger.getLogger(LQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
