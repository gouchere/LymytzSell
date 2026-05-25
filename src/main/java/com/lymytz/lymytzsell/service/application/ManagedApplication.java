/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application;

import com.lymytz.lymytzsell.persistence.dao.Options;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseArticles;
import com.lymytz.lymytzsell.persistence.entity.YvsComCreneauHoraireUsers;
import com.lymytz.lymytzsell.persistence.entity.YvsComEnteteDocVente;
import com.lymytz.lymytzsell.persistence.dao.LocalQueryFactories;
import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import com.lymytz.lymytzsell.service.application.listener.ListenServersRemote;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.LymytzService;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.synchro.ws.AccountingHttpService;
import com.lymytz.lymytzsell.view.LocalLoader;
import com.lymytz.lymytzsell.view.controller.HomeCaisseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author LENOVO les fonctionnalités metiers partagé de l'application
 */
public class ManagedApplication {

    public LocalQueryFactories dao = new LocalQueryFactories();
    @Getter
    @Setter
    private HomeCaisseController mainPage;
    public ListenServersRemote serviceListen;
    /*    @Setter
        private List<Long> idDepots;*/
    List<String> categories;

    public ManagedApplication() {
        categories = new ArrayList<>();
        categories.add(Constantes.CAT_MP);
        categories.add(Constantes.CAT_PSF);
    }

