/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.loader;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lymytz.dao.Options;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.bean.Articles;
import lymytz.service.utils.UtilsProject;

/**
 *
 * @author LYMYTZ
 */
public class LoaderCatalogue extends Task<ObservableList<Articles>> {

    LQueryFactories Ldao = new LQueryFactories();

    public LoaderCatalogue() {
    }

    @Override
    protected ObservableList<Articles> call() throws Exception {
        ObservableList<Articles> result = FXCollections.observableArrayList();
        if (UtilsProject.currentSociete != null) {
            List<Object[]> articles = Ldao.loadBySQLQuery(getQuery(), new Options[]{new Options(UtilsProject.currentSociete.getId(), 1)});
            int i = 1;
            for (Object[] art : articles) {
                result.add(buildBeanArticle(art, i));
                i++;
                this.updateProgress(i, articles.size());
                this.updateMessage(i + " sur " + articles.size());
            }
        } else {
        }
        return result;
    }

    private String getQuery() {
        StringBuilder sb = new StringBuilder("SELECT y.id,");
        sb.append("y.ref_art, ") //1
                .append("y.designation, ")//2
                .append("y.actif, ")//3
                .append("y.categorie, ")//4
                .append("u.reference, ")//5
                .append("u.libelle, ")//6
                .append("c.prix_achat, ")//7
                .append("c.prix, ")//8
                .append("c.prix_min, ")//9
                .append("f.designation, ")//10
                .append("g.designation, ")//11
                .append("cl1.designation, ")//12
                .append("cl2.designation ")//13
                .append("FROM yvs_base_articles y LEFT JOIN yvs_base_conditionnement c ON y.id=c.article ")
                .append("INNER JOIN yvs_base_famille_article f ON f.id=y.famille ")
                .append("LEFT JOIN yvs_base_unite_mesure u ON u.id=c.unite ")
                .append("LEFT JOIN yvs_base_groupes_article g ON g.id=y.groupe ")
                .append("LEFT JOIN yvs_base_classes_stat cl1 ON cl1.id=y.classe1 ")
                .append("LEFT JOIN yvs_base_classes_stat cl2 ON cl2.id=y.classe2 ")
                .append("WHERE f.societe=? ORDER BY f.id, y.ref_art ");
        return sb.toString();
    }

    private Articles buildBeanArticle(Object[] row, int line) {
        Articles re = new Articles();
        re.setActif((Boolean) (row[3] != null ? row[3] : false));
        re.setCategorie((String) (row[4] != null ? row[4] : ""));
        if (row[12] != null) {
            re.setClasse((String) row[12]);
        } else if (row[13] != null) {
            re.setClasse((String) row[13]);
        } else {
            re.setClasse("");
        }
        re.setDesignation((String) (row[2] != null ? row[2] : ""));
        re.setFamille((String) (row[10] != null ? row[10] : ""));
        re.setGroupe((String) (row[11] != null ? row[11] : ""));
        re.setNum(line);
        re.setPrixMin((Double) (row[9] != null ? row[9] : 0d));
        re.setPua((Double) (row[7] != null ? row[7] : 0d));
        re.setPuv((Double) (row[8] != null ? row[8] : 0d));
        re.setReference((String) (row[1] != null ? row[1] : ""));
        re.setUnite((String) (row[5] != null ? row[5] : ""));
        return re;
    }

}
