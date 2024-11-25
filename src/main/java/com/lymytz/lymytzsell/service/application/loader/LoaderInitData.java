package com.lymytz.lymytzsell.service.application.loader;


import com.lymytz.lymytzsell.dao.entity.YvsComEnteteDocVente;
import com.lymytz.lymytzsell.dao.query.LocalQueryFactories;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.UtilsProject;
import com.lymytz.lymytzsell.view.main.HomeCaisseController;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoaderInitData extends Task<Void> {
    LocalQueryFactories dao;
    HomeCaisseController mainController;
    final List<String> etats = List.of(Constantes.ETAT_CLOTURE, Constantes.ETAT_ATTENTE, Constantes.ETAT_SUSPENDU);

    public LoaderInitData(final LocalQueryFactories dao, final HomeCaisseController page) {
        this.dao = dao;
        this.mainController = page;
    }

    @Override
    protected Void call() throws Exception {
        /*
         * 1. récupère le créneau
         * 2. Récupère le header
         * 3. Récupérer la caisse
         * 4. Charger les familles d'articles
         * 5. Charger le catalogue
         * 6.
         * */
        Date ier = Constantes.givePrevOrNextDate(new Date(), -2);
        List<YvsComEnteteDocVente> l = dao.loadByNamedQuery("YvsComEnteteDocVente.findEncourByUsers_",
                new String[]{"users", "etats", "date"},
                new Object[]{UtilsProject.currentUser.getUsers(), etats, ier});
        if (l == null || l.isEmpty()) {
            UtilsProject.currentsHeaderDoc = new ArrayList<>();
        } else {
            UtilsProject.currentsHeaderDoc = l;
            UtilsProject.headerDoc = l.get(0);
        }
        UtilsProject.caisse = dao.findOneByNQ("YvsBaseCaisse.findByCaissier", new String[]{"caissier"}, new Object[]{UtilsProject.currentUser.getUsers()});
        if (UtilsProject.headerDoc != null) {
            mainController.loadFamilleArticles(UtilsProject.headerDoc);
            mainController.loadCatalogue(UtilsProject.headerDoc, null);
        }
        return null;
    }
}
