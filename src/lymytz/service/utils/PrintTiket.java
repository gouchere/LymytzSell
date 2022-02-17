/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.utils;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import java.util.Date;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lymytz.dao.entity.YvsComContenuDocVente;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.service.application.composant.Onglets;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LENOVO
 */
public class PrintTiket implements Runnable {

    HomeCaisseController mainPage;
    PrinterJob print;
    private double avance;
    String message;
    private WebView view = new WebView();
    private WebEngine wbEngine;
    private Double netAPayer;
    private Double montantRecu;
    private Double montantAvance;
    private Double montantTotal;
    private YvsComDocVentes facture;

    public PrintTiket(HomeCaisseController main, Onglets onglet, double avance, String message) {
        this.mainPage = main;
        this.avance = avance;
        this.message = message;
        view = new WebView();
        view.prefWidth(300);
        view.prefHeight(5000);
        wbEngine = view.getEngine();
        view.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    public Double getNetAPayer() {
        return netAPayer;
    }

    public void setNetAPayer(Double netAPayer) {
        this.netAPayer = netAPayer;
    }

    public Double getMontantRecu() {
        return montantRecu;
    }

    public void setMontantRecu(Double montantRecu) {
        this.montantRecu = montantRecu;
    }

    public Double getMontantAvance() {
        return montantAvance;
    }

    public void setMontantAvance(Double montantAvance) {
        this.montantAvance = montantAvance;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public YvsComDocVentes getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocVentes facture) {
        this.facture = facture;
    }

    public double getAvance() {
        return avance;
    }

    public void setAvance(double avance) {
        this.avance = avance;
    }

    @Override
    public void run() {
        if (facture != null) {
            dataPrint(facture.getContenus());
        } else {
            LymytzService.openAlertDialog("Aucune facture n'a été trouvé !", "Erreur de validation", "Erreur Ticket", Alert.AlertType.ERROR);
        }

    }

    private void dataPrint(List<YvsComContenuDocVente> l) {
        print = PrinterJob.createPrinterJob();
        print.getJobSettings().setJobName("Lymytz_caisse_print_ticket");
        PageLayout lay = getCustumPage();
        if (lay != null) {
            Platform.runLater(() -> {
                TextFlow content = print(l);
                if (print.printPage(lay, content)) {
                    print.endJob();
                }
            });
        }
    }
    double tremise = 0d, tristourne = 0d, trabais = 0d;

    public TextFlow print(List<YvsComContenuDocVente> contents) {
//        StringBuilder footer = UtilsProject.FOOTER_TICKET(0d, 0d, tremise, tristourne, avance, netAPayer, montantRecu, montantTotal, montantAvance, facture.getTypeDoc());
//        wbEngine.loadContent((UtilsProject.HEADERTICKET(facture).append(getBuildText(facture.getContenus())).append(footer)).toString());
        return FOOTER_TICKET_(facture, 0d, 0d, tremise, tristourne, avance, netAPayer, montantRecu, montantTotal, montantAvance, facture.getTypeDoc());
//        wbEngine.loadContent((UtilsProject.HEADERTICKET(facture).append(getBuildText(facture.getContenus())).append(footer)).toString());
    }

    private PageLayout getCustumPage() {
        PageLayout pl;
        javafx.print.Paper paper = PrintHelper.createPaper("Perso", 300d, 15000d, Units.INCH);
        PageOrientation PO = (UtilsProject.paramConnection.getOrientation() != null) ? UtilsProject.paramConnection.getOrientation().equals("PAYSAGE") ? PageOrientation.LANDSCAPE : PageOrientation.PORTRAIT : PageOrientation.PORTRAIT;
//        pl = Printer.getDefaultPrinter().createPageLayout(Paper.A4, PO, UtilsProject.paramConnection.getP_ml(), UtilsProject.paramConnection.getP_mr(), UtilsProject.paramConnection.getP_mt(), UtilsProject.paramConnection.getP_mb());
        pl = Printer.getDefaultPrinter().createPageLayout(paper, PO, UtilsProject.paramConnection.getP_ml(), UtilsProject.paramConnection.getP_mr(), UtilsProject.paramConnection.getP_mt(), UtilsProject.paramConnection.getP_mb());
        return pl;
    }

    public TextFlow getBuildText_(List<YvsComContenuDocVente> contents) {
        //content
        TextFlow result = new TextFlow();
        Text headCol = getText("Qté \t Prix.U \t Total \t N.A.P \n", 9, true);
        result.getChildren().add(headCol);
        tremise = tristourne = 0;
        Text line, rabais, rem, rist;
        for (YvsComContenuDocVente c : contents) {
            result.getChildren().add(getText(c.getArticle().getDesignation() + " \n", 9, false));
            result.getChildren().add(getText(Constantes.nbf.format(c.getQuantite()) + " \t\t " + Constantes.nbf.format(c.getPrix()) + "\t\t "
                    + "" + Constantes.nbf.format(c.getPrix() * c.getQuantite()) + "\t\t " + Constantes.nbf.format((c.getPrix() - c.getRemise() - c.getRabais()) * c.getQuantite()) + "\n", 8, false));
            if (c.getRabais() > 0 || c.getRemise() > 0 || c.getRistourne() > 0) {
                if (c.getRabais() > 0) {
                    result.getChildren().add(getText("Rabais: \t\t\t" + Constantes.nbf.format(c.getRabais()) + " \n", 8, false));
                }
                if (c.getRemise() > 0) {
                    result.getChildren().add(getText("Remise: \t\t\t" + Constantes.nbf.format(c.getRemise()) + " \n", 8, false));
                }
                if (c.getRistourne() > 0) {
                    result.getChildren().add(getText("Risttourne: \t\t\t" + Constantes.nbf.format(c.getRistourne()) + " \n", 8, false));
                }
            }

            tremise = tremise + c.getRemise();
            tristourne = tristourne + c.getRistourne();
            trabais = trabais + c.getRabais();
        }
        return result;
    }
//
//    private String getOpenDiv() {
//        StringBuilder sb = new StringBuilder("<div style=")
//                .append("font-size:7pt").append(">");
//        return sb.toString();
//    }
//
//    private String getOpenDivfoot() {
//        StringBuilder sb = new StringBuilder("<div style=")
//                .append("font-size:5pt; white-space: initial;white-space: nowrap;").append(">");
//        return sb.toString();
//    }
//
//    private String getSpan(String text) {
//        String sb = "<span style=font-size:6pt; witdh:110px;display:inline-block; float:left>" + text + "</span>";
//        return sb;
//    }

    public TextFlow FOOTER_TICKET_(YvsComDocVentes facture, Double taxe, Double trabais, Double tremise, Double tristourne, double avance, Double netAPayer, Double montantRecu, Double montantTotal, Double montantAvance, String type) {
//header
        if (UtilsProject.currentSociete != null && UtilsProject.currentAgence != null && facture != null) {
            Text wel = getText("BIENVENUE AU " + UtilsProject.currentSociete.getName().toUpperCase() + " \n", 10, true);
            Text bp = getText("BP:       \t" + UtilsProject.currentSociete.getCodePostal() + " \n", 9, false);
            Text rc = getText("RC:       \t " + UtilsProject.currentSociete.getNumeroRegistreComerce() + " \n", 9, false);
            Text contr = getText("CONTR:    \t " + UtilsProject.currentSociete.getNumeroRegistreComerce() + " \n", 9, false);
            Text tel1 = getText("TEL:      \t " + UtilsProject.currentAgence.getTelephone() + " \n", 9, false);
            Text tel2 = getText("TEL SIEGE:\t " + UtilsProject.currentSociete.getTel() + " \n", 9, false);

            Text numDoc = getText("Num:        \t " + facture.getNumDoc() + "\n", 9, false);
            Text cltC = getText("Code client:\t " + facture.getClient().getCodeClient() + "\n", 9, false);
            Text cltN = getText("Nom:        \t " + facture.getNom_client() + "\n", 9, false);
            Text date = getText("Facture du: \t " + Constantes.dfD.format(UtilsProject.headerDoc.getDateEntete()) + "\n", 9, false);
            Text dateP = getText("Imprimé le: \t " + Constantes.dfh.format(new Date()) + "\n", 9, false);
            Text dateL = new Text("");
            Text telClt = new Text("");
            if (facture.getTypeDoc().equals(Constantes.TYPE_BCV)) {
                dateL = getText("Liv. le:    \t " + Constantes.dfh.format(facture.getDateLivraisonPrevu()) + "\n", 9, false);
                telClt = getText("Tel. client:\t " + facture.getTelephone() != null ? facture.getTelephone() : "" + "\n", 9, false);
            }
            Text vend = getText("Vendeur:    \t " + UtilsProject.currentUser.getUsers().getNomUsers() + "\n", 9, false);
            TextFlow tf = new TextFlow(wel, bp, rc, contr, tel1, tel2, numDoc, cltC, cltN, date, dateP, dateL, telClt,vend);
            //content
            Text headCol = getText("Quantité \t Prix.U \t P.Total \t N.A.P \n", 9, true);
            tf.getChildren().add(headCol);
            tremise = tristourne = trabais = 0d;
            Text line, rabais, rem, rist;
            for (YvsComContenuDocVente c : facture.getContenus()) {
                tf.getChildren().add(getText(c.getArticle().getDesignation() + " \n", 8, false));
                tf.getChildren().add(getText(" \t" + Constantes.nbf.format(c.getQuantite()) + "\t\t" + Constantes.nbf.format(c.getPrix()) + "\t\t"
                        + "" + Constantes.nbf.format(c.getPrix() * c.getQuantite()) + "\t\t" + Constantes.nbf.format((c.getPrix() - c.getRemise() - c.getRabais()) * c.getQuantite()) + "\n", 8, false));
                if (c.getRabais() > 0 || c.getRemise() > 0 || c.getRistourne() > 0) {
                    if (c.getRabais() > 0) {
                        tf.getChildren().add(getText("   Rabais: \t\t\t" + Constantes.nbf.format(c.getRabais()) + " \n", 8, false));
                    }
                    if (c.getRemise() > 0) {
                        tf.getChildren().add(getText("   Remise: \t\t\t" + Constantes.nbf.format(c.getRemise()) + " \n", 8, false));
                    }
                    if (c.getRistourne() > 0) {
                        tf.getChildren().add(getText("   Ristourne: \t\t\t" + Constantes.nbf.format(c.getRistourne()) + " \n", 8, false));
                    }
                }
                tremise = tremise + c.getRemise();
                tristourne = tristourne + c.getRistourne();
                trabais = trabais + c.getRabais();
            }
            tf.getChildren().add(new Text("-------------------------------------\n"));
            //footer
            Text tNet = getText("Net A payer: \t\t " + Constantes.nbf.format(netAPayer) + "\n", 9, false);
            Text tRem = new Text("");
            Text tRab = new Text("");
            Text tRis = new Text("");
            Text avan = new Text("");
            Text rest = new Text("");
            Text remb;
            if (tremise > 0) {
                tRem = getText("Remise:       \t\t " + Constantes.nbf.format(tremise) + "\n", 9, false);
            }
            if (trabais > 0) {
                tRab = getText("Rabais:       \t\t " + Constantes.nbf.format(trabais) + "\n", 9, false);
            }
            if (tristourne > 0) {
                tRis = getText("Ristourne:    \t\t " + Constantes.nbf.format(tristourne) + "\n", 9, false);
            }
            if (type.equals(Constantes.TYPE_BCV)) {
                avan = getText("Avance:       \t\t " + Constantes.nbf.format(montantAvance) + "\n", 9, false);
                rest = getText("Reste à payer:\t\t " + Constantes.nbf.format(montantTotal - (montantAvance)) + "\n", 9, false);
            }
            Text recu = getText("Reçu:     \t\t\t " + Constantes.nbf.format(montantRecu) + "\n", 9, false);
            remb = getText("Restitué:     \t\t " + Constantes.nbf.format(montantRecu - (type.equals(Constantes.TYPE_BCV) ? (avance) : (netAPayer))) + "\n", 9, false);
            Text end = new Text("MERCI POUR VOTRE CONFIANCE ! \n");
            end.setFont(Font.font("Helvetica", FontWeight.BOLD, 10));
            Text end1 = new Text("Les marchandises livrées ne peuvent être \n ni reprise ni echangées ");
            end1.setFont(Font.font(8));
            tf.getChildren().addAll(tNet, tRem, tRab, tRis, avan, rest, recu, remb, end, end1);
            return tf;
        }
        return new TextFlow();
    }

    public Text getText(String texte, double size, boolean bold) {
        Text re = new Text(texte);
        re.setFont(Font.font(size));
        if (bold) {
            re.setFont(Font.font("Helvetica", FontWeight.BOLD, size));
        }
        return re;
    }
}