    public YvsComEnteteDocVente createNewFicheFromCreneaux(long idCreno, Date date) {
        //Vérifie si le vendeur a déjà ouvert une fiche ce jour
        //récupère les paramètres commerciale
        YvsComCreneauHoraireUsers creno = dao.findOneByNQ("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{idCreno});
        date = (date == null && creno != null) ? creno.getDateTravail() : (date == null) ? new Date() : date;
        if (!UtilsProject.verifyDateVente(date)) {
            return null;
        }
        if (creno != null) {
            YvsComEnteteDocVente header = dao.findOneByNQ("YvsComEnteteDocVente.findOneFiche", new String[]{"date", "creno"}, new Object[]{date, creno});
            if (header != null) {
                //vérifie que la fiche ne soit pas déjà clôturé
                if (Boolean.TRUE.equals(header.getCloturer()) || header.getEtat().equals(Constantes.ETAT_CLOTURE)) {
                    LymytzService.openAlertDialog("La fiche de ce planning est déjà clôturé", "information", "Fiche non editable", Alert.AlertType.WARNING);
                    UtilsProject.headerDoc = null;
                    return null;
                } else {
                    LymytzService.openAlertDialog("Vous avez déjà une fiche pour cette journée", "information", "La fiche existe", Alert.AlertType.INFORMATION);
                    UtilsProject.headerDoc = header;
                    LymytzService.success();
                }
            } else {
                header = createHeader(creno, ((date != null) ? date : creno.getDateTravail()));
                UtilsProject.headerDoc = header;
                LymytzService.success();

                //clôture toutes les fiches non encore clôturé de ce vendeur
                String query = "UPDATE yvs_com_entete_doc_vente e SET cloturer=true, cloturer_by=?, date_cloturer=?, date_update=? "
                        + "FROM yvs_com_creneau_horaire_users c "
                        + "WHERE (e.creneau=c.id AND c.users=? AND e.cloturer=false ) AND e.id!=?";
                dao.executeSqlQuery(query, new Options[]{new Options(creno.getUsers().getId(), 1),
                        new Options(new Date(), 2),
                        new Options(new Date(), 3),
                        new Options(creno.getUsers().getId(), 4),
                        new Options(header.getId(), 5)
                });
            }
            return header;
        } else {
            LymytzService.openAlertDialog("Aucun créneau actif n'a été trouvé !", "Objet non trouvé", "Erreur", Alert.AlertType.ERROR);
        }
        return null;
    }

    private YvsComEnteteDocVente createHeader(YvsComCreneauHoraireUsers creno, Date date) {
        YvsComEnteteDocVente head = new YvsComEnteteDocVente();
        head.setAgence(UtilsProject.currentAgence);
        head.setAuthor(UtilsProject.currentUser);
        head.setCloturer(false);
        head.setCreneau(creno);
        head.setDateEntete(date);
        head.setDateSave(new Date());
        head.setDateUpdate(new Date());
        head.setEtat(Constantes.ETAT_EDITABLE);
        head.setStatutLivre(Constantes.ETAT_ATTENTE);
        head.setStatutRegle(Constantes.ETAT_ATTENTE);
        head = (YvsComEnteteDocVente) dao.save1(head);
        return head;
    }

    final Tooltip info_quit = new Tooltip("Quitter");
    final Tooltip info_compte = new Tooltip("Voir mon espace");
    final Tooltip info_param = new Tooltip("Paramétrer");

    public Pagination pagination;

    @FXML
    private BorderPane ROOT_PAN;
    @FXML
    private VBox ZONE_IMG;
    @FXML
    private VBox PAN_ART;
    @FXML
    public Button BTN_CMDE;
    @FXML
    private Button BTN_LIST;
    @FXML
    public Button BTN_CATALOGUE;
    @FXML
    private Button BTN_TRASH;
    @FXML
    private Button BTN_COMPTE;
    @FXML
    private Button BTN_LOG_OOUT;
    @FXML
    private AnchorPane PANE;
    @FXML
    private TextField TEXT_FIND;
    @FXML
    public Label LABEL_NB_TRANSFERT;
    @FXML
    public Label LAB_RESULT;

    @FXML
    private Label LAB_VOL;
    @FXML
    private Label LAB_POID;
    //    @FXML
//    private VBox PAN_COND;
    @FXML
    private Button BTN_PARAM;

    @FXML
    private Label LAB_TOTAL;
    @FXML
    private Label LAB_T_HT;

    @FXML
    private Label L_CURRENT;

    @FXML
    public VBox PAN_STOCK;
    @FXML
    public ImageView IMG_WIFI;
    @FXML
    private TextArea ART_DESCRIPTION;

    @FXML
    private Label SESS_SOCIETE;
    @FXML
    private Label SESS_DUREE;

    @FXML
    private void openViewParam(ActionEvent event) {
        openViewParam(true);

    }

    public void openViewParam(boolean establish) {
        try {
            FXMLLoader loader = new FXMLLoader(LocalLoader.class.getResource("/start/form_parametres.fxml"));
            VBox root = loader.load();
            Scene scene = new Scene(root, 500, 280);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initOwner(UtilsProject.primaryStage);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openListContent(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("/main/report/form_listing.fxml"));
            VBox root = load.load();
            Scene scene = new Scene(root, 1000, 565);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lymytz:extended caisse apps");
            stage.centerOnScreen();
            stage.setIconified(false);
            stage.initOwner(UtilsProject.primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void createNewFacture(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("/main/form_create_facture.fxml"));
            VBox root = load.load();
            Scene scene = new Scene(root, 500, 320);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lymytz:extended caisse apps");
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(UtilsProject.primaryStage);
            stage.show();
            load.getController();
            scene.setOnKeyReleased(event1 -> {
                if (event1.getCode().equals(KeyCode.ESCAPE)) {
                    stage.close();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openViewFacture(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader(LocalLoader.class.getResource("/main/form_factures.fxml"));
            AnchorPane root = load.load();
            Scene scene = new Scene(root, 955, 580);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lymytz:Factures");
            stage.centerOnScreen();
            stage.initOwner(UtilsProject.primaryStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ManagedApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void comptabilise(Long id) {
        String squery = "SELECT y.comptabilisation_auto FROM yvs_com_parametre_vente y WHERE y.agence=?";
        Boolean be = (Boolean) dao.findOneObjectBySQLQ(squery, new Options[]{new Options(UtilsProject.currentAgence.getId(), 1)});
        if (Boolean.TRUE.equals(be)) {
            new AccountingHttpService().comptabilise(id, UtilsProject.currentUser.getId());
        }
    }

    private Image[] images;

    public void createImageProduit(YvsBaseArticles art) {
        int n = art.getPhotos().size();
        pagination = new Pagination((n <= 0) ? 1 : n);
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        images = new Image[art.getPhotos().size()];
        int idx = 0;
        for (String str : art.getPhotos()) {
            images[idx] = displayPhotos(str);
            idx++;
        }
        pagination.setPageFactory(this::displayPagesArticles);
    }

    private VBox displayPagesArticles(int pageIndex) {
        VBox box = new VBox();
        if (images.length > pageIndex) {
            ImageView iv = new ImageView(images[pageIndex]);
            iv.setFitWidth(200);
            iv.setFitHeight(200);
            iv.setPreserveRatio(true);
            box.setAlignment(Pos.CENTER);
            Label desc = new Label("PAGE " + (pageIndex + 1));
            box.getChildren().addAll(iv, desc);
        }
        return box;
    }

    private Image displayPhotos(String photo) {
        try {
            String path = PropertiesManager.getInstance().getVal(Constantes.KEY_PATH);
            if (Constantes.asString(path)) {
                File f = new File(path + '\\' + photo);
                if (f.exists()) {
                    return new Image(new FileInputStream(f));
                } else {
                    return new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/coffee.png")));
                }
            } else {
                return new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/coffee.png")));
            }
        } catch (FileNotFoundException ex) {
            return new Image(Objects.requireNonNull(LocalLoader.class.getResourceAsStream("/icones/coffee.png")));
        }
    }
}
