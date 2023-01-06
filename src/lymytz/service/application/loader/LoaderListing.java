/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.loader;

import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javax.print.attribute.standard.Severity;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsBaseArticles;
import lymytz.dao.entity.YvsBaseConditionnement;
import lymytz.dao.entity.YvsBaseUniteMesure;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.bean.ContentPanier;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;

/**
 *
 * @author LYMYTZ
 */
public class LoaderListing extends Task<ObservableList<ContentPanier>> {

    LQueryFactories Ldao;
    boolean details;

    public LoaderListing(boolean details) {
        Ldao = new LQueryFactories();
        this.details = details;
    }

    @Override
    public ObservableList<ContentPanier> call() throws Exception {
        List<Object[]> factures = Ldao.loadBySQLQuery(details ? getQuery() : getQuery1(), new Options[]{
            new Options(UtilsProject.headerDoc.getId(), 1)});
        int i = 1;
        ObservableList<ContentPanier> result = FXCollections.observableArrayList();
        if (factures.size() > 0) {
            ContentPanier f = null;
            for (Object[] art : factures) {
                if (details) {
                    result.add(buildBeanFacture(f, art, i));
                }else{
                    result.add(buildBeanContentCumul(f, art, i));
                }
                i++;
                this.updateProgress(i, factures.size());
                this.updateMessage(i + " sur " + factures.size());
            }
        } else {
            this.updateProgress(0, 0);
            this.updateMessage(0 + " sur " + factures.size());
        }
        return result;
    }

    private String getQuery() {
        StringBuilder sb = new StringBuilder("SELECT d.id, ");
        sb.append("c.id, ") //1
                .append("a.ref_art, ")//2
                .append("a.designation, ")//3
                .append("u.reference, ")//4
                .append("d.nom_client, ")//5
                .append("c.date_save, ")//6
                .append("d.statut_livre, ")//7
                .append("c.prix, ")//8
                .append("c.quantite, ")//9
                .append("c.prix_total, ")//10
                .append("d.type_doc, ")//11
                .append("co.id, ")//12
                .append("d.num_doc ")//13
                .append("FROM yvs_com_contenu_doc_vente c ")
                .append("INNER JOIN yvs_com_doc_ventes d ON c.doc_vente=d.id ")
                .append("INNER JOIN yvs_base_conditionnement co ON co.id=c.conditionnement ")
                .append("INNER JOIN yvs_base_articles a ON a.id=co.article ")
                .append("INNER JOIN yvs_base_unite_mesure u ON u.id=co.unite ")
                .append("WHERE d.entete_doc=? AND d.type_doc IN ('FV', 'BCV') AND d.statut='V' ")
                .append("ORDER BY d.id, c.id ");
        return sb.toString();
    }

    private String getQuery1() {
        StringBuilder sb = new StringBuilder("SELECT DISTINCT a.id, ");
        sb.append("a.ref_art, ") //1
                .append("a.designation, ")//2
                .append("d.type_doc, ")//3
                .append("u.reference, ")//4
                .append("SUM(c.quantite), ")//5
                .append("SUM(c.prix_total) ")//6
                .append("FROM yvs_com_contenu_doc_vente c ")
                .append("INNER JOIN yvs_com_doc_ventes d ON c.doc_vente=d.id ")
                .append("INNER JOIN yvs_base_conditionnement co ON co.id=c.conditionnement ")
                .append("INNER JOIN yvs_base_articles a ON a.id=co.article ")
                .append("INNER JOIN yvs_base_unite_mesure u ON u.id=co.unite ")
                .append("WHERE d.entete_doc=? AND d.type_doc IN ('FV', 'BCV') AND d.statut='V' ")
                .append("GROUP BY a.id ,u.id, d.type_doc ")
                .append("ORDER BY a.id ");
        LogFiles.addLogInFile(sb.toString(), Severity.REPORT);
        return sb.toString();
    }

    private ContentPanier buildBeanFacture(ContentPanier re, Object[] row, int line) {
        re = new ContentPanier();
        re.setIdContent((Long) row[1]);
        YvsBaseConditionnement c = null;
        YvsComDocVentes d = null;
        if (row[12] != null ? ((Long) row[12]) > 0 : false) {
            c = new YvsBaseConditionnement((Long) row[12]);
            c.setUnite(new YvsBaseUniteMesure(0L, (String) row[4]));
            c.setArticle(new YvsBaseArticles(0L, (String) row[2], (String) row[3]));
        }
        if (row[0] != null ? ((Long) row[0]) > 0 : false) {
            d = new YvsComDocVentes((Long) row[0]);
            d.setNumDoc((String) row[13]);
            d.setTypeDoc((String) row[11]);
            d.setNomClient((String) row[5]);
            d.setStatutLivre((String) row[7]);

        }
        re.setConditionnement(c);
        re.setFacture(d);
        re.setPrix(row[8] != null ? (Double) row[8] : 0d);
        re.setQuantite(row[9] != null ? (Double) row[9] : 0d);
        re.setMontantTotalTTC(row[10] != null ? (Double) row[10] : 0d);
        re.setDateSave((row[6] != null) ? (Date) row[6] : null);
        return re;
    }

    private ContentPanier buildBeanContentCumul(ContentPanier re, Object[] row, int line) {
        re = new ContentPanier();
        YvsBaseConditionnement c = null;
        YvsComDocVentes d = null;
        if (row[4] != null) {
            c = new YvsBaseConditionnement();
            c.setUnite(new YvsBaseUniteMesure(0L, (String) row[4]));
            c.setArticle(new YvsBaseArticles(0L, (String) row[1], (String) row[2]));
        }
        if (row[0] != null ? ((Long) row[0]) > 0 : false) {
            d = new YvsComDocVentes((Long) row[0]);
            d.setTypeDoc((String) row[3]);
        }
        re.setConditionnement(c);
        re.setFacture(d);
        re.setQuantite(row[5] != null ? (Double) row[5] : 0d);
        re.setMontantTotalTTC(row[6] != null ? (Double) row[6] : 0d);
        return re;
    }

}
