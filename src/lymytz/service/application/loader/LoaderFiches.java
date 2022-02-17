/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.loader;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.bean.HeaderDoc;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;

/**
 *
 * @author LYMYTZ
 */
public class LoaderFiches extends Task<ObservableList<HeaderDoc>> {

    LQueryFactories Ldao;

    public LoaderFiches() {
        Ldao = new LQueryFactories();
    }

    @Override
    public ObservableList<HeaderDoc> call() throws Exception {
        ObservableList<HeaderDoc> result = FXCollections.observableArrayList(loadData());
        return result;
    }

    private List<HeaderDoc> loadData() {
        //récupère le paramètre limite
        List<YvsComEnteteDocVente> datas = Ldao.loadByNamedQuery("YvsComEnteteDocVente.findByUsersStatut_", new String[]{"users"}, new Object[]{UtilsProject.currentUser.getUsers()});
        List<HeaderDoc> list = new ArrayList<>();
        HeaderDoc p;
        if (!datas.isEmpty()) {
            int i = 1;
            for (YvsComEnteteDocVente c : datas) {
                p = new HeaderDoc();
                p.setCloturer(c.getCloturer());
                p.setDate(Constantes.dfD.format(c.getDateEntete()));
                p.setId(c.getId());
                p.setIdPlus(c.getId());
                if (c.getCreneau() != null) {
                    p.setPointVente(c.getCreneau().getCreneauPoint().getPoint().getLibelle());
                    p.setTranche(c.getCreneau().getCreneauPoint().getTranche().getTitre());
                }
                p.setDateSave(Constantes.dfh.format(c.getDateSave()));
                list.add(p);
                i++;
                this.updateProgress(i, datas.size());
                this.updateMessage(i + " sur " + datas.size());
            }
        } else {
            this.updateProgress(100, 100);
            this.updateMessage(0 + " sur " + 0);
        }
        return list;
    }

}
