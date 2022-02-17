/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import lymytz.dao.entity.YvsAgences;
import lymytz.dao.entity.YvsBaseModeReglement;
import lymytz.dao.entity.YvsBaseModelReglement;
import lymytz.dao.entity.YvsComClient;
import lymytz.dao.entity.YvsSocietes;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.EncryptMessage;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;

/**
 * FXML Controller class
 *
 * @author LYMYTZ
 */
public class PreferenceController implements Initializable, Controller {

    LQueryFactories dao = new LQueryFactories();

    @FXML
    private TextField TXT_IP_L;
    @FXML
    private TextField TXT_PORT_L;
    @FXML
    private TextField TXT_USER_L;
    @FXML
    private PasswordField TXT_PASSWORD_L;
    @FXML
    private ComboBox<YvsSocietes> CB_SOCIETE_L;
    @FXML
    private ComboBox<YvsAgences> CB_AGENCE_L;
    @FXML
    private ComboBox<YvsComClient> CB_CLT;
    @FXML
    private ComboBox<YvsBaseModelReglement> CB_MDR;
    @FXML
    private ComboBox<YvsBaseModeReglement> CB_MODE_R;
    @FXML
    private TextField TXT_IP_R;
    @FXML
    private TextField TXT_USER_R;
    @FXML
    private TextField TXT_BD_NAME_L;
    @FXML
    private TextField TXT_BD_NAME_R;
    @FXML
    private TextField TXT_PORT_R;
    @FXML
    private PasswordField TXT_PASSWORD_R;
    @FXML
    private TextField TXT_IP_WEB;
    @FXML
    private TextField TXT_PORT_WEB;
    @FXML
    private TextField TXT_SOCIETE_R;
    @FXML
    private TextField TXT_PORT_APP;
    @FXML
    private CheckBox CKB_CODE_BARRE;
    @FXML
    private ComboBox<String> CB_ENVIRONNEMENT;
    @FXML
    private ComboBox<String> CB_MODE;
    @FXML
    private DatePicker TXT_DATE_INIT;
    @FXML
    private ComboBox<String> CB_TYPE_PRINT;
    @FXML
    private CheckBox CKB_PRINT;
    @FXML
    private TextField TXT_M_H;
    @FXML
    private TextField TXT_M_B;
    @FXML
    private TextField TXT_M_G;
    @FXML
    private TextField TXT_M_D;
    @FXML
    private TextField TXT_PATH;
    @FXML
    private TextField TXT_PAPER_H;
    @FXML
    private TextField TXT_PAPER_L;

