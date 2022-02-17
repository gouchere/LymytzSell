/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao.entity.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.log.LogFiles;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Admin
 * @param <T>
 *
 */
public final class LymytzLoaderEntity<T extends Serializable> {

//    String path = "";
//    List<String> chemin;
    public static List<LymytzEntityClass> ALLENTITY = new ArrayList<>();

    public LymytzLoaderEntity(boolean base) {
        //Initialise la liste de classes
        if (Constantes.ALLENTITY_BASE.isEmpty()) {
            new Constantes();
        }
        if (base) {
            loadAllEntityBase();
        } else {
            loadAllEntityFonctionnelle();
        }
    }

    public void loadAllEntityBase() {
        try (InputStream in = ClassLoader.getSystemResourceAsStream("lymytz/view/resources/persistence.xml")) {
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            SAXBuilder sax = new SAXBuilder();
            org.jdom.Document doc = (org.jdom.Document) sax.build(input);
            Element root = doc.getRootElement();
            List list = new ArrayList<>(root.getChildren("entity"));
            if (!list.isEmpty()) {
                Element nodeEntity, ename, path, table;
                ALLENTITY.clear();
                for (Object list1 : list) {
                    nodeEntity = (Element) list1;
                    ALLENTITY.add(new LymytzEntityClass(nodeEntity.getValue().trim(), nodeEntity.getAttribute("name").getValue(), nodeEntity.getAttribute("table").getValue()));
                }
            }
        } catch (FileNotFoundException ex) {
            LogFiles.addLogInFile("Lecture du fichier persistence.xml impossible", ex);
            Logger.getLogger(LymytzLoaderEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | JDOMException ex) {
            LogFiles.addLogInFile("Lecture du fichier persistence.xml impossible", ex);
            Logger.getLogger(LymytzLoaderEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAllEntityFonctionnelle() {
        try (InputStream in = ClassLoader.getSystemResourceAsStream("lymytz/view/resources/persistence_com.xml")) {
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            SAXBuilder sax = new SAXBuilder();
            org.jdom.Document doc = (org.jdom.Document) sax.build(input);
            Element root = doc.getRootElement();
            List list = new ArrayList<>(root.getChildren("entity"));
            if (!list.isEmpty()) {
                Element nodeEntity, ename, path, table;
                ALLENTITY.clear();
                for (int i = 0; i < list.size(); i++) {
                    nodeEntity = (Element) list.get(i);
                    ALLENTITY.add(new LymytzEntityClass(nodeEntity.getValue().trim(), nodeEntity.getAttribute("name").getValue(), nodeEntity.getAttribute("table").getValue()));
                }
            }
        } catch (FileNotFoundException ex) {
            LogFiles.addLogInFile("Lecture du fichier persistence.xml impossible", ex);
            Logger.getLogger(LymytzLoaderEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | JDOMException ex) {
            LogFiles.addLogInFile("Lecture du fichier persistence.xml impossible", ex);
            Logger.getLogger(LymytzLoaderEntity.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(LymytzLoaderEntity.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(LymytzLoaderEntity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return re;
    }

    private static String hasColumnOrJoinColumn(Field field) {
        if (field != null) {
            try {
                Column an0 = (Column) field.getAnnotation(Column.class);
                JoinColumn an1 = (JoinColumn) field.getAnnotation(JoinColumn.class);
                if (an0 != null || an1 != null) {
                    if (an0 != null) {
                        return an0.name();
                    } else {
                        return an1.name();
                    }
                }
            } catch (SecurityException ex) {
                Logger.getLogger(LymytzLoaderEntity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private static String getJoinTable(Field field) {
        if (field != null) {
            try {
                JoinColumn an1 = (JoinColumn) field.getAnnotation(JoinColumn.class);
                if (an1 != null) {
                    //si c'est un champ de jointure alors field.getType().getName() renvoie une Entity
                    return findTableForEntity(field.getType().getName());
                }
            } catch (SecurityException ex) {
                Logger.getLogger(LymytzLoaderEntity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private static String getJoinColumn(Field field) {
        if (field != null) {
            try {
                JoinColumn an1 = (JoinColumn) field.getAnnotation(JoinColumn.class);
                if (an1 != null) {
                    return an1.referencedColumnName();
                }
            } catch (SecurityException ex) {
                Logger.getLogger(LymytzLoaderEntity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private static String findTableForEntity(String entity) {

        try {
            Class e = Class.forName(entity);
            Table ann = (Table) e.getAnnotation(Table.class);
            if (ann != null) {
                return ann.name();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LymytzLoaderEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
