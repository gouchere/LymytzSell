/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.view.main.synchro.data;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import lymytz.dao.entity.service.EntityColumn;
import lymytz.dao.entity.service.LymytzEntityClass;
import lymytz.dao.entity.service.LymytzLoaderEntity;

/**
 *
 * @author Admin
 */
public class FactoryColumn {

    private String table;

    public FactoryColumn() {
    }

    public FactoryColumn(String table) {
        this.table = table;
    }

    public static List<TableColumn> factoryTableColum(LymytzEntityClass entity) {
        //1. récupérer la classe à partir du nom de la table
        List<TableColumn> re=new ArrayList<>();
        if (entity != null) {
            TableColumn column;
            for (EntityColumn e : LymytzLoaderEntity.loadEntityColumn(entity.getEntity())) {
                column=new TableColumn(e.getName());
                re.add(column);
            }
        }
        //Parcourir les annotation colum et créer les colonne
        return re;
    }
}
