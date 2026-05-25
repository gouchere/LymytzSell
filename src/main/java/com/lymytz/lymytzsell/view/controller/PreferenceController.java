/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.controller;

import com.lymytz.lymytzsell.business.helpers.Helpers;
import com.lymytz.lymytzsell.persistence.entity.YvsAgences;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseModeReglement;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseModelReglement;
import com.lymytz.lymytzsell.persistence.entity.YvsComClient;
import com.lymytz.lymytzsell.persistence.entity.YvsEntity;
import com.lymytz.lymytzsell.persistence.entity.YvsSocietes;
import com.lymytz.lymytzsell.persistence.dao.LocalQueryFactories;
import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.EncryptMessage;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.lymytz.lymytzsell.service.utils.Constantes.PROPERTIE_FILE_NAME;
import static com.lymytz.lymytzsell.service.utils.MessagesConstants.KEY_VALUE;
import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * FXML Controller class
 *
 * @author LYMYTZ
 */
public class PreferenceController implements Initializable, Controller {

    LocalQueryFactories dao = new LocalQueryFactories();
    private static final org.apache.logging.log4j.Logger LOGGER = getLogger(PreferenceController.class.getName());

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
    @FXML
    private ToggleGroup NB_COL_CATALOGUE;
    @FXML
    private ToggleGroup NB_LINE_CATALOGUE;
    @FXML
    private RadioButton CATALOGUE_DISPLAY_2_COL;
    @FXML
    private RadioButton CATALOGUE_DISPLAY_3_COL;
    @FXML
    private RadioButton CATALOGUE_DISPLAY_5_LINE;
    @FXML
    private RadioButton CATALOGUE_DISPLAY_6_LINE;
    @FXML
    private RadioButton CATALOGUE_DISPLAY_10_LINE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDataView();
        readDataFromFile();
        CATALOGUE_DISPLAY_2_COL.getProperties().put(KEY_VALUE, "2");
        CATALOGUE_DISPLAY_3_COL.getProperties().put(KEY_VALUE, "3");
        CATALOGUE_DISPLAY_5_LINE.getProperties().put(KEY_VALUE, "5");
        CATALOGUE_DISPLAY_6_LINE.getProperties().put(KEY_VALUE, "6");
        CATALOGUE_DISPLAY_10_LINE.getProperties().put(KEY_VALUE, "10");
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
        String datString = getProp(Constantes.KEY_DATE_INIT);
        assert datString != null;
        LocalDate dateInit = LocalDate.parse(datString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        TXT_DATE_INIT.setValue(dateInit);
        CKB_CODE_BARRE.setSelected(Boolean.parseBoolean(getProp(Constantes.KEY_USE_CODE_BARRE)));
        CKB_PRINT.setSelected(Boolean.parseBoolean(getProp(Constantes.KEY_USE_PRINTER)));
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
            CB_MODE_R.setValue(new YvsBaseModeReglement(Long.valueOf(Objects.requireNonNull(moder))));
        }

