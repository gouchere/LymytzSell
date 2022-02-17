/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.application.loader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lymytz.dao.entity.YvsComCreneauHoraireUsers;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.bean.Planning;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;

/**
 *
 * @author LYMYTZ
 */
public class LoaderPlanning extends Task<ObservableList<Planning>> {

    LQueryFactories Ldao;

    public LoaderPlanning() {
        Ldao = new LQueryFactories();
    }

    @Override
    public ObservableList<Planning> call() throws Exception {
        ObservableList<Planning> result = FXCollections.observableArrayList(loadData());
        return result;
    }

    private List<Planning> loadData() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date hier = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 12);
        List<YvsComCreneauHoraireUsers> datas = Ldao.loadByNamedQuery("YvsComCreneauHoraireUsers.findByUsersDates", new String[]{"users", "dateDebut", "dateFin"}, new Object[]{UtilsProject.currentUser.getUsers(), hier, new Date()}, 0, 15);
        List<Planning> list = new ArrayList<>();
        Planning p;
        for (YvsComCreneauHoraireUsers c : datas) {
            if (c.getCreneauPoint() != null) {
                p = new Planning();
                p.setActif(c.getActif());
                p.setDate(Constantes.dfD.format(c.getDateTravail()));
                p.setId(c.getId());
                p.setPointVente(c.getCreneauPoint().getPoint().getLibelle());
                p.setTranche(c.getCreneauPoint().getTranche().getTitre());
                list.add(p);
            }
        }
        return list;
    }

}
