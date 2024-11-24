/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.dao.entity.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.lymytz.lymytzsell.service.utils.Constantes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @param <T>
 * @author Admin
 */
public final class LymytzLoaderEntity<T extends Serializable> {

    private static final Logger LOGGER = LogManager.getLogger(LymytzLoaderEntity.class);
    public static List<EntityClass> ALLENTITY = new ArrayList<>();

    public LymytzLoaderEntity(boolean base) {
        //Initialise la liste de classes
        if (Constantes.ALLENTITY_BASE.isEmpty()) {
            Constantes.initConstant();
        }
        if (base) {
            loadAllEntityBase();
        } else {
            loadAllEntityFonctionnelle();
        }
    }

    /*todo: trouver une lib qui permet de parser le xml*/
    public void loadAllEntityBase() {
        try {
            File file = new File(Objects.requireNonNull(LymytzLoaderEntity.class.getResource("/synchro/import-entities.xml")).getFile());
            XmlMapper xmlMapper = new XmlMapper();
            EntitiesClass entities = xmlMapper.readValue(file, EntitiesClass.class);
            ALLENTITY = entities.getEntities();
        } catch (IOException ex) {
            LOGGER.error("Lecture du fichier import-entities.xml impossible", ex);
        }
    }

    public void loadAllEntityFonctionnelle() {
        try {
            File file = new File(Objects.requireNonNull(LymytzLoaderEntity.class.getResource("/synchro/export-entities.xml")).getFile());
            XmlMapper xmlMapper = new XmlMapper();
            EntitiesClass entities = xmlMapper.readValue(file, EntitiesClass.class);
            ALLENTITY = entities.getEntities();
        } catch (IOException ex) {
            LOGGER.error("Lecture du fichier export-entities.xml impossible", ex);
        }
    }

    private String filterClassEntity(String className) {
        if (className != null) {
            try {
                Class classe = Class.forName(className);
                Table an = (Table) classe.getAnnotation(Table.class);
                if (an != null) {
                    return an.name();
                }
            } catch (ClassNotFoundException | SecurityException ex) {
                LOGGER.error(ex);
            }
        }
        return null;
    }

    /*Introspecte la classe pour récupérer ses colonnes*/
    public static List<EntityColumn> loadEntityColumn(String entity) {
        List<EntityColumn> re = new ArrayList<>();
        if (entity != null) {
            try {
                Class e = Class.forName(entity);
                String columnName = null;
                int idx = 0;
                String tableName = ((Table) e.getAnnotation(Table.class)).name();
                for (Field f : e.getDeclaredFields()) {
                    f.setAccessible(true);
                    //récupère les colonnes ayant l'annotation @Colum ou @JoinColumn
                    columnName = hasColumnOrJoinColumn(f);
                    if (columnName != null) {
                        EntityColumn c = new EntityColumn(f.getName(), columnName, f.getType());
                        c.setTableName(tableName);
                        c.setText(f.getName());
                        c.setColIndex(idx);
                        c.setJoinTable(getJoinTable(f));
                        c.setJoinkey(getJoinColumn(f));
                        c.setHasJoinColum(c.getJoinTable() != null);
                        re.add(c);
                        idx++;
                    }
                }
            } catch (ClassNotFoundException ex) {
                LOGGER.error(ex);
            }
        }
        return re;
    }

    private static String hasColumnOrJoinColumn(Field field) {
        if (field != null) {
            try {
                Column an0 = field.getAnnotation(Column.class);
                JoinColumn an1 = field.getAnnotation(JoinColumn.class);
                if (an0 != null || an1 != null) {
                    if (an0 != null) {
                        return an0.name();
                    } else {
                        return an1.name();
                    }
                }
            } catch (SecurityException ex) {
                LOGGER.error(ex);
            }
        }
        return null;
    }

    private static String getJoinTable(Field field) {
        if (field != null) {
            try {
                JoinColumn an1 = field.getAnnotation(JoinColumn.class);
                if (an1 != null) {
                    //si c'est un champ de jointure alors field.getType().getName() renvoie une Entity
                    return findTableForEntity(field.getType().getName());
                }
            } catch (SecurityException ex) {
                LOGGER.error(ex);
            }
        }
        return null;
    }

    private static String getJoinColumn(Field field) {
        if (field != null) {
            try {
                JoinColumn an1 = field.getAnnotation(JoinColumn.class);
                if (an1 != null) {
                    return an1.referencedColumnName();
                }
            } catch (SecurityException ex) {
                LOGGER.error(ex);
            }
        }
        return null;
    }

    private static String findTableForEntity(String entity) {

        try {
            Class<?> e = Class.forName(entity);
            Table ann = e.getAnnotation(Table.class);
            if (ann != null) {
                return ann.name();
            }
        } catch (ClassNotFoundException ex) {
            LOGGER.error(ex);
        }
        return null;
    }

}
