    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.start;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;
import lymytz.dao.LocalDao;
import lymytz.dao.ParamConnection;
import lymytz.dao.RemoteDao;
import lymytz.dao.entity.YvsAgences;
import lymytz.dao.entity.YvsBaseModeReglement;
import lymytz.dao.entity.YvsBaseModelReglement;
import lymytz.dao.entity.YvsComClient;
import lymytz.dao.entity.YvsDictionnaire;
import lymytz.dao.entity.YvsSocietes;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.Controller;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.LymytzService;
import lymytz.service.utils.UtilsProject;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class ParametresController implements Initializable, Controller {

    LQueryFactories dao = new LQueryFactories();

    @FXML
    private TextField TXT_ID_R;
    @FXML
    private Label TXT_RESULT;
    @FXML
    private TextField TXT_HOTE;
    @FXML
    private TextField TXT_HOTE_R;
    @FXML
    private TextField TXT_PORT;
    @FXML
    private TextField TXT_PORT_R;
    @FXML
    private TextField TXT_USER;
    @FXML
    private TextField TXT_USER_R;
    @FXML
    private TextField TXT_HOTE_W;
    @FXML
    private TextField TXT_PORT_W;
    @FXML
    private PasswordField TXT_PASS;
    @FXML
    private PasswordField TXT_PASS_R;
    @FXML
    private TextField TXT_DB;
    @FXML
    private TextField TXT_DB_R;
    @FXML
    private Tab TAB_GEN;
    @FXML
    private ComboBox<YvsSocietes> CB_SOCIETE;
    @FXML
    private ComboBox<YvsAgences> CB_AGENCE;
    @FXML
    private TextField TXT_NUM_CAISSE;
    @FXML
    private TextField TXT_ORIENTATION;
    @FXML
    private TextField TXT_LARGEUR;
    @FXML
    private TextField TXT_HAUTEUR;
    @FXML
    private TextField TXT_MARGE_D;
    @FXML
    private TextField TXT_MARGE_TOP;
    @FXML
    private TextField TXT_MARGE_G;
    @FXML
    private TextField TXT_PATH;
    @FXML
    private Button CHOOSE_DIR;
    @FXML
    private CheckBox CHB_PRINT;
    @FXML
    private ComboBox<YvsComClient> CB_CLIENT;
    @FXML
    private ComboBox<YvsDictionnaire> CB_VILLE;
    @FXML
    private ComboBox<YvsDictionnaire> CB_SECTEUR;

    @FXML
    private RadioButton RAD_TICKET;
    @FXML
    private RadioButton RAD_A5;
    @FXML
    private RadioButton RAD_A4;

    @FXML
    private ToggleGroup TYPE_PRINT;

    private ObservableList dataClients;
    private ObservableList dataVilles;
    private ObservableList dataSecteurs;
    private ObservableList dataModesReg, dataModelsReg;
    @FXML
    private ComboBox<YvsBaseModeReglement> CB_MODE_REG;
    @FXML
    private ComboBox<YvsBaseModelReglement> CB_MODEL_REG;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        TYPE_PRINT = RAD_A4.getToggleGroup();
        CB_AGENCE.setConverter(new StringConverter<YvsAgences>() {

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
        CB_SECTEUR.setConverter(new StringConverter<YvsDictionnaire>() {

            @Override
            public String toString(YvsDictionnaire object) {
                if (object != null) {
                    return object.getLibele();
                }
                return "";
            }

            @Override
            public YvsDictionnaire fromString(String string) {
                return null;
            }
        });
        ParamConnection p = ParamConnection.readFile(LymytzService.getFileInputStream());
        if (p != null ? p.getIdRemoteScte() > 0 : false) {
            init();
        }
        displayParametres(false);
    }

    @FXML
    private void testConnect(ActionEvent event) {
        try {
//            ConnexionBD.HOST = TXT_HOTE.getText();
//            ConnexionBD.PORT = TXT_PORT.getText();
//            ConnexionBD.USER = TXT_USER.getText();
//            ConnexionBD.PASSWORD = TXT_PASS.getText();
//            ConnexionBD.DATABASE = TXT_DB.getText();
            TXT_RESULT.setText("Connected");
            TXT_RESULT.setStyle("-fx-text-fill: blue");
            TAB_GEN.setDisable(false);
            init();
        } catch (Exception ex) {
            TXT_RESULT.setText("Connection failed ");
            TXT_RESULT.setStyle("-fx-text-fill: red");

        }
    }

    private void init() {
        List<YvsSocietes> societes = dao.loadByNamedQuery("YvsSocietes.findAll", new String[]{}, new Object[]{});
        ObservableList lSocietes = FXCollections.observableArrayList(societes);
        CB_SOCIETE.setItems(lSocietes);
        CB_SOCIETE.setConverter(new StringConverter<YvsSocietes>() {

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
        initClients();
        initVilles();
        initModePaiement();
    }

    @FXML
    private void choixSociete(ActionEvent event) {
        YvsSocietes val = CB_SOCIETE.getValue();
        if (val != null) {
            UtilsProject.currentSociete=val;
            List<YvsAgences> agences = dao.loadByNamedQuery("YvsAgences.findBySociete", new String[]{"societe"}, new Object[]{val});
            ObservableList lagences = FXCollections.observableArrayList(agences);
            CB_AGENCE.setItems(lagences);
            initClients();
        initVilles();
        initModePaiement();
        }
    }

    @FXML
    private void saveParametres(ActionEvent event) {
        //contrôle les infos De societes
        if (TXT_NUM_CAISSE.getText() != null) {
//            if (CB_SOCIETE.getValue() != null) {
//                if (CB_AGENCE.getValue() != null) {
            ParamConnection param = new ParamConnection();
            param.setIdRemoteScte(Constantes.asString(TXT_ID_R.getText()) ? Long.valueOf(TXT_ID_R.getText()) : 0L);
            param.setCheminPhotos(TXT_PATH.getText());
            param.setCodeCaisse(TXT_NUM_CAISSE.getText());
            param.setCodeSociete((CB_SOCIETE.getValue() != null) ? CB_SOCIETE.getValue().getId() : 0);
            param.setCodeAgence((CB_AGENCE.getValue() != null) ? CB_AGENCE.getValue().getId() : 0);
            param.setHostWeb(TXT_HOTE_W.getText());
            param.setPortWeb(TXT_PORT_W.getText());
            param.setOrientation(TXT_ORIENTATION.getText());
            param.setP_default(CHB_PRINT.isSelected());
            param.setP_height(getDoubleValue(TXT_HAUTEUR.getText()));
            param.setP_mb(getDoubleValue(TXT_MARGE_TOP.getText()));
            param.setP_ml(getDoubleValue(TXT_MARGE_G.getText()));
            param.setP_mr(getDoubleValue(TXT_MARGE_D.getText()));
            param.setP_mt(getDoubleValue(TXT_MARGE_TOP.getText()));
            param.setP_width(getDoubleValue(TXT_LARGEUR.getText()));
            param.setDataBase(TXT_DB.getText());
            param.setPassword(TXT_PASS.getText());
            param.setPort(TXT_PORT.getText());
            param.setSever(TXT_HOTE.getText());
            param.setUsers(TXT_USER.getText());
            param.setP_width(getDoubleValue(TXT_LARGEUR.getText()));
            param.setDataBaseRemote(TXT_DB_R.getText());
            param.setPasswordRemote(TXT_PASS_R.getText());
            param.setPortRemote(TXT_PORT_R.getText());
            param.setSeverRemote(TXT_HOTE_R.getText());
            param.setUsersRemote(TXT_USER_R.getText());
            param.setUseCodeBarre(true);
            param.setUsePrinter(CHB_PRINT.isSelected());
            param.setVille((CB_VILLE.getValue() != null) ? CB_VILLE.getValue().getId() : 0);
            param.setSecteur((CB_SECTEUR.getValue() != null) ? CB_SECTEUR.getValue().getId() : 0);
            param.setClientDivers((CB_CLIENT.getValue() != null) ? CB_CLIENT.getValue().getId() : 0);
            param.setModeReg((CB_MODE_REG.getValue() != null) ? CB_MODE_REG.getValue().getId() : 0);
            param.setModelReg((CB_MODEL_REG.getValue() != null) ? CB_MODEL_REG.getValue().getId() : 0);
            param.setTypeRapport(((RadioButton) TYPE_PRINT.getSelectedToggle()).getText());
            param.createFile(LymytzService.getFileoutputStream());
            LymytzService.openAlertDialog("", "Succes", "Succes", Alert.AlertType.INFORMATION);
            //relance la conneexion aux serveurs de données
            LocalDao.setInstance(null);
            RemoteDao.setInstance(null);
            //Initialise les valeurs par défaut
            if (param.getIdRemoteScte() > 0) {
                UtilsProject.RcurrentSociete = new YvsSocietes(param.getIdRemoteScte());
            }
//                } else {
//                    LymytzService.openAlertDialog("Vous devez choisir l'agence", "Error", "Erreur Paramétrage", Alert.AlertType.ERROR);
//                }
//            } else {
//                LymytzService.openAlertDialog("Vous devez choisir une société", "Error", "Erreur Paramétrage", Alert.AlertType.ERROR);
//            }
        } else {
            LymytzService.openAlertDialog("Vous devez entrer le code de la caisse", "Error", "Erreur Paramétrage", Alert.AlertType.ERROR);
        }
    }

    private double getDoubleValue(String number) {
        if (number != null ? !number.trim().isEmpty() : false) {
            return Double.valueOf(number);
        }
        return 0;
    }

    public void displayParametres(boolean establish) {
        ParamConnection param = ParamConnection.readFile(LymytzService.getFileInputStream());
        if (param != null) {
            if (establish) {
                initClients();
                initModePaiement();
            }
            if (param.getSecteur() > 0) {
                CB_SECTEUR.getItems().clear();
                CB_SECTEUR.getItems().add((YvsDictionnaire) dao.findOneByNQ("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{param.getSecteur()}));
            }
            if (param.getCodeAgence() > 0) {
                CB_AGENCE.getItems().clear();
                CB_AGENCE.getItems().add((YvsAgences) dao.findOneByNQ("YvsAgences.findById", new String[]{"id"}, new Object[]{param.getCodeAgence()}));
            }
            TXT_ID_R.setText(param.getIdRemoteScte() + "");
            TXT_PATH.setText(param.getCheminPhotos());
            TXT_NUM_CAISSE.setText(param.getCodeCaisse());
            CB_SOCIETE.setValue(new YvsSocietes(param.getCodeSociete()));
            CB_AGENCE.setValue(new YvsAgences(param.getCodeAgence()));
            TXT_HOTE_W.setText(param.getHostWeb());
            TXT_ORIENTATION.setText(param.getOrientation());
            CHB_PRINT.setSelected(param.getUsePrinter());
            TXT_HAUTEUR.setText(param.getP_height() + "");
            TXT_MARGE_TOP.setText(param.getP_mt() + "");
            TXT_MARGE_G.setText(param.getP_ml() + "");
            TXT_MARGE_D.setText(param.getP_mr() + "");
            TXT_LARGEUR.setText(param.getP_width() + "");
            TXT_DB.setText(param.getDataBase());
            TXT_DB_R.setText(param.getDataBaseRemote());
            TXT_PASS.setText(param.getPassword());
            TXT_PORT.setText(param.getPort());
            TXT_PASS_R.setText(param.getPasswordRemote());
            TXT_PORT_R.setText(param.getPortRemote());
            TXT_PORT_W.setText(param.getPortWeb());
            TXT_HOTE.setText(param.getSever());
            TXT_USER.setText(param.getUsers());
            TXT_HOTE_R.setText(param.getSeverRemote());
            TXT_USER_R.setText(param.getUsersRemote());
            CHB_PRINT.setSelected(param.getUsePrinter());
            CB_VILLE.setValue(new YvsDictionnaire(param.getVille()));
            CB_SECTEUR.setValue(new YvsDictionnaire(param.getSecteur()));
            if (param.getSecteur() > 0) {
                CB_SECTEUR.getItems().add(new YvsDictionnaire(param.getSecteur()));
            }
            CB_CLIENT.setValue(new YvsComClient(param.getClientDivers()));
            CB_MODEL_REG.setValue(new YvsBaseModelReglement(param.getClientDivers()));
            CB_MODE_REG.setValue(new YvsBaseModeReglement(param.getModeReg()));
            if (param.getTypeRapport() != null) {
                if (param.getTypeRapport().equals(UtilsProject.TYPE_RAPPORT_A4)) {
                    TYPE_PRINT.selectToggle(RAD_A4);
                } else if (param.getTypeRapport().equals(UtilsProject.TYPE_RAPPORT_A5)) {
                    TYPE_PRINT.selectToggle(RAD_A4);
                } else {
                    TYPE_PRINT.selectToggle(RAD_TICKET);
                }
            } else {
                TYPE_PRINT.selectToggle(RAD_TICKET);
            }
        } else {
            TXT_ID_R.setText(UtilsProject.properties.getProperty(Constantes.KEY_REMOTE_SOCIETE));
        }
    }

    private void initClients() {
        List<YvsComClient> clients = dao.loadByNamedQuery("YvsComClient.findAll", new String[]{"societe"}, new Object[]{UtilsProject.currentSociete});
        dataClients = FXCollections.observableArrayList(clients);
        CB_CLIENT.setItems(dataClients);
        CB_CLIENT.setConverter(new StringConverter<YvsComClient>() {

            @Override
            public String toString(YvsComClient object) {
                if (object != null) {
                    return object.getNom_prenom();
                }
                return "";
            }

            @Override
            public YvsComClient fromString(String string) {
                return null;
            }
        });
    }

    private void initVilles() {
        List<YvsDictionnaire> villes = dao.loadByNamedQuery("YvsDictionnaire.findVilles", new String[]{}, new Object[]{});
        dataVilles = FXCollections.observableArrayList(villes);
        CB_VILLE.setItems(dataVilles);
        CB_VILLE.setConverter(new StringConverter<YvsDictionnaire>() {

            @Override
            public String toString(YvsDictionnaire object) {
                if (object != null) {
                    return object.getLibele();
                }
                return "";
            }

            @Override
            public YvsDictionnaire fromString(String string) {
                return null;
            }
        });
    }

    private void initModePaiement() {
        List<YvsBaseModeReglement> villes = dao.loadByNamedQuery("YvsBaseModeReglement.findByActif", new String[]{"actif","societe"}, new Object[]{true, UtilsProject.currentSociete});
        dataModesReg = FXCollections.observableArrayList(villes);
        CB_MODE_REG.setItems(dataModesReg);
        CB_MODE_REG.setConverter(new StringConverter<YvsBaseModeReglement>() {

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
        List<YvsBaseModelReglement> models = dao.loadByNamedQuery("YvsBaseModelReglement.findByActif", new String[]{"actif", "societe"}, new Object[]{true,UtilsProject.currentSociete});
        dataModelsReg = FXCollections.observableArrayList(models);
        CB_MODEL_REG.setItems(dataModelsReg);
        CB_MODEL_REG.setConverter(new StringConverter<YvsBaseModelReglement>() {

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
    }

    @FXML
    private void listenValue(ActionEvent event) {
        List<YvsDictionnaire> villes = dao.loadByNamedQuery("YvsDictionnaire.findAllByParent", new String[]{"parent"}, new Object[]{CB_VILLE.getValue()});
        dataSecteurs = FXCollections.observableArrayList(villes);
        CB_SECTEUR.setItems(dataSecteurs);

    }

    @FXML
    private void openDirectorie(ActionEvent event) {
        final DirectoryChooser dlg = new DirectoryChooser();
        final File dir = dlg.showDialog(CHOOSE_DIR.getScene().getWindow());
        if (dir != null) {
            TXT_PATH.setText(dir.getPath());
        }
    }

    @Override
    public void freeMemoryController() {
    }

}
