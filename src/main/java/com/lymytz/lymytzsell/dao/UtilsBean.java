/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.lymytz.lymytzsell.dao.entity.YvsAgences;
import com.lymytz.lymytzsell.dao.entity.YvsBaseDepots;
import com.lymytz.lymytzsell.dao.entity.YvsBaseModeleReference;
import com.lymytz.lymytzsell.dao.entity.YvsBasePointVente;
import com.lymytz.lymytzsell.dao.entity.YvsComContenuDocVente;
import com.lymytz.lymytzsell.dao.entity.YvsComDocVentes;
import com.lymytz.lymytzsell.dao.entity.YvsComEnteteDocVente;
import com.lymytz.lymytzsell.dao.entity.YvsComptaCaissePieceVente;
import com.lymytz.lymytzsell.dao.entity.YvsSocietes;
import com.lymytz.lymytzsell.dao.query.LocalQueryFactories;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.UtilsProject;

import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_BAV_NAME;
import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_BCV_NAME;
import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_BLV_NAME;
import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_BRV_NAME;
import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_FAV_NAME;
import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_FRV_NAME;
import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_FV_NAME;

/**
 * @author LENOVO
 */
public class UtilsBean {

    LocalQueryFactories dao = new LocalQueryFactories();


    /*Générer les références des documents*/
    private YvsBaseModeleReference rechercheModeleReference(String mot) {
        if (!mot.isEmpty()) {
            String[] ch = new String[]{"designation", "societe"};
            Object[] v = new Object[]{mot, UtilsProject.currentAgence.getSociete()};
            String query = "YvsBaseModeleReference.findByElement";
            return dao.findOneByNQ(query, ch, v);
        }
        return null;
    }

    public String genererReference(String element, Date date, long id, String type, String code, YvsAgences agence) {
        YvsBaseModeleReference model = rechercheModeleReference(element);
        if (model != null && model.getId() > 0) {
            return getReferenceElement(model, date, id, type, code, agence);
        } else {
            return "";
        }

    }

    private String getReferenceElement(YvsBaseModeleReference modele, Date date, long id, String type, String code, YvsAgences agence) {
        String motRefTable = "";
        StringBuilder inter = new StringBuilder(genererPrefixeComplet(modele, date, id, type, code, agence));
        switch (modele.getElement().getDesignation()) {
            case TYPE_BLV_NAME,
                 TYPE_BRV_NAME,
                 TYPE_BAV_NAME,
                 TYPE_BCV_NAME,
                 TYPE_FRV_NAME,
                 TYPE_FAV_NAME,
                 TYPE_FV_NAME -> {
                String[] ch = new String[]{"numDoc"};
                Object[] v = new Object[]{inter + "%"};
                String query = "YvsComDocVentes.findByReference";
                List<YvsComDocVentes> l = dao.loadByNamedQuery(query, ch, v, 0, 1);
                if (l != null && !l.isEmpty()) {
                    motRefTable = l.get(0).getNumeroExterne();
                } else {
                    motRefTable = "";
                }
            }
            case Constantes.TYPE_PC_NAME -> {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", UtilsProject.currentSociete};
                List<YvsComptaCaissePieceVente> l = dao.loadByNamedQuery("YvsComptaCaissePieceVente.findByNumeroPiece", ch, v);
                if (l != null && !l.isEmpty()) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
            }

            default -> motRefTable = "";
        }
        String partieNum = motRefTable.replaceFirst(inter.toString(), "");
        if (!partieNum.trim().isEmpty()) {
            int num = Integer.parseInt(partieNum.trim().replace("°", ""));
            if (Integer.toString(num + 1).length() > modele.getTaille()) {
                return "";
            } else {
                inter.append("0".repeat(Math.max(0, (modele.getTaille() - Integer.toString(num + 1).length()))));
            }
            inter.append(Long.parseLong(partieNum.trim().replace("°", "")) + 1);
        } else {
            inter.append("0".repeat(Math.max(0, modele.getTaille() - 1)));
            inter.append("1");
        }
        return inter.toString();
    }

    public String genererPrefixe(YvsBaseModeleReference modele, long id, String type, String code, YvsAgences agence) {
        String inter = modele.getPrefix();
        if (id > 0 && type != null) {
            code = genererPrefixe(modele, id, agence);
        }
        if (code != null && !code.trim().isEmpty()) {
            inter += modele.getSeparateur() + code;
        }
        inter += modele.getSeparateur();
        return inter;
    }

