/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lymytz.dao.entity.YvsAgences;
import lymytz.dao.entity.YvsBaseDepots;
import lymytz.dao.entity.YvsBaseModeleReference;
import lymytz.dao.entity.YvsBasePointVente;
import lymytz.dao.entity.YvsComContenuDocVente;
import lymytz.dao.entity.YvsComDocVentes;
import lymytz.dao.entity.YvsComEnteteDocVente;
import lymytz.dao.entity.YvsComptaCaissePieceVente;
import lymytz.dao.entity.YvsSocietes;
import lymytz.dao.query.LQueryFactories;
import lymytz.service.utils.Constantes;
import lymytz.service.utils.UtilsProject;

/**
 *
 * @author LENOVO
 */
public class UtilsBean {

    LQueryFactories dao = new LQueryFactories();


    /*Générer les références des documents*/
    private YvsBaseModeleReference rechercheModeleReference(String mot, YvsAgences agence) {
        if (!mot.toLowerCase().equals("")) {
            String[] ch = new String[]{"designation"};
            Object[] v = new Object[]{mot};
            String query = "YvsBaseModeleReference.findByElement";
            YvsBaseModeleReference l = (YvsBaseModeleReference) dao.findOneByNQ(query, ch, v);
            return l;
        }
        return null;
    }

    public String genererReference(String element, Date date, long id, String type, String code, YvsAgences agence) {
        YvsBaseModeleReference model = rechercheModeleReference(element, agence);

        if ((model != null) ? model.getId() > 0 : false) {
            return getReferenceElement(model, date, id, type, code, agence);
        } else {
            return "";
        }

    }