        TXT_PATH.setText(getProp(Constantes.KEY_PATH));
        selectNbColCatalogueProperty(getProp(Constantes.KEY_COL_CATALOGUE));
        selectNbLineCatalogueProperty(getProp(Constantes.KEY_LINE_CATALOGUE));

    }

    private void selectNbColCatalogueProperty(String nbCatalogueColumn) {
        Optional.ofNullable(nbCatalogueColumn).filter(StringUtils::isNotEmpty).ifPresent(value -> {
            switch (value) {
                case "2" -> NB_COL_CATALOGUE.selectToggle(CATALOGUE_DISPLAY_2_COL);
                case "3" -> NB_COL_CATALOGUE.selectToggle(CATALOGUE_DISPLAY_3_COL);
                default -> {
                }
            }
        });
    }

    private void selectNbLineCatalogueProperty(String nbCatalogueLine) {
        Optional.ofNullable(nbCatalogueLine).filter(StringUtils::isNotEmpty).ifPresent(value -> {
            switch (value) {
                case "5" -> NB_LINE_CATALOGUE.selectToggle(CATALOGUE_DISPLAY_5_LINE);
                case "6" -> NB_LINE_CATALOGUE.selectToggle(CATALOGUE_DISPLAY_6_LINE);
                case "10" -> NB_LINE_CATALOGUE.selectToggle(CATALOGUE_DISPLAY_10_LINE);
                default -> {
                    break;
                }
            }
        });
    }

    public boolean copyToSave() {
        File file = Helpers.getPropertiesFile(PROPERTIE_FILE_NAME);
        if (file != null && file.exists()) {
            PropertiesManager.getInstance().setProperty(Constantes.KEY_APPS_PORT, getVal(TXT_PORT_APP.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_ENVIRONNEMENT, getVal(CB_ENVIRONNEMENT.getValue()));
            verifyAndSaveComboBoxValue(CB_CLT, Constantes.KEY_CLIENT_DIVERS);
            verifyAndSaveComboBoxValue(CB_AGENCE_L, Constantes.KEY_LOCAL_AGENCE);
            verifyAndSaveComboBoxValue(CB_SOCIETE_L, Constantes.KEY_LOCAL_SOCIETE);
            verifyAndSaveComboBoxValue(CB_MDR, Constantes.KEY_MODEL_REGLEMENT);
            verifyAndSaveComboBoxValue(CB_MODE_R, Constantes.KEY_MODE_REGLEMENT);
            PropertiesManager.getInstance().setProperty(Constantes.KEY_LOCAL_DB_NAME, getVal(TXT_BD_NAME_L.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_LOCAL_HOST, getVal(TXT_IP_L.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_LOCAL_PASSWORD, EncryptMessage.encrypt(getVal(TXT_PASSWORD_L.getText()), Constantes.KEY_ENCRYPT));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_LOCAL_PORT, getVal(TXT_PORT_L.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_LOCAL_USERS, EncryptMessage.encrypt(getVal(TXT_USER_L.getText()), Constantes.KEY_ENCRYPT));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_MODE, getVal(CB_MODE.getValue()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_ORIENTATION_PRINT, "");
            PropertiesManager.getInstance().setProperty(Constantes.KEY_PAPER_HEIGHT, getVal(TXT_PAPER_H.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_PAPER_WIDTH, getVal(TXT_PAPER_L.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_PAPER_M_BOTOM, getVal(TXT_M_B.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_PAPER_M_LEFT, getVal(TXT_M_G.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_PAPER_M_RIGHT, getVal(TXT_M_D.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_PAPER_M_TOP, getVal(TXT_M_H.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_PATH, getVal(TXT_PATH.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_REMOTE_DB_NAME, getVal(TXT_BD_NAME_R.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_REMOTE_HOST, getVal(TXT_IP_R.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_REMOTE_PASSWORD, EncryptMessage.encrypt(getVal(TXT_PASSWORD_R.getText()), Constantes.KEY_ENCRYPT));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_REMOTE_PORT, getVal(TXT_PORT_R.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_REMOTE_SOCIETE, getVal(TXT_SOCIETE_R.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_REMOTE_USERS, EncryptMessage.encrypt(getVal(TXT_USER_R.getText()), Constantes.KEY_ENCRYPT));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_SECTEUR, "");
            PropertiesManager.getInstance().setProperty(Constantes.KEY_TYPE_PRINT, getVal(CB_TYPE_PRINT.getValue()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_USE_CODE_BARRE, "" + CKB_CODE_BARRE.isSelected());
            PropertiesManager.getInstance().setProperty(Constantes.KEY_USE_PRINTER, CKB_PRINT.isSelected() + "");
            PropertiesManager.getInstance().setProperty(Constantes.KEY_VILLE, "");
            PropertiesManager.getInstance().setProperty(Constantes.KEY_WEB_HOST, getVal(TXT_IP_WEB.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_WEB_PORT, getVal(TXT_PORT_WEB.getText()));
            PropertiesManager.getInstance().setProperty(Constantes.KEY_DATE_INIT, getDate());
            RadioButton radioButtonCol = (RadioButton) NB_COL_CATALOGUE.getSelectedToggle();
            RadioButton radioButtonLine = (RadioButton) NB_LINE_CATALOGUE.getSelectedToggle();
            PropertiesManager.getInstance().setProperty(Constantes.KEY_COL_CATALOGUE, radioButtonCol != null ? (String) radioButtonCol.getProperties().get("value") : "");
            PropertiesManager.getInstance().setProperty(Constantes.KEY_LINE_CATALOGUE, radioButtonLine != null ? (String) radioButtonLine.getProperties().get("value") : "");
            PropertiesManager.getInstance().storeValues(file);
        }
        return false;
    }

    private <T extends YvsEntity> void verifyAndSaveComboBoxValue(ComboBox<T> component, String keyProperty) {
        Optional.ofNullable(component.getValue())
                .map(YvsEntity::getId)
                .filter(Constantes::asLong)
                .map(Object::toString)
                .ifPresent(idAgence -> PropertiesManager.getInstance().setProperty(keyProperty, idAgence));
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
        CB_MDR.setConverter(new StringConverter<>() {

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
        List<YvsBaseModeReglement> modesRemote = dao.loadByNamedQuery("YvsBaseModeReglement.findAll", new String[]{}, new Object[]{});
        CB_MODE_R.setItems(FXCollections.observableArrayList(modesRemote));
        CB_MODE_R.setConverter(new StringConverter<>() {

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
            CB_CLT.setConverter(new StringConverter<>() {

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
        CB_AGENCE_L.setConverter(new StringConverter<>() {

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
        societes.add(0, null);
        CB_SOCIETE_L.setItems(FXCollections.observableArrayList(societes));
        CB_SOCIETE_L.setConverter(new StringConverter<>() {
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
            String re = PropertiesManager.getInstance().getVal(key);
            return (Constantes.asString(re) ? re : null);
        } catch (Exception ex) {
            LOGGER.error("Récupération de la date erronée !", ex);
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
        }
    }

    @Override
    public void freeMemoryController() {
        // comming soon
    }

}