    @FXML
    private Button BTN_FILE;
    @FXML
    private Button BTN_SAVE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDataView();
        readDataFromFile();
    }

    private void readDataFromFile() {
        UtilsProject.loadFilePropertie();
        TXT_IP_L.setText(getProp(Constantes.KEY_LOCAL_HOST));
        TXT_PORT_L.setText(getProp(Constantes.KEY_LOCAL_PORT));
        TXT_USER_L.setText(EncryptMessage.decrypt(getProp(Constantes.KEY_LOCAL_USERS), Constantes.KEY_ENCRYPT));
        TXT_PASSWORD_L.setText(EncryptMessage.decrypt(getProp(Constantes.KEY_LOCAL_PASSWORD), Constantes.KEY_ENCRYPT));
        TXT_BD_NAME_L.setText(getProp(Constantes.KEY_LOCAL_DB_NAME));

        TXT_IP_R.setText(getProp(Constantes.KEY_REMOTE_HOST));
        TXT_PORT_R.setText(getProp(Constantes.KEY_REMOTE_PORT));
        TXT_USER_R.setText(EncryptMessage.decrypt(getProp(Constantes.KEY_REMOTE_USERS), Constantes.KEY_ENCRYPT));
        TXT_PASSWORD_R.setText(EncryptMessage.decrypt(getProp(Constantes.KEY_REMOTE_PASSWORD), Constantes.KEY_ENCRYPT));
        TXT_BD_NAME_R.setText(getProp(Constantes.KEY_REMOTE_DB_NAME));
        String societe = getProp(Constantes.KEY_LOCAL_SOCIETE);
        String agence = getProp(Constantes.KEY_LOCAL_AGENCE);
        if (societe != null) {
            CB_SOCIETE_L.setValue(new YvsSocietes(Long.valueOf(societe)));
        }
        if (agence != null) {
            CB_AGENCE_L.setValue(new YvsAgences(Long.valueOf(agence)));
        }

        TXT_IP_WEB.setText(getProp(Constantes.KEY_WEB_HOST));
        TXT_PORT_WEB.setText(getProp(Constantes.KEY_WEB_PORT));
        TXT_SOCIETE_R.setText(getProp(Constantes.KEY_REMOTE_SOCIETE));

        TXT_PORT_APP.setText(getProp(Constantes.KEY_APPS_PORT));
        CB_ENVIRONNEMENT.setValue(getProp(Constantes.KEY_ENVIRONNEMENT));
        CB_MODE.setValue(getProp(Constantes.KEY_MODE));
        String date_ = getProp(Constantes.KEY_DATE_INIT);
        if (Constantes.asString(date_)) {
            Integer[] d = getDate(date_);
            if (d != null) {
                TXT_DATE_INIT.setValue(LocalDate.of(d[2], d[1], d[0]));
            }
        }
        CKB_CODE_BARRE.setSelected(Boolean.valueOf(getProp(Constantes.KEY_USE_CODE_BARRE)));
        CKB_PRINT.setSelected(Boolean.valueOf(getProp(Constantes.KEY_USE_PRINTER)));
        CB_TYPE_PRINT.setValue(getProp(Constantes.KEY_TYPE_PRINT));
        TXT_M_B.setText(getProp(Constantes.KEY_PAPER_M_BOTOM));
        TXT_M_H.setText(getProp(Constantes.KEY_PAPER_M_TOP));
        TXT_M_G.setText(getProp(Constantes.KEY_PAPER_M_LEFT));
        TXT_M_D.setText(getProp(Constantes.KEY_PAPER_M_RIGHT));
        TXT_PAPER_H.setText(getProp(Constantes.KEY_PAPER_HEIGHT));
        TXT_PAPER_L.setText(getProp(Constantes.KEY_PAPER_WIDTH));

        String client = getProp(Constantes.KEY_CLIENT_DIVERS);
        if (societe != null && client != null) {
            CB_CLT.setValue(new YvsComClient(Long.valueOf(client)));
        }
        String mdr = getProp(Constantes.KEY_MODEL_REGLEMENT);
        if (societe != null && mdr != null) {
            CB_MDR.setValue(new YvsBaseModelReglement(Long.valueOf(mdr)));
        }
        String moder = getProp(Constantes.KEY_MODE_REGLEMENT);
        if (societe != null && mdr != null) {
            CB_MODE_R.setValue(new YvsBaseModeReglement(Long.valueOf(moder)));
        }

        TXT_PATH.setText(getProp(Constantes.KEY_PATH));

    }

    public boolean copyToSave() {
        File file = new File("conf/application.properties");
        if (file.exists()) {
            FileOutputStream oStream = null;
            try {
                UtilsProject.properties.setProperty(Constantes.KEY_APPS_PORT, getVal(TXT_PORT_APP.getText()));
                if (CB_CLT.getValue() != null ? Constantes.asLong(CB_CLT.getValue().getId()) : false) {
                    UtilsProject.properties.setProperty(Constantes.KEY_CLIENT_DIVERS, CB_CLT.getValue().getId().toString());
                }
                UtilsProject.properties.setProperty(Constantes.KEY_ENVIRONNEMENT, getVal(CB_ENVIRONNEMENT.getValue()));
                if (CB_AGENCE_L.getValue() != null ? Constantes.asLong(CB_AGENCE_L.getValue().getId()) : false) {
                    UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_AGENCE, CB_AGENCE_L.getValue().getId().toString());
                }
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_DB_NAME, getVal(TXT_BD_NAME_L.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_HOST, getVal(TXT_IP_L.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_PASSWORD, EncryptMessage.encrypt(getVal(TXT_PASSWORD_L.getText()), Constantes.KEY_ENCRYPT));
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_PORT, getVal(TXT_PORT_L.getText()));
                if (CB_SOCIETE_L.getValue() != null ? Constantes.asLong(CB_SOCIETE_L.getValue().getId()) : false) {
                    UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_SOCIETE, CB_SOCIETE_L.getValue().getId().toString());
                }
                UtilsProject.properties.setProperty(Constantes.KEY_LOCAL_USERS, EncryptMessage.encrypt(getVal(TXT_USER_L.getText()), Constantes.KEY_ENCRYPT));
                UtilsProject.properties.setProperty(Constantes.KEY_MODE, getVal(CB_MODE.getValue()));
                if (CB_MDR.getValue() != null ? Constantes.asLong(CB_MDR.getValue().getId()) : false) {
                    UtilsProject.properties.setProperty(Constantes.KEY_MODEL_REGLEMENT, CB_MDR.getValue().getId().toString());
                }
                if (CB_MODE_R.getValue() != null ? Constantes.asLong(CB_MODE_R.getValue().getId()) : false) {
                    UtilsProject.properties.setProperty(Constantes.KEY_MODE_REGLEMENT, CB_MODE_R.getValue().getId().toString());
                }
                UtilsProject.properties.setProperty(Constantes.KEY_ORIENTATION_PRINT, "");
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_HEIGHT, getVal(TXT_PAPER_H.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_WIDTH, getVal(TXT_PAPER_L.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_M_BOTOM, getVal(TXT_M_B.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_M_LEFT, getVal(TXT_M_G.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_M_RIGHT, getVal(TXT_M_D.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_PAPER_M_TOP, getVal(TXT_M_H.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_PATH, getVal(TXT_PATH.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_DB_NAME, getVal(TXT_BD_NAME_R.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_HOST, getVal(TXT_IP_R.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_PASSWORD, EncryptMessage.encrypt(getVal(TXT_PASSWORD_R.getText()), Constantes.KEY_ENCRYPT));
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_PORT, getVal(TXT_PORT_R.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_SOCIETE, getVal(TXT_SOCIETE_R.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_REMOTE_USERS, EncryptMessage.encrypt(getVal(TXT_USER_R.getText()), Constantes.KEY_ENCRYPT));
                UtilsProject.properties.setProperty(Constantes.KEY_SECTEUR, "");
                UtilsProject.properties.setProperty(Constantes.KEY_TYPE_PRINT, getVal(CB_TYPE_PRINT.getValue()));
                UtilsProject.properties.setProperty(Constantes.KEY_USE_CODE_BARRE, "" + CKB_CODE_BARRE.isSelected());
                UtilsProject.properties.setProperty(Constantes.KEY_USE_PRINTER, CKB_PRINT.isSelected() + "");
                UtilsProject.properties.setProperty(Constantes.KEY_VILLE, "");
                UtilsProject.properties.setProperty(Constantes.KEY_WEB_HOST, getVal(TXT_IP_WEB.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_WEB_PORT, getVal(TXT_PORT_WEB.getText()));
                UtilsProject.properties.setProperty(Constantes.KEY_DATE_INIT, getDate());
                oStream = new FileOutputStream(file);
                UtilsProject.properties.store(oStream, "");
                return true;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PreferenceController.class.getName()).log(Level.SEVERE, null, ex);
                LymytzService.openExceptionDialog("Enregistrement non réussi !", "", "", Alert.AlertType.ERROR, ex);
            } catch (IOException ex) {
                Logger.getLogger(PreferenceController.class.getName()).log(Level.SEVERE, null, ex);
                LymytzService.openExceptionDialog("Enregistrement non réussi !", "", "", Alert.AlertType.ERROR, ex);
            } finally {
                try {
                    if (oStream != null) {
                        oStream.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PreferenceController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    private void initDataView() {
        ObservableList<String> modes = FXCollections.observableArrayList();
        modes.add(Constantes.APPS_MODE_BOTH);
        modes.add(Constantes.APPS_MODE_SINGLE);
        ObservableList<String> envs = FXCollections.observableArrayList();
        envs.add(Constantes.APPS_ENV_PROD);
        envs.add(Constantes.APPS_ENV_DEV);
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add(Constantes.TYPE_PRINT_TICKET);
        types.add(Constantes.TYPE_PRINT_A4);
        CB_ENVIRONNEMENT.setItems(envs);
        CB_MODE.setItems(modes);
        CB_TYPE_PRINT.setItems(types);
        //charge les mdr disponible
        List<YvsBaseModelReglement> models = dao.loadByNamedQuery("YvsBaseModelReglement.findAll", new String[]{}, new Object[]{});
        CB_MDR.setItems(FXCollections.observableArrayList(models));
        CB_MDR.setConverter(new StringConverter<YvsBaseModelReglement>() {

            @Override
            public String toString(YvsBaseModelReglement object) {
                if (object != null) {
                    return object.getReference();
                }
                return "";
            }

            @Override
            public YvsBaseModelReglement fromString(String string) {
                return null;
            }
        });
//charge les mode de règlement disponible
        List<YvsBaseModeReglement> modes_R = dao.loadByNamedQuery("YvsBaseModeReglement.findAll", new String[]{}, new Object[]{});
        CB_MODE_R.setItems(FXCollections.observableArrayList(modes_R));
        CB_MODE_R.setConverter(new StringConverter<YvsBaseModeReglement>() {

            @Override
            public String toString(YvsBaseModeReglement object) {
                if (object != null) {
                    return object.getDesignation();
                }
                return "";
            }

            @Override
            public YvsBaseModeReglement fromString(String string) {
                return null;
            }
        });
        //charge les clients disponible
        if (UtilsProject.listClients != null) {
            CB_CLT.setItems(FXCollections.observableArrayList(UtilsProject.listClients));
            CB_CLT.setConverter(new StringConverter<YvsComClient>() {

                @Override
                public String toString(YvsComClient object) {
                    if (object != null) {
                        return object.getNom_prenom() + "[" + object.getCodeClient() + "]";
                    }
                    return "";
                }

                @Override
                public YvsComClient fromString(String string) {
                    return null;
                }
            });
        }
        //charge les agences disponible
        List<YvsAgences> agences = dao.loadByNamedQuery("YvsAgences.findAll", new String[]{}, new Object[]{});
        CB_AGENCE_L.setItems(FXCollections.observableArrayList(agences));
        CB_AGENCE_L.setConverter(new StringConverter<YvsAgences>() {

            @Override
            public String toString(YvsAgences object) {
                if (object != null) {
                    return object.getDesignation();
                }
                return "";
            }

            @Override
            public YvsAgences fromString(String string) {
                return null;
            }
        });
//charge les societe disponible
        List<YvsSocietes> societes = dao.loadByNamedQuery("YvsSocietes.findAll", new String[]{}, new Object[]{});
        CB_SOCIETE_L.setItems(FXCollections.observableArrayList(societes));
        CB_SOCIETE_L.setConverter(new StringConverter<YvsSocietes>() {

            @Override
            public String toString(YvsSocietes object) {
                if (object != null) {
                    return object.getName();
                }
                return "";
            }

            @Override
            public YvsSocietes fromString(String string) {
                return null;
            }
        });
    }

    private String getProp(String key) {
        try {
            String re = UtilsProject.properties.getProperty(key);
            return (Constantes.asString(re) ? re : null);
        } catch (Exception ex) {
            LogFiles.addLogInFile("Récupération de la date erronée !", ex);
        }
        return null;
    }

    private String getVal(String key) {
        if (Constantes.asString(key)) {
            return key;
        } else {
            return "";
        }
    }

    private Integer[] getDate(String format) {
        try {
            if (format != null) {
                String[] date_ = format.split("-");
                int d = Integer.valueOf(date_[0]);
                int m = Integer.valueOf(date_[1]);
                int y = Integer.valueOf(date_[2]);
                return new Integer[]{d, m, y};
            }
        } catch (NumberFormatException ex) {
            LogFiles.addLogInFile("Récupération de la date erronée !", ex);
        }
        return null;
    }

    private String getDate() {
        LocalDate local = TXT_DATE_INIT.getValue();
        Date date;
        if (local != null) {
            date = Date.from(local.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            return Constantes.dfD.format(date);
        }
        return Constantes.dfD.format(new Date());
    }

    @FXML
    public void saveConfig(ActionEvent ev) {

        if (copyToSave()) {
            LymytzService.success();
        } else {

        }
    }

    @Override
    public void freeMemoryController() {
    }

}
