/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application;

import com.lymytz.lymytzsell.dao.entity.YvsComClient;
import com.lymytz.lymytzsell.dao.entity.YvsComCreneauHoraireUsers;
import com.lymytz.lymytzsell.dao.entity.YvsComEnteteDocVente;
import com.lymytz.lymytzsell.dao.entity.YvsDictionnaire;
import com.lymytz.lymytzsell.dao.query.LocalQueryFactories;
import com.lymytz.lymytzsell.service.application.service.ServiceCreateFacture;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.view.component.DateTimePickerController;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
import impl.org.controlsfx.skin.AutoCompletePopup;
import impl.org.controlsfx.skin.AutoCompletePopupSkin;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class FactureController implements Initializable, Controller {

    LocalQueryFactories dao = new LocalQueryFactories();
    HomeCaisseController mainController;
    ToggleGroup TYPE_DOC = new ToggleGroup();
    YvsComClient selectClient;

    @FXML
    private RadioButton RB_FACTURE;
    @FXML
    private RadioButton RB_CMDE;
    @FXML
    private ComboBox<YvsDictionnaire> CB_VILLE;
    @FXML
    private ComboBox<YvsDictionnaire> CB_SECTEUR;

    private ObservableList dataClients;
    private ObservableList dataVilles;
    private ObservableList dataSecteurs;
    @FXML
    private TextField TF_CLIENT;

    @FXML
    private TextField TXT_NAME_CLIENT;
    @FXML
    private TextField TXT_TEL;
    @FXML
    private HBox DATE_CONTAINER;

    private DateTimePickerController datePickerController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectClient = null;
        RB_FACTURE.setToggleGroup(TYPE_DOC);
        RB_FACTURE.setSelected(true);
        RB_CMDE.setToggleGroup(TYPE_DOC);
        datePickerController = (DateTimePickerController) DATE_CONTAINER.getProperties().get("controller");
        if (UtilsProject.headerDoc != null) {
            datePickerController.setDateTime(UtilsProject.headerDoc.getDateEntete().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        } else {
            datePickerController.setDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)));
        }
        TYPE_DOC.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
