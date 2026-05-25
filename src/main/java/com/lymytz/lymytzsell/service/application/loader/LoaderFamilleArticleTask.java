/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.loader;

import com.lymytz.lymytzsell.persistence.dao.Options;
import com.lymytz.lymytzsell.persistence.entity.YvsBaseFamilleArticle;
import com.lymytz.lymytzsell.persistence.entity.YvsComEnteteDocVente;
import com.lymytz.lymytzsell.persistence.dao.LocalQueryFactories;
import com.lymytz.lymytzsell.view.controller.HomeCaisseController;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.lymytz.lymytzsell.service.utils.Constantes.CAT_MARCHANDISE;
import static com.lymytz.lymytzsell.service.utils.Constantes.CAT_PF;
import static com.lymytz.lymytzsell.service.utils.Constantes.CAT_SERVICE;

/**
 * @author LYMYTZ
 */
public class LoaderFamilleArticleTask extends Task<VBox> {

    private final LocalQueryFactories localQueryFactories = new LocalQueryFactories();
    private HomeCaisseController page;
    private final YvsComEnteteDocVente header;


    public LoaderFamilleArticleTask(HomeCaisseController page, final YvsComEnteteDocVente header) {
        this.page = page;
        this.header = header;

    }

    @Override
    public VBox call() throws Exception {
        VBox result = new VBox();
        try {
            List<Object[]> articles = filterArticlesInDb();
            VBox container = new VBox();
            container.getStyleClass().add("family-panel");
            for (Object[] article : articles) {
                var itemBox = buildNodeFamille(buildFamilleArticle(article));
                container.getChildren().add(itemBox);
            }
            result.getChildren().add(container);
        } catch (Exception ex) {
            Logger.getLogger(LoaderFamilleArticleTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private HBox buildNodeFamille(YvsBaseFamilleArticle famille) {
        return Optional.ofNullable(famille).map(f -> {
            var label = new Button(famille.getDesignation());
            label.getStyleClass().add("family-item");
            label.setMaxWidth(Double.MAX_VALUE);
            label.setWrapText(true);
            label.setOnAction(event -> {
                Button box = (Button) event.getTarget();
                VBox parent = (VBox) box.getParent().getParent();
                parent.getChildren().forEach(node -> ((HBox) node).getChildren().get(0).getStyleClass().remove("selected"));
                if (this.page.getSelectedFamilleArticle() != null && this.page.getSelectedFamilleArticle().equals(famille)) {
                    this.page.loadCatalogue(header, null, null);
                    this.page.setSelectedFamilleArticle(null);
                } else {
                    this.page.loadCatalogue(header, null, famille);
                    this.page.setSelectedFamilleArticle(famille);
                    label.getStyleClass().add("selected");
                }
            });
            HBox hbox = new HBox();
            HBox.setHgrow(label, Priority.ALWAYS); // Définit la croissance horizontale
            hbox.getChildren().add(label);
            return hbox;
        }).orElse(null);
    }

    private List<Object[]> filterArticlesInDb() {
        return localQueryFactories.loadBySQLQuery(getQuery(), getQueryParameters(), 0, 1000);
    }

    private YvsBaseFamilleArticle buildFamilleArticle(Object[] row) {
        YvsBaseFamilleArticle familleArticle = new YvsBaseFamilleArticle();
        familleArticle.setId((Long) row[0]);
        familleArticle.setReferenceFamille((String) row[1]);
        familleArticle.setDesignation((String) row[2]);
        return familleArticle;
    }

    private Options[] getQueryParameters() {
        return new Options[]{
                new Options(this.header.getCreneau().getCreneauPoint().getPoint().getId(), 1),
                new Options(this.header.getCreneau().getCreneauDepot().getDepot().getId(), 2),
                new Options(CAT_MARCHANDISE, 3),
                new Options(CAT_PF, 4),
                new Options(CAT_SERVICE, 5)
        };
    }

    private String getQuery() {
        return "SELECT DISTINCT f.id," + //0
                "f.reference_famille, " + //1
                "f.designation " +//2
                queryFrom() + " ORDER BY f.reference_famille;";
    }

    private String queryCount() {
        return "SELECT COUNT(y.id) " + queryFrom();
    }

    private String queryFrom() {
        return """
                FROM yvs_base_articles y INNER JOIN yvs_base_conditionnement c ON y.id=c.article
                                INNER JOIN yvs_base_famille_article f ON f.id=y.famille
                                INNER JOIN yvs_base_article_depot ad ON ad.article=y.id
                                LEFT JOIN yvs_base_unite_mesure u ON u.id=c.unite
                                LEFT JOIN yvs_base_groupes_article g ON g.id=y.groupe
                                LEFT JOIN yvs_base_classes_stat cl1 ON cl1.id=y.classe1
                                LEFT JOIN yvs_base_classes_stat cl2 ON cl2.id=y.classe2
                                LEFT JOIN yvs_base_article_point ap ON (ap.article=y.id AND ap.point=?)
                                LEFT JOIN yvs_base_conditionnement_point cp ON (cp.article=ap.id AND cp.conditionnement=c.id)
                                LEFT JOIN yvs_base_article_code_barre cb ON cb.conditionnement=c.id
                                WHERE ad.depot=? AND ad.actif IS TRUE AND (c.actif IS TRUE OR c.actif IS NULL) AND y.actif IS TRUE AND (cp.actif IS TRUE OR cp.actif IS NULL)
                                AND y.categorie IN (?,?,?)
                """;
    }
}