    public String genererPrefixe(YvsBaseModeleReference modele, long id, YvsAgences agence) {
        if (Boolean.TRUE.equals(modele.getCodePoint())) {
            String code = "";
            switch (modele.getElementCode()) {
                case Constantes.SOCIETE: {
                    if (agence != null && !agence.getSociete().getCodeAbreviation().trim().isEmpty()) {
                        code = agence.getSociete().getCodeAbreviation();
                    }
                    break;
                }
                case Constantes.AGENCE: {
                    if (agence != null && Constantes.asString(agence.getAbbreviation())) {
                        code = agence.getAbbreviation();
                    }
                    break;
                }
                case Constantes.AUTRES: {
                    switch (modele.getElement().getDesignation()) {
                        case Constantes.DEPOT: {
                            YvsBaseDepots p = dao.findOneByNQ("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{id});
                            if (p != null && p.getId() > 0) {
                                code = p.getAbbreviation();
                            }
                            break;
                        }
                        case Constantes.POINTVENTE: {
                            YvsBasePointVente p = dao.findOneByNQ("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{id});
                            if (p != null && p.getId() > 0) {
                                code = p.getCode();
                            }
                            break;
                        }
                        case Constantes.CAISSE: {
                            break;
                        }
                        default: {
                            if (agence != null && !agence.getSociete().getCodeAbreviation().trim().isEmpty()) {
                                code = agence.getSociete().getCodeAbreviation();
                            }
                            break;
                        }
                    }
                }
            }
            if (code.length() > modele.getLongueurCodePoint()) {
                return code.substring(0, modele.getLongueurCodePoint());
            } else {
                return code;
            }
        }
        return "";
    }

    public String genererPrefixeComplet(YvsBaseModeleReference modele, Date date, long id, String type, String code, YvsAgences agence) {
        String prefixe = genererPrefixe(modele, id, type, code, agence);
        if (prefixe != null && !prefixe.trim().isEmpty()) {
            Calendar cal = Constantes.dateToCalendar(date);
            if (Boolean.TRUE.equals(modele.getJour())) {
                if (cal.get(Calendar.DATE) > 9) {
                    prefixe += Integer.toString(cal.get(Calendar.DATE));
                }
                if (cal.get(Calendar.DATE) < 10) {
                    prefixe += ("0" + cal.get(Calendar.DATE));
                }
            }
            if (Boolean.TRUE.equals(modele.getMois())) {
                if (cal.get(Calendar.MONTH) + 1 > 9) {
                    prefixe += Integer.toString(cal.get(Calendar.MONTH) + 1);
                }
                if (cal.get(Calendar.MONTH) + 1 < 10) {
                    prefixe += ("0" + (cal.get(Calendar.MONTH) + 1));
                }
            }
            if (Boolean.TRUE.equals(modele.getAnnee())) {
                prefixe += Integer.toString(cal.get(Calendar.YEAR)).substring(2);
            }
            prefixe += modele.getSeparateur();
        }
        return prefixe != null ? prefixe : "";
    }

    public double arrondi(double d, YvsSocietes societe) {
        return dao.arrondi(societe != null ? societe.getId() : 0, d);
    }

    public double setMontantTotalDoc(YvsComDocVentes doc, List<YvsComContenuDocVente> lc) {
        doc.setMontantRemise(0);
        doc.setMontantTaxe(0);
        doc.setMontantRistourne(0);
        doc.setMontantCommission(0);
        doc.setMontantHT(0);
        doc.setMontantTTC(0);
        doc.setMontantRemises(0);
        doc.setMontantCS(0);
        doc.setMontantAvance(0.0);
        doc.setMontantTaxeR(0);
        doc.setMontantResteApayer(0);
        doc.setMontantPlanifier(0);
        if (lc != null && !lc.isEmpty()) {
            for (YvsComContenuDocVente c : lc) {
                doc.setMontantRemise(doc.getMontantRemise() + c.getRemise());
                doc.setMontantRistourne(doc.getMontantRistourne() + c.getRistourne());
                doc.setMontantCommission(doc.getMontantCommission() + c.getComission());
                doc.setMontantTTC(doc.getMontantTTC() + c.getPrixTotal());
                doc.setMontantTaxe(doc.getMontantTaxe() + c.getTaxe());
                doc.setMontantTaxeR(doc.getMontantTaxeR() + (Boolean.TRUE.equals((c.getArticle().getPuvTtc())) ? (c.getTaxe()) : 0));
            }
        }


        String query = "SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_vente y WHERE y.vente=? AND y.statut_piece=? AND COALESCE(y.mouvement,'R')='R'";
        Double a = (Double) dao.findOneObjectBySQLQ(query, new Options[]{new Options(doc.getId(), 1), new Options(Constantes.STATUT_DOC_PAYER, 2)});
        doc.setMontantAvance(a != null ? a : 0);
        query = "SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_vente y WHERE y.vente=? AND y.statut_piece!=? AND COALESCE(y.mouvement,'R')='R'";
        a = (Double) dao.findOneObjectBySQLQ(query, new Options[]{new Options(doc.getId(), 1), new Options(Constantes.STATUT_DOC_PAYER, 2)});
        doc.setMontantPlanifier(a != null ? a : 0);
        YvsSocietes scte = UtilsProject.currentSociete;
        doc.setMontantRemise(arrondi(doc.getMontantRemise(), scte));
        doc.setMontantTaxe(arrondi(doc.getMontantTaxe(), scte));
        doc.setMontantRistourne(arrondi(doc.getMontantRistourne(), scte));
        doc.setMontantCommission(arrondi(doc.getMontantCommission(), scte));
        doc.setMontantHT(arrondi(doc.getMontantHT(), scte));
        doc.setMontantTTC(arrondi(doc.getMontantTTC(), scte));
        doc.setMontantRemises(arrondi(doc.getMontantRemises(), scte));
        doc.setMontantCS(arrondi(doc.getMontantCS(), scte));
        doc.setMontantAvance(arrondi(doc.getMontantAvance(), scte));
        doc.setMontantTaxeR(arrondi(doc.getMontantTaxeR(), scte));
        doc.setMontantResteApayer(arrondi(doc.getMontantResteApayer(), scte));
        return doc.getMontantTotal();
    }

    public static double calculeVersementAttendu(long idCaisse) {
        double re = 0;
        // le versement attendu peut correspondre au solde de la caisse du vendeur

        return getTotalCaisse(0, idCaisse, 0, null, "R", "ESPECE,BANQUE", Constantes.STATUT_DOC_PAYER, new Date())
                - getTotalCaisse(0, idCaisse, 0, null, "D", "ESPECE,BANQUE", Constantes.STATUT_DOC_PAYER, new Date());
    }

    public static double getTotalCaisse(long societe, long caisse, long mode, String table, String mouvement, String type, Character statut, Date date) {
        String query = "select public.compta_total_caisse(?,?,?,?,?,?,?,?)";
        LocalQueryFactories rq = new LocalQueryFactories();
        Options[] options = new Options[]{new Options(societe, 1), new Options(caisse, 2), new Options(mode, 3), new Options(table, 4),
                new Options(mouvement, 5), new Options(type, 6), new Options(statut, 7), new Options(new Date(), 8)};
        Double re = (Double) rq.findOneObjectBySQLQ(query, options);
        return re != null ? re : 0;
    }

    public static double getTotalFacturesHeader(long header) {
        String query = "select public.get_ca_entete_vente(?)";
        LocalQueryFactories rq = new LocalQueryFactories();
        Options[] options = new Options[]{new Options(header, 1)};
        Double re = (Double) rq.findOneObjectBySQLQ(query, options);
        return re != null ? re : 0;
    }

    public static double getVersementAttenduHeader(long header) {
        String query = "select public.com_get_versement_attendu(?)";
        LocalQueryFactories rq = new LocalQueryFactories();
        Options[] options = new Options[]{new Options("" + header, 1)};
        Double re = (Double) rq.findOneObjectBySQLQ(query, options);
        return re != null ? re : 0;
    }

    public static double getTotalCommandeHeader(long header) {
        String champ[] = new String[]{"header", "typeDoc"};
        LocalQueryFactories rq = new LocalQueryFactories();
        Object val[] = new Object[]{new YvsComEnteteDocVente(header), Constantes.TYPE_BCV};
        Double re = (Double) rq.findOneObjectByNQ("YvsComContenuDocVente.findTotalByTypeDocAndHeader", champ, val);
        return re != null ? re : 0;
    }

    public static double getCommandeRecu(long user, Date date) {
        String query = "SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_vente y "
                + "                         INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id "
                + "  WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) "
                + "     AND d.statut = 'V' AND y.statut_piece = 'P' AND y.caissier = ? "
                + "     AND y.date_paiement BETWEEN ? AND ?";
        LocalQueryFactories rq = new LocalQueryFactories();
        Options[] options = new Options[]{new Options(user, 1), new Options(date, 2), new Options(date, 3)};
        Double re = (Double) rq.findOneObjectBySQLQ(query, options);
        return re != null ? re : 0;
    }
}