//            RadioButton r = (RadioButton) TYPE_DOC.getSelectedToggle();
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(UtilsProject.headerDoc.getDateEntete());
//                DT_DATE = new DateTimePicker(LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));
        });
        initVilles();
        AutoCompletePopup<YvsComClient> popUpClients = TextFields.bindAutoCompletion(TF_CLIENT, c -> {
            return UtilsProject.listClients.stream().filter(elt -> {
                return (elt.getNom_prenom().toLowerCase().contains(c.getUserText().toLowerCase())
                        || elt.getCodeClient().toLowerCase().contains(c.getUserText().toLowerCase()));
            }).collect(Collectors.toList());
        }, new StringConverter<YvsComClient>() {

            @Override
            public String toString(YvsComClient object) {
                if (object != null) {
                    return object.getTextClient();
                }
                return "";
            }

            @Override
            public YvsComClient fromString(String string) {
                return null;
            }
        }).getAutoCompletionPopup();
        popUpClients.setSkin(new AutoCompletePopupSkin<>(popUpClients, param -> new ListCell<YvsComClient>() {
            @Override
            public void updateItem(YvsComClient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(new Rectangle(32, 32, Color.BEIGE));
                    setText(item.getTextClient());
                }
            }
        }));
        TF_CLIENT.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            getClientWithText();
        });
        TF_CLIENT.setPromptText(TF_CLIENT.getText());
    }

    public void initDataForm(HomeCaisseController main) {
        this.mainController = main;
        initClients();
    }

    private void initClients() {
        if (UtilsProject.listClients != null ? !UtilsProject.listClients.isEmpty() : false) {
//            Récupère le client par défaut
            if (UtilsProject.listClients != null) {
                if (UtilsProject.clientDivers != null) {
                    TF_CLIENT.setText(UtilsProject.clientDivers.getTextClient());
                    getClientWithText();
                } else {
                    if (UtilsProject.listClients.size() > 0) {
                        TF_CLIENT.setText(UtilsProject.listClients.get(0).getTextClient());
                        getClientWithText();
                    }
                }
            }
        } else {
            LymytzService.openAlertDialog("Impossible de trouver la liste des clients", "Liste clients non chargé", "Aucun client trouvé!", Alert.AlertType.ERROR);
        }
    }

    private void initVilles() {
        dataVilles = FXCollections.observableArrayList(UtilsProject.villes);
        if (UtilsProject.defaultAdresse != null) {
            CB_VILLE.setValue(UtilsProject.defaultAdresse.getParent());
            CB_SECTEUR.setValue(UtilsProject.defaultAdresse);
        }
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
        CB_VILLE.setItems(dataVilles);
    }

    @FXML
    private void listenVille(ActionEvent event) {
        List<YvsDictionnaire> villes = dao.loadByNamedQuery("YvsDictionnaire.findAllByParent", new String[]{"parent"}, new Object[]{CB_VILLE.getValue()});
        dataSecteurs = FXCollections.observableArrayList(villes);
        CB_SECTEUR.setItems(dataSecteurs);

    }

    @FXML
    private void refreshTime(ActionEvent event) {
        datePickerController.setDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(12, 0)));
    }

    @FXML
    private void createNewFacture(ActionEvent event) {
        if (selectClient == null && TF_CLIENT.getText() != null) {
            getClientWithText();
        }
        RadioButton type = (RadioButton) TYPE_DOC.getSelectedToggle();
        String ty = (type.getText().equals("Commande")) ? Constantes.TYPE_BCV : Constantes.TYPE_FV;
        LocalDateTime local = datePickerController.getDateTime();
        Date date = new Date();
        if (local != null) {
            date = Date.from(local.atZone(ZoneId.systemDefault()).toInstant());
        }
        createFactureVente(selectClient, ty, (CB_SECTEUR.getValue() != null) ? CB_SECTEUR.getValue() : CB_VILLE.getValue(), TXT_NAME_CLIENT.getText(), date, TXT_TEL.getText());
    }

    private void listenSelectClient(YvsComClient clt) {
        if (clt != null) {
            TXT_NAME_CLIENT.setText(clt.getNom_prenom());
            if (clt.getTiers() != null) {
                if (clt.getTiers().getTel() != null) {
                    TXT_TEL.setText(clt.getTiers().getTel());
                } else {
                    TXT_TEL.setText(clt.getTiers().getTel());
                }
                CB_VILLE.setValue(clt.getTiers().getVille());
                if (clt.getTiers() != null ? clt.getTiers().getSecteur() != null : false) {
                    CB_SECTEUR.setValue(clt.getTiers().getSecteur());
                } else {
                    CB_SECTEUR.setValue(UtilsProject.defaultAdresse);
                }
            }
        }
    }

    public synchronized Long createFactureVente(YvsComClient client, String typeDoc, YvsDictionnaire adresse, String nameClient, Date dateLiv, String telephone) {
        ServiceCreateFacture f = new ServiceCreateFacture(client, typeDoc, adresse, nameClient, dateLiv, telephone, mainController);
        Thread t = new Thread(f);
        t.start();
        return f.getResult();
    }

    public YvsComEnteteDocVente createNewFicheFromCreneaux(long idCreno, Date date) {
        //Vérifie si le vendeur a déjà ouvert une fiche ce jour
        //récupère les paramètres commerciale
        YvsComCreneauHoraireUsers creno = (YvsComCreneauHoraireUsers) dao.findOneByNQ("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{idCreno});
        date = (date == null && creno != null) ? creno.getDateTravail() : new Date();
        if (!UtilsProject.verifyDateVente(date)) {
            return null;
        }
        if (creno != null) {
            YvsComEnteteDocVente header = (YvsComEnteteDocVente) dao.findOneByNQ("YvsComEnteteDocVente.findOneFiche", new String[]{"date", "creno"}, new Object[]{date, creno});
            if (header != null) {
                //vérifie que la fiche ne soit pas déjà clôturé
                if (header.getCloturer() || header.getEtat().equals(Constantes.ETAT_CLOTURE)) {
                    LymytzService.openAlertDialog("La fiche de ce planning est déjà clôturé", "Fiche non editable", "nformation", Alert.AlertType.WARNING);
                    UtilsProject.headerDoc = null;
                } else {
                    UtilsProject.headerDoc = header;
                }
            } else {
                header = createHeder(creno, ((date != null) ? date : creno.getDateTravail()));
                UtilsProject.headerDoc = header;
            }
//            displayPropertiesFiche(header);
            return header;
        } else {
            LymytzService.openAlertDialog("Aucun créno actif n'a été trouvé !", "Objet non trouvé", "Erreur", Alert.AlertType.ERROR);
        }
        return null;
    }

    private YvsComEnteteDocVente createHeder(YvsComCreneauHoraireUsers creno, Date date) {
        YvsComEnteteDocVente head = new YvsComEnteteDocVente();
        head.setAgence(UtilsProject.currentAgence);
        head.setAuthor(UtilsProject.currentUser);
        head.setCloturer(false);
        head.setCreneau(creno);
        head.setDateEntete(date);
        head.setDateSave(new Date());
        head.setDateUpdate(new Date());
        head.setEtat(Constantes.ETAT_EDITABLE);
        head = (YvsComEnteteDocVente) dao.save1(head);
        return head;
    }

    @Override
    public void freeMemoryController() {
       /* if (dataClients != null) {
            dataClients.clear();
        }
        if (dataSecteurs != null) {
            dataSecteurs.clear();
        }
        if (dataVilles != null) {
            dataVilles.clear();
        }*/
    }

    public void getClientWithText() {
        String[] values = TF_CLIENT.getText().split("->");
        if (values.length > 1) {
            UtilsProject.listClients.stream().filter((c) -> (c.getCodeClient().equals(values[1]))).map((c) -> {
                selectClient = c;
                return c;
            }).forEach((c) -> {
                TXT_NAME_CLIENT.setText(c.getNom_prenom());
                listenSelectClient(c);
            });
        }
    }
}
