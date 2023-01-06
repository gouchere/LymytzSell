/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.view.main.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import lymytz.dao.ConnexionBD;
import lymytz.dao.LocalDao;
import lymytz.dao.LocalSqlDao;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.service.application.ManagedApplication;
import lymytz.service.application.composant.Onglets;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class PrintFacture extends JFrame {

    ManagedApplication page;
    Onglets onglet;

    public PrintFacture(ManagedApplication page, Onglets ong) {
        this.page = page;
        this.onglet = ong;
    }

    /**
     * Initializes the controller class.
     * @param doc
     */
    public void loadFactureToPrint(YvsComDocVentes doc) {
        String chemin = "src/lymytz/view/reports/icones/";
        String cheminr = "src/lymytz/view/reports/";
        File file = new File("src/lymytz/view/reports/facture_vente.jasper");
        JasperPrint j = new JasperPrint();
        Map<String, Object> params = new HashMap<>();
        params.put("ID", doc.getId().intValue());
        params.put("IMG_PAYE", chemin + "/" + ((doc.getStatutRegle().equals(Constantes.ETAT_REGLE) ? "solde.png" : "empty.png")));
        params.put("IMG_LIVRE", chemin + "/" + ((doc.getStatutLivre().equals(Constantes.ETAT_LIVRE) ? "livre.png" : "empty.png")));
        params.put("MONTANT", "");
        params.put("AUTEUR", UtilsProject.currentUser.getUsers().getNomUsers());
        params.put("TAXE", doc.getMontantTaxe());
        params.put("SUBREPORT_DIR", cheminr);
        params.put("LOGO", "");
        try {
            try {
                j = JasperFillManager.fillReport(new FileInputStream(file), params, LocalSqlDao.getInstance().getConnection());
                JRViewer viewer = new JRViewer(j);
                viewer.setOpaque(true); 
                viewer.setVisible(true);
                this.add(viewer);
                this.setSize(1024, 800);
                this.setVisible(true);                
            } catch (JRException ex) { 
                Logger.getLogger(PrintFacture.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PrintFacture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
