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
import lymytz.dao.query.LQueryFactories;
import lymytz.dao.query.RQueryFactories;

/**
 *
 * @author Admin
 */
public class LoaderLocalImportData extends Task<ObservableList<ObservableList>> {

    LQueryFactories dao = new LQueryFactories();
    private String methode;
    private String table;
    private String colFilter;
    private Object valueFilter;
    public List<EntityColumn> colonnes;
    public List<LymytzData> listData;

    public LoaderLocalImportData() {
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

    public String getColFilter() {
        return colFilter;
    }

    public void setColFilter(String colFilter) {
        this.colFilter = colFilter;
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
            rs.setFetchSize(1000);
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
                line = new LymytzData(new EntityColumn[colonnes.size() + 1]);
                row.add(String.valueOf(progress + 1));
//                for (EntityColumn c : colonnes) {
//                    line.getValue()[idx] = fillColumn(rs.getMetaData().getColumnTypeName(idx + 1), c, rs, (idx + 1));
//                    row.add(fillColumn(rs.getMetaData().getColumnTypeName(idx + 1), rs, (idx + 1)));
//                    idx++;
//                }
                for (int i = 0; i < (colonnes.size() + 1); i++) {
                    switch (i) {
                        case 0:
                            line.getValue()[i] = fillColumn(rs.getMetaData().getColumnTypeName(i + 1), new EntityColumn("id_distant", "id_distant", null, Long.TYPE), rs, (i + 1));
                            break;
                        default:
                            EntityColumn c = colonnes.get(idx);
                            line.getValue()[i] = fillColumn(rs.getMetaData().getColumnTypeName(i + 1), c, rs, (i + 1));
                            idx++;
                            break;
                    }
                    row.add(fillColumn(rs.getMetaData().getColumnTypeName(idx + 1), rs, (idx + 1)));
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

    private EntityColumn fillColumn(String type, EntityColumn c, ResultSet rs, int colIndex) {
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

    @Override
    protected ObservableList<ObservableList> call() throws Exception {
        return requeteLibre(dao.buildeGenericQuery(table, colonnes, colFilter, valueFilter));
    }

}