    private String getReferenceElement(YvsBaseModeleReference modele, Date date, long id, String type, String code, YvsAgences agence) {
        String motRefTable = "";
        String inter = genererPrefixeComplet(modele, date, id, type, code, agence);
        switch (modele.getElement().getDesignation()) {
            case "Employe": {
                break;
            }
            case Constantes.TYPE_BLV_NAME:
            case Constantes.TYPE_BRV_NAME:
            case Constantes.TYPE_BAV_NAME:
            case Constantes.TYPE_BCV_NAME:
            case Constantes.TYPE_FRV_NAME:
            case Constantes.TYPE_FAV_NAME:
            case Constantes.TYPE_FV_NAME: {
                String[] ch = new String[]{"numDoc"};
                Object[] v = new Object[]{inter + "%"};
                String query = "YvsComDocVentes.findByReference";
                List<YvsComDocVentes> l = dao.loadByNamedQuery(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroExterne();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_NAME: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", UtilsProject.currentSociete};
                List<YvsComptaCaissePieceVente> l = dao.loadByNamedQuery("YvsComptaCaissePieceVente.findByNumeroPiece", ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }

            default: {
                break;
            }
        }
        String partieNum = motRefTable.replaceFirst(inter, "");
        if (partieNum != null ? partieNum.trim().length() > 0 : false) {
            int num = Integer.valueOf(partieNum.trim().replace("°", ""));
            if (Integer.toString(num + 1).length() > modele.getTaille()) {
                return "";
            } else {
                for (int i = 0; i < (modele.getTaille() - Integer.toString(num + 1).length()); i++) {
                    inter += "0";
                }
            }
            inter += Long.toString(Long.valueOf(partieNum.trim().replace("°", "")) + 1);
        } else {
            for (int i = 0; i < modele.getTaille() - 1; i++) {
                inter += "0";
            }
            inter += "1";
        }
        return inter;
    }

    public String genererPrefixe(String element, long id, String type, String code, YvsAgences agence) {
        YvsBaseModeleReference modele = rechercheModeleReference(element, agence);
        if ((modele != null) ? modele.getId() > 0 : false) {
            return genererPrefixe(modele, id, type, code, agence);
        }
        return "";
    }

    public String genererPrefixe(YvsBaseModeleReference modele, long id, String type, String code, YvsAgences agence) {
        String inter = modele.getPrefix();
        if (id > 0 && type != null) {
            code = genererPrefixe(modele, id, type, agence);
        }
        if (code != null ? code.trim().length() > 0 : false) {
            inter += modele.getSeparateur() + code;
        }
        inter += modele.getSeparateur();
        return inter != null ? inter : "";
    }

    public String genererPrefixe(YvsBaseModeleReference modele, long id, String type, YvsAgences agence) {
        if (modele.getCodePoint()) {
            String code = "";
            switch (modele.getElementCode()) {

                case Constantes.SOCIETE: {
                    if (agence != null ? agence.getSociete().getCodeAbreviation().trim().length() > 0 : false) {
                        code = agence.getSociete().getCodeAbreviation();
                    }
                    break;
                }
                case Constantes.AGENCE: {
                    if (agence != null ? Constantes.asString(agence.getAbbreviation()) : false) {
                        code = agence.getAbbreviation();
                    }
                    break;
                }
                case Constantes.AUTRES: {
                    switch (modele.getElement().getDesignation()) {
                        case Constantes.DEPOT: {
                            YvsBaseDepots p = (YvsBaseDepots) dao.findOneByNQ("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{id});
                            if (p != null ? p.getId() > 0 : false) {
                                code = p.getAbbreviation();
                            }
                            break;
                        }
                        case Constantes.POINTVENTE: {
                            YvsBasePointVente p = (YvsBasePointVente) dao.findOneByNQ("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{id});
                            if (p != null ? p.getId() > 0 : false) {
                                code = p.getCode();
                            }
                            break;
                        }
                        case Constantes.CAISSE: {
//                    YvsMutCaisse en = (YvsMutCaisse) dao.loadObjectByNameQueries("YvsMutCaisse.findById", new String[]{"id"}, new Object[]{id});
//                    if ((en != null ? en.getId() > 0 : false) && currentMutuel != null) {
//                        if (en.getReferenceCaisse().length() > modele.getLongueurCodePoint()) {
//                            modele.setCodePointvente(currentMutuel.getCode() + "/" + en.getReferenceCaisse().substring(0, modele.getLongueurCodePoint()));
//                        } else {
//                            modele.setCodePointvente(currentMutuel.getCode() + "/" + en.getReferenceCaisse());
//                        }
//                    }
//                    return modele.getCodePointvente();
                        }
//                case Constantes.TYPECREDIT: {
//                    YvsMutTypeCredit p = (YvsMutTypeCredit) dao.loadObjectByNameQueries("YvsMutTypeCredit.findById", new String[]{"id"}, new Object[]{id});
//                    if ((p != null ? p.getId() > 0 : false) && currentMutuel != null) {
//                        if (p.getCode().length() > modele.getLongueurCodePoint()) {
//                            modele.setCodePointvente(currentMutuel.getCode() + "/" + p.getCode().substring(0, modele.getLongueurCodePoint()));
//                        } else {
//                            modele.setCodePointvente(currentMutuel.getCode() + "/" + p.getCode());
//                        }
//                    }
//                    return modele.getCodePointvente();
//                }
                        default: {
                            if (agence != null ? agence.getSociete().getCodeAbreviation().trim().length() > 0 : false) {
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
        return modele.getPrefix();
    }

    public String genererPrefixeComplet(YvsBaseModeleReference modele, Date date, long id, String type, String code, YvsAgences agence) {
        String prefixe = genererPrefixe(modele, id, type, code, agence);
        if (prefixe != null ? prefixe.trim().length() > 0 : false) {
            Calendar cal = Constantes.dateToCalendar(date);
            if (modele.getJour()) {
                if (cal.get(Calendar.DATE) > 9) {
                    prefixe += Integer.toString(cal.get(Calendar.DATE));
                }
                if (cal.get(Calendar.DATE) < 10) {
                    prefixe += ("0" + Integer.toString(cal.get(Calendar.DATE)));
                }
            }
            if (modele.getMois()) {
                if (cal.get(Calendar.MONTH) + 1 > 9) {
                    prefixe += Integer.toString(cal.get(Calendar.MONTH) + 1);
                }
                if (cal.get(Calendar.MONTH) + 1 < 10) {
                    prefixe += ("0" + Integer.toString(cal.get(Calendar.MONTH) + 1));
                }
            }
            if (modele.getAnnee()) {
                prefixe += Integer.toString(cal.get(Calendar.YEAR)).substring(2);
            }
            prefixe += modele.getSeparateur();
        }
        return prefixe != null ? prefixe : "";
    }

    public static boolean checkOperationArticle(long article, long depot, String operation) {
//        Requete rq = new Requete();
//        if (depot > 0) {
//            String[] champ = new String[]{"depot", "article"};
//            Object[] val = new Object[]{new YvsBaseDepots(depot), new YvsBaseArticles(article)};
//            String nameQueri = "YvsBaseArticleDepot.findByArticleDepot";
//            List<YvsBaseArticleDepot> l = rq.loadNameQueries(nameQueri, champ, val, 0, 1);
//            if (l != null ? !l.isEmpty() : false) {
//                YvsBaseArticleDepot a = l.get(0);
//                if (a.getModeAppro() != null) {
//                    switch (operation) {
//                        case Constantes.ACHAT: {
//                            switch (a.getModeAppro()) {
//                                case Constantes.APPRO_ACHTON:
//                                case Constantes.APPRO_ACHT_EN:
//                                case Constantes.APPRO_ACHT_PROD:
//                                case Constantes.APPRO_ACHT_PROD_EN:
//                                    return true;
//                                default:
//                                    return false;
//                            }
//                        }
//                        case Constantes.ENTREE: {
//                            switch (a.getModeAppro()) {
//                                case Constantes.APPRO_ENON:
//                                case Constantes.APPRO_ACHT_EN:
//                                case Constantes.APPRO_PROD_EN:
//                                case Constantes.APPRO_ACHT_PROD_EN:
//                                    return true;
//                                default:
//                                    return false;
//                            }
//                        }
//                        case Constantes.PRODUCTION: {
//                            switch (a.getModeAppro()) {
//                                case Constantes.APPRO_PRODON:
//                                case Constantes.APPRO_ACHT_PROD:
//                                case Constantes.APPRO_PROD_EN:
//                                case Constantes.APPRO_ACHT_PROD_EN:
//                                    return true;
//                                default:
//                                    return false;
//                            }
//                        }
//                        default:
//                            return false;
//                    }
//                } else {
//                    return true;
//                }
//            }
//        } else {
//            return true;
//        }
        return true;
    }

    public static boolean checkOperationDepot(long depot, String type) {
//        Requete rq = new Requete();
//        String[] champ = new String[]{"depot", "type"};
//        Object[] val = new Object[]{new YvsBaseDepots(depot), type};
//        String nameQueri = "YvsBaseDepotOperation.findByDepotType";
//        List<YvsBaseDepotOperation> l = rq.loadNameQueries(nameQueri, champ, val, 0, 1);
//        return l != null ? !l.isEmpty() : false;
        return true;
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
        if (lc != null ? !lc.isEmpty() : false) {
            for (YvsComContenuDocVente c : lc) {
                doc.setMontantRemise(doc.getMontantRemise() + c.getRemise());
                doc.setMontantRistourne(doc.getMontantRistourne() + c.getRistourne());
                doc.setMontantCommission(doc.getMontantCommission() + c.getComission());
                doc.setMontantTTC(doc.getMontantTTC() + c.getPrixTotal());
                doc.setMontantTaxe(doc.getMontantTaxe() + c.getTaxe());
                doc.setMontantTaxeR(doc.getMontantTaxeR() + ((c.getArticle().getPuvTtc()) ? (c.getTaxe()) : 0));
            }
        }

        String[] champ = new String[]{"facture", "statut"};
        Object[] val = new Object[]{doc, Constantes.STATUT_DOC_PAYER};
        String nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutS";
        String query = "SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_vente y WHERE y.vente=? AND y.statut_piece=? AND COALESCE(y.mouvement,'R')='R'";
//        Double a = (Double) dao.findOneObjectByNQ(nameQueri, champ, val);
        Double a = (Double) dao.findOneObjectBySQLQ(query, new Options[]{new Options(doc.getId(), 1), new Options(Constantes.STATUT_DOC_PAYER, 2)});
        doc.setMontantAvance(a != null ? a : 0);
        val = new Object[]{doc, Constantes.STATUT_DOC_SUSPENDU};
        nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutSDiff";
        query = "SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_vente y WHERE y.vente=? AND y.statut_piece!=? AND COALESCE(y.mouvement,'R')='R'";
//        a = (Double) dao.findOneObjectByNQ(nameQueri, champ, val);
        a = (Double) dao.findOneObjectBySQLQ(query, new Options[]{new Options(doc.getId(), 1), new Options(Constantes.STATUT_DOC_PAYER, 2)});
        doc.setMontantPlanifier(a != null ? a : 0);

//        champ = new String[]{"docVente", "sens"};
//        val = new Object[]{doc, true};
//        Double p = (Double) dao.findOneObjectByNQ("YvsComCoutSupDocVente.findSumByDocVente", champ, val);
//        val = new Object[]{doc, false};
//        Double m = (Double) dao.findOneObjectByNQ("YvsComCoutSupDocVente.findSumByDocVente", champ, val);
//        double s = (p != null ? p : 0) - (m != null ? m : 0);
//        doc.setMontantCS(s);
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
        LQueryFactories rq = new LQueryFactories();
        Options[] options = new Options[]{new Options(societe, 1), new Options(caisse, 2), new Options(mode, 3), new Options(table, 4),
            new Options(mouvement, 5), new Options(type, 6), new Options(statut, 7), new Options(new Date(), 8)};
        Double re = (Double) rq.findOneObjectBySQLQ(query, options);
        return re != null ? re : 0;
    }

    public static double getTotalFacturesHeader(long header) {
        String query = "select public.get_ca_entete_vente(?)";
        LQueryFactories rq = new LQueryFactories();
        Options[] options = new Options[]{new Options(header, 1)};
        Double re = (Double) rq.findOneObjectBySQLQ(query, options);
        return re != null ? re : 0;
    }

    public static double getVersementAttenduHeader(long header) {
        String query = "select public.com_get_versement_attendu(?)";
        LQueryFactories rq = new LQueryFactories();
        Options[] options = new Options[]{new Options("" + header, 1)};
        Double re = (Double) rq.findOneObjectBySQLQ(query, options);
        return re != null ? re : 0;
    }

    public static double getTotalCommandeHeader(long header) {
        String champ[] = new String[]{"header", "typeDoc"};
        LQueryFactories rq = new LQueryFactories();
        Object val[] = new Object[]{new YvsComEnteteDocVente(header), Constantes.TYPE_BCV};
        Double re = (Double) rq.findOneObjectByNQ("YvsComContenuDocVente.findTotalByTypeDocAndHeader", champ, val);
        return re != null ? re : 0;
    }
}
