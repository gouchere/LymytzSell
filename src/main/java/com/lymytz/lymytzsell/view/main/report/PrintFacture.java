/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.view.main.report;

import com.lymytz.lymytzsell.dao.LocalSqlDao;
import com.lymytz.lymytzsell.dao.entity.YvsComDocVentes;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class PrintFacture extends JFrame {
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
