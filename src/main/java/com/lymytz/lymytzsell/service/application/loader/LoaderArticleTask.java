/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.layout.GridPane;
import com.lymytz.lymytzsell.dao.Options;
import com.lymytz.lymytzsell.dao.entity.YvsBaseArticles;
import com.lymytz.lymytzsell.dao.entity.YvsBaseClassesStat;
import com.lymytz.lymytzsell.dao.entity.YvsBaseConditionnement;
import com.lymytz.lymytzsell.dao.entity.YvsBaseFamilleArticle;
import com.lymytz.lymytzsell.dao.entity.YvsBaseGroupesArticle;
import com.lymytz.lymytzsell.dao.entity.YvsBaseUniteMesure;
import com.lymytz.lymytzsell.dao.query.LocalQueryFactories;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.view.component.CustomComponents;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
import javafx.scene.layout.VBox;

/**
 * @author LYMYTZ
 */
public class LoaderArticleTask extends Task<ObservableList<GridPane>> {

    LocalQueryFactories localQueryFactories = new LocalQueryFactories();
    HomeCaisseController page;
    String reference;
    List<String> categories;

    public LoaderArticleTask(HomeCaisseController page, String reference) {
        this.page = page;
        this.reference = reference;
        categories = new ArrayList<>();
        categories.add(Constantes.CAT_MARCHANDISE);
        categories.add(Constantes.CAT_PF);
        categories.add(Constantes.CAT_SERVICE);

    }

    public YvsBaseConditionnement findOneArticle() {
        List<Object[]> articles = filterArticlesInDb();
        if (articles.size() != 1) {
            return null;
        } else {
            return buildConditionnement(articles.get(0));
        }
    }

