/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.EOFException;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.service.utils.log.LogFiles;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author LENOVO
 */
@Setter
@Getter
public class ParamConnection implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(ParamConnection.class);

    private String users, usersRemote;
    private String password, passwordRemote;
    private String sever, severRemote;
    private String port, portRemote;
    private String dataBase, dataBaseRemote;
    private String portWeb;
    private String hostWeb; 
    private Boolean useCodeBarre;
    private Boolean usePrinter;
    private Boolean loadCatalogue;
    private String cheminPhotos;

    private String orientation;
    private Double p_width, p_height, p_ml, p_mr, p_mt, p_mb;
    private Boolean p_default;
    private String typeRapport = UtilsProject.TYPE_RAPPORT_TICKET;

    private String codeCaisse;
    private long idRemoteScte;
    private long codeSociete;
    private long codeAgence;
    private long clientDivers;
    private long ville;
    private long secteur;
    private long modelReg;
    private long modeReg;

    public ParamConnection() {
    }

    public ParamConnection(String users, String password, String sever, String port, String dataBase) {
        this.users = users;
        this.password = password;
        this.sever = sever;
        this.port = port;
        this.dataBase = dataBase;
    }

    public Boolean getUseCodeBarre() {
        return useCodeBarre != null ? useCodeBarre : false;
    }

    public Boolean getUsePrinter() {
        return usePrinter != null ? usePrinter : true;
    }

    public Double getP_width() {
        return p_width != null ? p_width : 0;
    }

    public Double getP_height() {
        return p_height != null ? p_height : 0;
    }

    public Double getP_ml() {
        return p_ml != null ? p_ml : 0;
    }

    public Double getP_mr() {
        return p_mr != null ? p_mr : 0;
    }

    public Double getP_mt() {
        return p_mt != null ? p_mt : 0;
    }

    public Double getP_mb() {
        return p_mb != null ? p_mb : 0;
    }

    public Boolean getP_default() {
        return p_default == null || p_default;
    }

    public void createFile(FileOutputStream fos) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (IOException ex) {
            LOGGER.error("Erreur d'écritre sur le fichier", ex);
        }
    }

    public static ParamConnection readFile(FileInputStream fis) {
        try {
            try {
                ParamConnection p;
                try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                    p = (ParamConnection) ois.readObject();
                }
                return p;
            } catch (ClassNotFoundException ex) {
                LOGGER.error("Fichier Introuvable", ex);
            }
        } catch (EOFException ex) {
            LOGGER.error("Fichier Incorrecte, la fin du fichier est introuvable", ex);
        } catch (IOException ex) {
            LOGGER.error("Erreur d'ouverture du fichier", ex);
        }
        return null;
    }
}
