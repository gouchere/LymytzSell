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
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.application.bean.Factures;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;
import lymytz.service.utils.log.LogFiles;

/**
 *
 * @author LYMYTZ
 */
public class LoaderFacture_old extends Task<ObservableList<Factures>> {

    LQueryFactories Ldao;
    String type;
    

    public LoaderFacture_old(String type) {
        Ldao = new LQueryFactories();
        this.type = type;
    }

    @Override
    public ObservableList<Factures> call() throws Exception {
        Options[] params;
        if(type.equals(Constantes.TYPE_FV)){
            params=new Options[]{
            new Options(UtilsProject.headerDoc.getId(), 1),
            new Options(this.type, 2),
            new Options(Constantes.ETAT_ANNULE, 3),};
        }else{
            params=new Options[]{
            new Options(this.type, 1),
            new Options(Constantes.ETAT_ANNULE, 2),
            new Options(Constantes.ETAT_LIVRE, 3),
            new Options(UtilsProject.currentAgence.getId(), 4)};
        }
        List<Object[]> factures = Ldao.loadBySQLQuery((UtilsProject.REPLICATION) ? getQuery1(true) : getQuery(true), params);
        int i = 1;
        ObservableList<Factures> result = FXCollections.observableArrayList();
        if (factures.size() > 0) {
            Factures f = null;
            for (Object[] art : factures) {
                result.add(buildBeanFacture(f, art, i));
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

    private String getQuery(boolean withTtc) {
        StringBuilder sb = new StringBuilder("SELECT d.id,");
        String where;
        if (type.equals(Constantes.TYPE_FV)) {
            where = "WHERE e.id=? AND d.type_doc=? AND d.statut!=? ";
        } else {
             where = "WHERE d.type_doc=? AND d.statut!=? AND d.statut_livre!=? AND e.agence=? ";
        }
        sb.append("e.id, ") //1
                .append("d.type_doc, ")//2
                .append("d.statut, ")//3
                .append("d.num_doc, ")//4
                .append("d.nom_client, ")//5
                .append("d.heure_doc, ")//6
                .append("d.statut_livre, ")//7
                .append("d.statut_regle, ")//8
                .append("cl.nom, ")//9
                .append("cl.prenom, ")//10
                .append("e.date_entete, ")//11
                .append("di.libele, ")//12
                .append("cc.code, ")//13
                .append("d.date_livraison_prevu ")//14
                .append((withTtc) ? ",get_ttc_vente(d.id) " : "")//15
                .append("FROM yvs_com_doc_ventes d ")
                .append("INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc ")
                .append("INNER JOIN yvs_com_creneau_horaire_users cr ON cr.id=e.creneau ")
                .append("INNER JOIN yvs_com_client cl ON cl.id=d.client ")
                .append("INNER JOIN yvs_base_categorie_comptable cc ON cc.id=d.categorie_comptable ")
                .append("LEFT JOIN yvs_dictionnaire di ON di.id=d.adresse ")
                .append(where)
                .append("ORDER BY d.type_doc, d.num_doc ");
        return sb.toString();
    }

    private String getQuery1(boolean withTtc) {
        StringBuilder sb = new StringBuilder("SELECT DISTINCT d.id,");
        sb.append("e.id, ") //1
                .append("d.type_doc, ")//2
                .append("d.statut, ")//3
                .append("d.num_doc, ")//4
                .append("d.nom_client, ")//5
                .append("d.date_save, ")//6
                .append("d.statut_livre, ")//7
                .append("d.statut_regle, ")//8
                .append("cl.nom, ")//9
                .append("cl.prenom, ")//10
                .append("e.date_entete, ")//11
                .append("di.libele, ")//12
                .append("cc.code, ")//13
                .append("d.date_livraison_prevu, ")//14
                .append("ds.id_distant, ")//15
                .append("l.to_listen ")//16
                .append((withTtc) ? ",get_ttc_vente(d.id) " : "")//17
                .append("FROM yvs_com_doc_ventes d ")
                .append("INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc ")
                .append("INNER JOIN yvs_com_creneau_horaire_users cr ON cr.id=e.creneau ")
                .append("INNER JOIN yvs_com_client cl ON cl.id=d.client ")
                .append("INNER JOIN yvs_base_categorie_comptable cc ON cc.id=d.categorie_comptable ")
                .append("LEFT JOIN yvs_dictionnaire di ON di.id=d.adresse ")
                .append("LEFT JOIN yvs_synchro_listen_table l ON l.id_source=d.id ")
                .append("LEFT JOIN yvs_synchro_data_synchro ds ON ds.id_listen=l.id ")
                .append("WHERE e.id=? AND d.type_doc=? AND d.statut!=? ")
                .append("AND l.name_table='yvs_com_doc_ventes'")
                .append("ORDER BY d.type_doc, d.num_doc ");
        LogFiles.addLogInFile(sb.toString(), Severity.REPORT);
        return sb.toString();
    }

    private Factures buildBeanFacture(Factures re, Object[] row, int line) {
        re = new Factures();
        re.setActif(true);
        re.setAdresse((String) row[12]);
        re.setCategorieComptable((String) row[13]);
        re.setDate((row[11] != null) ? (Date) row[11] : null);
        re.setHeader(buildBeanHeader(row));
        re.setHeure((row[6] != null) ? (Date) row[6] : null);
        re.setId((row[0] != null) ? (Long) row[0] : -1L);
        if (row[5] != null) {
            re.setNomClient((String) row[5]);
        } else {
            String nom = (String) row[9];
            String prenom = (String) row[10];
            re.setNomClient(getNom_prenom(nom, prenom));
        }
        re.setNumDoc((String) row[4]);
        re.setStatutDoc((String) row[3]);
        re.setStatutLivraison((String) row[7]);
        re.setStatutReglement((String) row[8]);
        re.setType((String) row[2]);
        re.setNumLine(line);
        re.setDateLiv((row[14] != null) ? (Date) row[14] : null);
        if (UtilsProject.REPLICATION) {
            re.setIdDistant((row[15] != null) ? (Long) row[15] : -1L);
            re.setToListen((row[16] != null) ? (Boolean) row[16] : false);
            re.setTotal((row[17] != null) ? (Double) row[17] : 0d);
        } else {
            re.setTotal((row[15] != null) ? (Double) row[15] : 0d);
        }
        return re;
    }

    private YvsComEnteteDocVente buildBeanHeader(Object[] row) {
        YvsComEnteteDocVente re = new YvsComEnteteDocVente();
        re.setId((row[1] != null) ? (Long) row[1] : -1L);
        re.setDateEntete((row[11] != null) ? (Date) row[11] : null);
        return re;
    }

    private String getNom_prenom(String nom, String prenom) {
        StringBuilder re = new StringBuilder();
        if (Constantes.asString(nom)) {
            re.append(nom);
        }
        if (Constantes.asString(prenom)) {
            re.append(" ").append(nom);
        }
        return re.toString();
    }

}