    @Override
    public ObservableList<GridPane> call() throws Exception {
        ObservableList<GridPane> result = FXCollections.observableArrayList();
        try {
            this.page.createAndAddProgressBar();
            List<Object[]> articles = filterDoublon(filterArticlesInDb());
            GridPane container = CustomComponents.getBasicGridPane();
            var total = articles.size();
            int columns = getNbColumns();
            int rowIndex = 0;
            int colIndex = 0;
            for (Object[] article : articles) {
                VBox itemBox = CustomComponents.displayCatalogue(buildConditionnement(article), page);
                container.add(itemBox, colIndex, rowIndex);

                colIndex++;
                if (colIndex == columns) {
                    colIndex = 0;
                    rowIndex++;
                }
                this.updateProgress(rowIndex, articles.size() / columns);
                this.updateMessage(rowIndex + " sur " + articles.size());
            }
            result.add(container);
            if (total == 0) {
                this.updateProgress(0, 0);
            }
        } catch (Exception ex) {
            Logger.getLogger(LoaderArticleTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private int getNbColumns() {
        try {
            var propertyValue = UtilsProject.getVal(Constantes.KEY_COL_CATALOGUE);
            return Optional.ofNullable(propertyValue).map(Integer::parseInt).orElse(3);
        } catch (NumberFormatException ex) {
            return 3;
        }
    }

    private List<Object[]> filterDoublon(List<Object[]> articles) {
        List<Object[]> re = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        Long id;
        for (Object[] article : articles) {
            if (article[10] != null) {
                id = ((Long) article[10]);
                if (!ids.contains(id)) {
                    re.add(article);
                    ids.add(id);
                }
            }
        }
        return re;
    }


    private List<Object[]> filterArticlesInDb() {
        return localQueryFactories.loadBySQLQuery(getQuery(), new Options[]{
                new Options(UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), 1),
                new Options(UtilsProject.depotLivraison.getId(), 2),
                new Options(("%" + reference + "%"), 3),
                new Options(("%" + reference + "%"), 4),
                new Options(reference, 5),
                new Options(Constantes.CAT_MARCHANDISE, 6),
                new Options(Constantes.CAT_PF, 7),
                new Options(Constantes.CAT_SERVICE, 8)
        }, 0, 50);
    }

    private String getQuery() {
        return "SELECT y.id," + //0
                "y.ref_art, " + //1
                "y.designation, " +//2
                "y.actif, " +//3
                "y.categorie, " +//4
                "y.puv_ttc, " +//5
                "cp.change_prix, " +//6
                "u.id, " +//7
                "u.reference, " +//8
                "u.libelle, " +//9
                "c.id, " +//10
                "c.prix_achat, " +//11
                "c.prix, " +//12
                "c.prix_min, " +//13
                "f.id, " +//14
                "f.designation, " +//15
                "g.id::bigint, " +//16
                "g.designation, " +//17
                "cl1.designation, " +//18
                "cl2.designation, " +//19
                "c.marge_min, " +//20
                "cp.puv, " +//21
                "cp.prix_min, " +//22
                "y.photo_1, " +//23
                "y.photo_2, " +//24
                "y.photo_3, " +//25
                "cb.code_barre, " +//26
                "cl1.id::bigint, " +//27
                "cl2.id::bigint " +//28
                "FROM yvs_base_articles y LEFT JOIN yvs_base_conditionnement c ON y.id=c.article " +
                "INNER JOIN yvs_base_famille_article f ON f.id=y.famille " +
                "INNER JOIN yvs_base_article_depot ad ON ad.article=y.id " +
                "LEFT JOIN yvs_base_unite_mesure u ON u.id=c.unite " +
                "LEFT JOIN yvs_base_groupes_article g ON g.id=y.groupe " +
                "LEFT JOIN yvs_base_classes_stat cl1 ON cl1.id=y.classe1 " +
                "LEFT JOIN yvs_base_classes_stat cl2 ON cl2.id=y.classe2 " +
                "LEFT JOIN yvs_base_article_point ap ON (ap.article=y.id AND ap.point=?) " +
                "LEFT JOIN yvs_base_conditionnement_point cp ON (cp.article=ap.id AND cp.conditionnement=c.id) " +
                "LEFT JOIN yvs_base_article_code_barre cb ON cb.conditionnement=c.id " +
                "WHERE ad.depot=? AND (UPPER(y.ref_art) LIKE UPPER(?) OR UPPER(y.designation) LIKE UPPER(?) OR  UPPER(cb.code_barre)=UPPER(?)) " +
                "AND ad.actif IS TRUE AND (c.actif IS TRUE OR c.actif IS NULL) AND y.actif IS TRUE AND (cp.actif IS TRUE OR cp.actif IS NULL) " +
                "AND y.categorie IN (?,?,?) " +
                "ORDER BY f.id, y.ref_art ";
    }

    private YvsBaseConditionnement buildConditionnement(Object[] row) {
        YvsBaseConditionnement re = new YvsBaseConditionnement();
        Double prix;
        Double prixMin;
        prix = (Double) (row[21] != null ? row[21] : 0d);
        prixMin = (Double) (row[22] != null ? row[22] : 0d);
        assert (row[10] != null ? row[10] : -1) instanceof Long;
        re.setId((Long) row[10]);
        if (prix > 0) {
            re.setPrix(prix);
        } else {
            re.setPrix((Double) (row[12] != null ? row[12] : 0d));
        }
        if (prixMin > 0) {
            re.setPrixMin(prixMin);
        } else {
            re.setPrixMin((Double) (row[13] != null ? row[13] : 0d));
        }
        re.setPrixAchat((Double) (row[11] != null ? row[11] : 0d));
        re.setMargeMin((Double) (row[20] != null ? row[20] : 0d));
        re.setArticle(buildEntityArt(row));
        YvsBaseUniteMesure u = new YvsBaseUniteMesure();
        assert (row[7] != null ? row[7] : -1) instanceof Long;
        u.setId((Long) row[7]);
        u.setReference((String) (row[8] != null ? row[8] : null));
        u.setLibelle((String) (row[9] != null ? row[9] : null));
        re.setUnite(u);
        return re;
    }

    private YvsBaseArticles buildEntityArt(Object[] row) {
        YvsBaseArticles art = new YvsBaseArticles();
        art.setId((Long) (row[0] != null ? row[0] : -1));
        art.setRefArt((String) (row[1] != null ? row[1] : null));
        art.setDesignation((String) (row[2] != null ? row[2] : null));
        art.setCategorie((String) (row[4] != null ? row[4] : null));
        art.setPuvTtc((Boolean) (row[5] != null ? row[5] : false));
        art.setChangePrix((Boolean) (row[6] != null ? row[6] : false));
        if (row[27] != null || row[28] != null) {
            YvsBaseClassesStat cl = new YvsBaseClassesStat();
            if (row[18] != null) {
                cl.setId((Long) row[27]);
                cl.setDesignation((String) row[18]);
            } else if (row[19] != null) {
                cl.setId((Long) row[28]);
                cl.setDesignation((String) row[19]);
            } else {
                cl.setDesignation("");
            }
//            art.setClasse1(cl);
        }
        YvsBaseFamilleArticle f = new YvsBaseFamilleArticle();
        f.setId((Long) (row[14] != null ? row[14] : -1));
        f.setDesignation((String) (row[15] != null ? row[15] : null));
        art.setFamille(f);
        YvsBaseGroupesArticle g = new YvsBaseGroupesArticle();
        g.setId((Long) (row[16] != null ? row[16] : -1L));
        g.setDesignation((String) (row[17] != null ? row[17] : null));
        art.setPhoto1((String) (row[23] != null ? row[23] : null));
        art.setPhoto2((String) (row[24] != null ? row[24] : null));
        art.setPhoto3((String) (row[25] != null ? row[25] : null));
//        art.setGroupe(g);
        return art;
    }

}
