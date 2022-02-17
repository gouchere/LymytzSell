/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.layout.GridPane;
import lymytz.dao.Options;
import lymytz.dao.entity.YvsBaseArticles;
import lymytz.dao.entity.YvsBaseClassesStat;
import lymytz.dao.entity.YvsBaseConditionnement;
import lymytz.dao.entity.YvsBaseFamilleArticle;
import lymytz.dao.entity.YvsBaseGroupesArticle;
import lymytz.dao.entity.YvsBaseUniteMesure;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;
import lymytz.view.component.CustomComponents;
import lymytz.view.main.HomeCaisseController;

/**
 *
 * @author LYMYTZ
 */
public class LoaderConditionnement extends Task<ObservableList<GridPane>> {

    LQueryFactories Ldao = new LQueryFactories();
    HomeCaisseController page;
    String reference;
    List<String> categories;

    public LoaderConditionnement(HomeCaisseController page, String reference) {
        this.page = page;
        this.reference = reference;
        categories = new ArrayList<>();
        categories.add(Constantes.CAT_MARCHANDISE);
        categories.add(Constantes.CAT_PF);
        categories.add(Constantes.CAT_SERVICE);

    }

    public YvsBaseConditionnement findOneArticle(String ref) {
        List<Object[]> articles = Ldao.loadBySQLQuery(getQuery(), new Options[]{
            new Options(UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), 1),
            new Options(UtilsProject.depotLivraison.getId(), 2),
            new Options(("%" + reference + "%"), 3),
            new Options(("%" + reference + "%"), 4),
            new Options(reference, 5),
            new Options(Constantes.CAT_MARCHANDISE, 6),
            new Options(Constantes.CAT_PF, 7),
            new Options(Constantes.CAT_SERVICE, 8)
        }, 0, 50);
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
            List<Object[]> l = Ldao.loadBySQLQuery(getQuery(), new Options[]{
                new Options(UtilsProject.headerDoc.getCreneau().getCreneauPoint().getPoint().getId(), 1),
                new Options(UtilsProject.depotLivraison.getId(), 2),
                new Options(("%" + reference + "%"), 3),
                new Options(("%" + reference + "%"), 4),
                new Options(reference, 5),
                new Options(Constantes.CAT_MARCHANDISE, 6),
                new Options(Constantes.CAT_PF, 7),
                new Options(Constantes.CAT_SERVICE, 8)
            }, 0, 50);
            List<Object[]> articles = filterDoublon(l);
            YvsBaseConditionnement y1 = null, y2 = null;
            int j = 0;
            int total = (!pair(articles.size())) ? (articles.size() + 1) / 2 : (articles.size() / 2);
            for (int i = 0; i < total; i++) {
                if (j < articles.size()) {
                    y1 = buildConditionnement(articles.get(j));
                    j++;
                }
                if (j < articles.size()) {
                    y2 = buildConditionnement(articles.get(j));
                }
                result.add(CustomComponents.displayCatalogue(y1, y2, page));
                j++;
                this.updateProgress(j, articles.size() / 2);
                this.updateMessage(j + " sur " + articles.size());

            }
        } catch (Exception ex) {
            Logger.getLogger(LoaderConditionnement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
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

    private String getQuery() {
        StringBuilder sb = new StringBuilder("SELECT y.id,");   //0
        sb.append("y.ref_art, ") //1
                .append("y.designation, ")//2
                .append("y.actif, ")//3
                .append("y.categorie, ")//4
                .append("y.puv_ttc, ")//5
                .append("cp.change_prix, ")//6
                .append("u.id, ")//7
                .append("u.reference, ")//8
                .append("u.libelle, ")//9
                .append("c.id, ")//10
                .append("c.prix_achat, ")//11
                .append("c.prix, ")//12
                .append("c.prix_min, ")//13
                .append("f.id, ")//14
                .append("f.designation, ")//15
                .append("g.id::bigint, ")//16
                .append("g.designation, ")//17
                .append("cl1.designation, ")//18
                .append("cl2.designation, ")//19
                .append("c.marge_min, ")//20
                .append("cp.puv, ")//21
                .append("cp.prix_min, ")//22
                .append("y.photo_1, ")//23
                .append("y.photo_2, ")//24
                .append("y.photo_3, ")//25
                .append("cb.code_barre ")//26
                .append("FROM yvs_base_articles y LEFT JOIN yvs_base_conditionnement c ON y.id=c.article ")
                .append("INNER JOIN yvs_base_famille_article f ON f.id=y.famille ")
                .append("INNER JOIN yvs_base_article_depot ad ON ad.article=y.id ")
                .append("LEFT JOIN yvs_base_unite_mesure u ON u.id=c.unite ")
                .append("LEFT JOIN yvs_base_groupes_article g ON g.id=y.groupe ")
                .append("LEFT JOIN yvs_base_classes_stat cl1 ON cl1.id=y.classe1 ")
                .append("LEFT JOIN yvs_base_classes_stat cl2 ON cl2.id=y.classe2 ")
                .append("LEFT JOIN yvs_base_article_point ap ON (ap.article=y.id AND ap.point=?) ")
                .append("LEFT JOIN yvs_base_conditionnement_point cp ON (cp.article=ap.id AND cp.conditionnement=c.id) ")
                .append("LEFT JOIN yvs_base_article_code_barre cb ON cb.conditionnement=c.id ")
                .append("WHERE ad.depot=? AND (UPPER(y.ref_art) LIKE UPPER(?) OR UPPER(y.designation) LIKE UPPER(?) OR  UPPER(cb.code_barre)=UPPER(?)) ")
                .append("AND ad.actif IS TRUE AND (c.actif IS TRUE OR c.actif IS NULL) AND y.actif IS TRUE AND (cp.actif IS TRUE OR cp.actif IS NULL) ")
                .append("AND y.categorie IN (?,?,?) ")
                .append("ORDER BY f.id, y.ref_art ");
        System.err.println("Query : " + sb.toString());
        return sb.toString();
    }

    private YvsBaseConditionnement buildConditionnement(Object[] row) {
        YvsBaseConditionnement re = new YvsBaseConditionnement();
        Double prix, prixMin;
        prix = (Double) (row[21] != null ? row[21] : 0d);
        prixMin = (Double) (row[22] != null ? row[22] : 0d);
        re.setId((Long) (row[10] != null ? row[10] : -1));
        if (prix != null ? prix > 0 : false) {
            re.setPrix(prix);
        } else {
            re.setPrix((Double) (row[12] != null ? row[12] : 0d));
        }
        if (prixMin != null ? prixMin > 0 : false) {
            re.setPrixMin(prixMin);
        } else {
            re.setPrixMin((Double) (row[13] != null ? row[13] : 0d));
        }
        re.setPrixAchat((Double) (row[11] != null ? row[11] : 0d));
        re.setMargeMin((Double) (row[20] != null ? row[20] : 0d));
        re.setArticle(buildEntityArt(row));
        YvsBaseUniteMesure u = new YvsBaseUniteMesure();
        u.setId((Long) (row[7] != null ? row[7] : -1));
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
        YvsBaseClassesStat cl = new YvsBaseClassesStat();
        if (row[18] != null) {
            cl.setDesignation((String) row[18]);
        } else if (row[19] != null) {
            cl.setDesignation((String) row[19]);
        } else {
            cl.setDesignation("");
        }
        art.setClasse1(cl);
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
        art.setGroupe(g);
        return art;
    }

    private boolean pair(int nb) {
        return (nb % 2) == 0;
    }
}
