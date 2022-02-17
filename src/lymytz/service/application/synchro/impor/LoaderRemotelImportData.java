/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.synchro.impor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.dao.entity.service.LymytzData;
import lymytz.dao.query.RQueryFactories;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;

/**
 *
 * @author Admin
 */
public class LoaderRemotelImportData extends Task<ObservableList<ObservableList>> {

    RQueryFactories dao = new RQueryFactories();
    private String methode;
    private String table;
    private String[] colFilter;
    private Object[] valueFilters;
    private Object valueFilter;
    List<EntityColumn> colonnes;
    public List<LymytzData> listData;

    public LoaderRemotelImportData() {
        listData = new ArrayList<>();
    }

    public List<LymytzData> getListData() {
        return listData;
    }

    public void setListData(List<LymytzData> listData) {
        this.listData = listData;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<EntityColumn> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<EntityColumn> colonnes) {
        this.colonnes = colonnes;
    }

    public String[] getColFilter() {
        return colFilter;
    }

    public void setColFilter(String[] colFilter) {
        this.colFilter = colFilter;
    }

    public Object[] getValueFilters() {
        return valueFilters;
    }

    public void setValueFilters(Object[] valueFilters) {
        this.valueFilters = valueFilters;
    }

    public Object getValueFilter() {
        return valueFilter;
    }

    public void setValueFilter(Object valueFilter) {
        this.valueFilter = valueFilter;
    }

    public ObservableList<ObservableList> requeteLibre(PreparedStatement st) {
        ObservableList<ObservableList> re = FXCollections.observableArrayList();
        try (ResultSet rs = st.executeQuery()) {
            rs.setFetchSize(10000);
            //construction du résultat
            //recherche du nombre max de ligne: Positionne le curseur à la fin
            rs.last();
            int max = rs.getRow();
            max = (max == 0) ? 1 : max;
            //revient à la première ligne
            rs.beforeFirst();
            int idx, progress = 0;
            ObservableList<String> row;
            listData.clear();
            LymytzData line;
            while (rs.next()) {
                row = FXCollections.observableArrayList();
                idx = 0;
                line = new LymytzData(new EntityColumn[colonnes.size()]);
                line.setIdListen(rs.getLong(idx + 1));
                row.add(String.valueOf(progress + 1));
                idx++;
                try {
                    for (EntityColumn c : colonnes) {
                        line.getValue()[idx - 1] = UtilsProject.fillColumn(rs.getMetaData().getColumnTypeName(idx + 1), c, rs, (idx + 1));
                        if (c.getName().equals("id")) {
                            line.setIdDistant(Long.valueOf(line.getValue()[idx - 1].getColumnValue().toString()));
                        }
                        row.add(fillColumn(rs.getMetaData().getColumnTypeName(idx + 1), rs, (idx + 1)));
                        idx++;
                    }
                } catch (SQLException | NumberFormatException e) {
                    st.close();
                    LogFiles.addLogInFile("", new Exception(e));
                    Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, e);
                }
                listData.add(line);
                re.add(row);
                progress++;
                this.updateProgress(progress, max);
                this.updateMessage(progress + " ligne sur " + max);
            }
            if (re.isEmpty()) {
                this.updateProgress(100, 100);
                this.updateMessage(0 + " ligne sur " + 0);
            }
            st.close();
        } catch (SQLException ex) {
            try {
                st.close();
            } catch (SQLException ex1) {
                LogFiles.addLogInFile("", new Exception(ex1));
                Logger.getLogger(LoaderRemotelImportData.class.getName()).log(Level.SEVERE, null, ex1);
            }
            LogFiles.addLogInFile("Erreur à l'exécution de la requête", new Exception(ex));
            Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
        }
        return re;
    }

    private String fillColumn(String type, ResultSet rs, int colIndex) {
        String re = null;
        try {
            if (type != null) {
                switch (type) {
                    case "bigint":
                    case "bigserial":
                        re = ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getLong(colIndex)) : "");
                        break;
                    case "double precision":
                        re = ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getDouble(colIndex)) : "");
                        break;
                    case "character varying":
                        re = ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getString(colIndex)) : "");
                        break;
                    default:
                        re = ((rs.getObject(colIndex) != null) ? String.valueOf(rs.getObject(colIndex)) : "");
                        break;

                }
            }
        } catch (SQLException ex) {
            try {
                rs.close();
            } catch (SQLException ex1) {
                LogFiles.addLogInFile("Erreur à la fermeture de la requête", new Exception(ex));
                Logger.getLogger(LoaderRemotelImportData.class.getName()).log(Level.SEVERE, null, ex1);
            }
            LogFiles.addLogInFile("Erreur à l'exécution de la requête", new Exception(ex));
            Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
        }
        return re;
    }

    @Override
    protected ObservableList<ObservableList> call() throws Exception {
        try {
            if (valueFilter != null) {
                valueFilters[valueFilters.length] = valueFilter;
            }
            return requeteLibre(dao.buildeGenericRemoteQuery(table, colonnes, colFilter, valueFilters));
        } catch (Exception ex) {
            Logger.getLogger(RQueryFactories.class.getName()).log(Level.SEVERE, null, ex);
        }
        return FXCollections.observableArrayList();
    }

}
