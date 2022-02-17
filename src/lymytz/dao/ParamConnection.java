/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.EOFException;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;

/**
 *
 * @author LENOVO
 */
public class ParamConnection implements Serializable {

    private String users, usersRemote;
    private String password, passwordRemote;
    private String sever, severRemote;
    private String port, portRemote;
    private String dataBase, dataBaseRemote;
    private String portWeb;
    private String hostWeb; 
    private Boolean useCodeBarre;
    private Boolean usePrinter;
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

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSever() {
        return sever;
    }

    public void setSever(String sever) {
        this.sever = sever;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getHostWeb() {
        return hostWeb;
    }

    public void setHostWeb(String hostWeb) {
        this.hostWeb = hostWeb;
    }

    public String getPortWeb() {
        return portWeb;
    }

    public void setPortWeb(String portWeb) {
        this.portWeb = portWeb;
    }

    public Boolean getUseCodeBarre() {
        return useCodeBarre != null ? useCodeBarre : false;
    }

    public void setUseCodeBarre(Boolean useCodeBarre) {
        this.useCodeBarre = useCodeBarre;
    }

    public Boolean getUsePrinter() {
        return usePrinter != null ? usePrinter : true;
    }

    public void setUsePrinter(Boolean usePrinter) {
        this.usePrinter = usePrinter;
    }

    public String getCheminPhotos() {
        return cheminPhotos;
    }

    public void setCheminPhotos(String cheminPhotos) {
        this.cheminPhotos = cheminPhotos;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getUsersRemote() {
        return usersRemote;
    }

    public void setUsersRemote(String usersRemote) {
        this.usersRemote = usersRemote;
    }

    public String getPasswordRemote() {
        return passwordRemote;
    }

    public void setPasswordRemote(String passwordRemote) {
        this.passwordRemote = passwordRemote;
    }

    public String getSeverRemote() {
        return severRemote;
    }

    public void setSeverRemote(String severRemote) {
        this.severRemote = severRemote;
    }

    public String getPortRemote() {
        return portRemote;
    }

    public void setPortRemote(String portRemote) {
        this.portRemote = portRemote;
    }

    public String getDataBaseRemote() {
        return dataBaseRemote;
    }

    public void setDataBaseRemote(String dataBaseRemote) {
        this.dataBaseRemote = dataBaseRemote;
    }

    public Double getP_width() {
        return p_width != null ? p_width : 0;
    }

    public void setP_width(Double p_width) {
        this.p_width = p_width;
    }

    public Double getP_height() {
        return p_height != null ? p_height : 0;
    }

    public void setP_height(Double p_height) {
        this.p_height = p_height;
    }

    public Double getP_ml() {
//        return p_ml != null ? p_ml : 0;
        return 0d;
    }

    public void setP_ml(Double p_ml) {
        this.p_ml = p_ml;
    }

    public Double getP_mr() {
        return p_mr != null ? p_mr : 0;
    }

    public void setP_mr(Double p_mr) {
        this.p_mr = p_mr;
    }

    public Double getP_mt() {
        return p_mt != null ? p_mt : 0;
    }

    public void setP_mt(Double p_mt) {
        this.p_mt = p_mt;
    }

    public Double getP_mb() {
        return p_mb != null ? p_mb : 0;
    }

    public void setP_mb(Double p_mb) {
        this.p_mb = p_mb;
    }

    public Boolean getP_default() {
        return p_default == null ? true : p_default;
    }

    public void setP_default(Boolean p_default) {
        this.p_default = p_default;
    }

    public String getCodeCaisse() {
        return codeCaisse;
    }

    public void setCodeCaisse(String codeCaisse) {
        this.codeCaisse = codeCaisse;
    }

    public long getCodeSociete() {
        return codeSociete;
    }

    public void setCodeSociete(long codeSociete) {
        this.codeSociete = codeSociete;
    }

    public long getCodeAgence() {
        return codeAgence;
    }

    public void setCodeAgence(long codeAgence) {
        this.codeAgence = codeAgence;
    }

    public long getClientDivers() {
        return clientDivers;
    }

    public void setClientDivers(long clientDivers) {
        this.clientDivers = clientDivers;
    }

    public long getVille() {
        return ville;
    }

    public void setVille(long ville) {
        this.ville = ville;
    }

    public long getSecteur() {
        return secteur;
    }

    public void setSecteur(long secteur) {
        this.secteur = secteur;
    }

    public long getModeReg() {
        return modeReg;
    }

    public void setModeReg(long modeReg) {
        this.modeReg = modeReg;
    }

    public long getModelReg() {
        return modelReg;
    }

    public void setModelReg(long modelReg) {
        this.modelReg = modelReg;
    }

    public String getTypeRapport() {
        return typeRapport;
    }

    public void setTypeRapport(String typeRapport) {
        this.typeRapport = typeRapport;
    }

    public long getIdRemoteScte() {
        return idRemoteScte;
    }

    public void setIdRemoteScte(long idRemoteScte) {
        this.idRemoteScte = idRemoteScte;
    }

    public void createFile(FileOutputStream fos) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (IOException ex) {
            LogFiles.addLogInFile("Erreur d'écritre sur le fichier", ex);
            Logger.getLogger(ParamConnection.class.getName()).log(Level.SEVERE, null, ex);
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
                LogFiles.addLogInFile("Fichier Introuvable", ex);
                Logger.getLogger(ParamConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (EOFException ex) {
            LogFiles.addLogInFile("Fichier Incorrecte, la fin du fichier est introuvable", ex);
        } catch (IOException ex) {
            LogFiles.addLogInFile("Erreur d'ouverture du fichier", ex);
        }
        return null;
    }
}
