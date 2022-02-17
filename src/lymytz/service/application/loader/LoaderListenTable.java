/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.loader;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lymytz.dao.Options;
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.application.bean.ListenTableBean;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;

/**
 *
 * @author LYMYTZ
 */
public class LoaderListenTable extends Task<ObservableList<ListenTableBean>> {

    LQueryFactories Ldao;
    RQueryFactories Rdao;
    boolean local;
    boolean loadAll;

    public LoaderListenTable(boolean local, boolean all) {
        this.local = local;
        this.loadAll = all;
        if (local) {
            Ldao = new LQueryFactories();
        } else {
            Rdao = new RQueryFactories();
        }
    }

    @Override
    public ObservableList<ListenTableBean> call() throws Exception {
        String query;
        List<Object[]> l;
        if (local) {
            if (!loadAll) {
                query = "SELECT l.id,l.name_table, l.id_source, l.action_name,l.message,l.date_save "
                        + "FROM yvs_synchro_listen_table l WHERE (l.to_listen=true OR (l.message IS NOT NULL AND l.to_listen=true)) "
                        + "AND name_table IN ( "+Constantes.TABLES_TO_SYNCHRO+" ) "
                        + "ORDER BY l.id";
            } else {
                query = "SELECT l.id,l.name_table, l.id_source, l.action_name,l.message,l.date_save "
                        + "FROM yvs_synchro_listen_table l WHERE "
                        + "name_table IN ( "+Constantes.TABLES_TO_SYNCHRO+" ) "
                        + "ORDER BY l.id";
            }
            l = Ldao.loadBySQLQuery(query, new Options[]{});
        } else {
            query = Constantes.getQueryListenData().replace("LIMIT 1", "");
            String dure_init = UtilsProject.properties.get("DATE_INIT").toString();
            Date date = getDate(dure_init);
            l = Rdao.loadBySQLQuery(query, new Options[]{new Options(UtilsProject.ID_SERVEUR, 1), new Options(UtilsProject.RcurrentSociete.getId(), 2), new Options(date, 3)});
        }
        int i = 1;
        ObservableList<ListenTableBean> result = FXCollections.observableArrayList();
        try {
            if (l.size() > 0) {
                for (Object[] line : l) {
                    result.add(buildBeanFacture(line, i));
                    this.updateProgress(i, l.size());
                    this.updateMessage(i + " sur " + l.size());
                    i++;
                }
            } else {
                this.updateProgress(0, 0);
                this.updateMessage(0 + " sur " + l.size());
            }
        } catch (Exception ex) {
            Logger.getLogger(LoaderListenTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private ListenTableBean buildBeanFacture(Object[] row, int line) {
        ListenTableBean re = null;
        try {
            re = new ListenTableBean();
            if (row[0].getClass().getSimpleName().equals("Long")) {
                re.setId((Long) row[0]);
            } else {
                re.setId(Long.valueOf((String) row[0]));
            }
            re.setTable((String) row[1]);
            if (row[2].getClass().getSimpleName().equals("Long")) {
                re.setIdSource((Long) row[2]);
            } else {
                re.setIdSource(Long.valueOf((String) row[2]));
            }
            re.setAction((String) row[3]);
            re.setMessage((String) row[4]);
            if (row[5] != null) {
//                re.setDateSave(Constantes.dfh.format(getDate((String) row[5])));
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(LoaderListenTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return re;
    }

    private Date getDate(String format) {
        try {
            if (Constantes.asString(format)) {
                String[] date_ = format.split("-");
                int d = Integer.valueOf(date_[0]);
                int m = Integer.valueOf(date_[1]);
                int y = Integer.valueOf(date_[2]);
                Calendar cal = Calendar.getInstance();
                cal.set(y, (m - 1), d);
                return cal.getTime();
            }
        } catch (NumberFormatException ex) {
            LogFiles.addLogInFile("Récupération de la date erronée !", ex);
            Logger.getLogger(LoaderListenTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date();
    }

}
