/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lymytz.lymytzsell.service.application.synchro;

import com.lymytz.lymytzsell.persistence.entity.YvsComCommercialVente;
import com.lymytz.lymytzsell.persistence.entity.YvsComContenuDocVente;
import com.lymytz.lymytzsell.persistence.entity.YvsComDocVentes;
import com.lymytz.lymytzsell.persistence.entity.YvsComEnteteDocVente;
import com.lymytz.lymytzsell.persistence.entity.YvsComptaAcompteClient;
import com.lymytz.lymytzsell.persistence.entity.YvsComptaCaissePieceVente;
import com.lymytz.lymytzsell.persistence.entity.YvsComptaNotifReglementVente;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.lymytz.lymytzsell.service.application.synchro.export.UtilExport.dao;

/**
 *
 * @author LYMYTZ
 */
public class BuilderEntitySynchro {

    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(BuilderEntitySynchro.class);

    public BuilderEntitySynchro() {
    }

    public static YvsComEnteteDocVente builderHeader(Long id, Long idListen) {
        try {
            YvsComEnteteDocVente entity = (YvsComEnteteDocVente) dao.findOneByNQ("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{id});
            if(entity==null){
                //désactiver la ligne listen
            }
            return entity;
        } catch (Exception ex) {
            LOGGER.error("", ex);
            Logger.getLogger(BuilderEntitySynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static YvsComDocVentes builderDocVente(Long id, Long idListen, boolean allContent) {
        try {
            YvsComDocVentes entity = (YvsComDocVentes) dao.findOneByNQ("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{id});
            if (entity != null) {
                if (allContent) {
                    //trouve les ids des contenus
                    entity.setContenus(dao.loadByNamedQuery("YvsComContenuDocVente.findByDocVente", new String[]{"docVente"}, new Object[]{entity}));
                    //trouve les ids des règlements
                    entity.setReglements(dao.loadByNamedQuery("YvsComptaCaissePieceVente.findByDocVente", new String[]{"docVente"}, new Object[]{entity}));
                }
                return entity;
            }
        } catch (Exception ex) {
            LOGGER.error("", ex);
            Logger.getLogger(BuilderEntitySynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static YvsComContenuDocVente builderContentDocVente(Long id, Long idListen) {
        YvsComContenuDocVente entity = (YvsComContenuDocVente) dao.findOneByNQ("YvsComContenuDocVente.findById", new String[]{"id"}, new Object[]{id});
        return entity;
    }

    public static YvsComCommercialVente builderComercialeVente(Long id, Long idListen) {
        YvsComCommercialVente entity = (YvsComCommercialVente) dao.findOneByNQ("YvsComCommercialVente.findById", new String[]{"id"}, new Object[]{id});
        return entity;
    }

    public static YvsComptaAcompteClient builderAcompteClient(Long id, Long idListen) {
        try {
            YvsComptaAcompteClient entity = (YvsComptaAcompteClient) dao.findOneByNQ("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{id});

            return entity;
        } catch (Exception ex) {
            LOGGER.error("", ex);
            Logger.getLogger(BuilderEntitySynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static YvsComptaCaissePieceVente builderPieceReglement(Long id, Long idListen) {
        try {
            YvsComptaCaissePieceVente entity = (YvsComptaCaissePieceVente) dao.findOneByNQ("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{id});
            return entity;
        } catch (Exception ex) {
            LOGGER.error("", ex);
            Logger.getLogger(BuilderEntitySynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static YvsComptaNotifReglementVente builderNotifReglement(Long id, Long idListen) {
        try {
            YvsComptaNotifReglementVente entity = (YvsComptaNotifReglementVente) dao.findOneByNQ("YvsComptaNotifReglementVente.findById", new String[]{"id"}, new Object[]{id});
            return entity;
        } catch (Exception ex) {
            LOGGER.error("", ex);
            Logger.getLogger(BuilderEntitySynchro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    public static void buildWaitingFile(YvsComContenuDocVente entity, Long idListen) {
//        if (entity != null) {
//            Long id_;
//            int idx = ExportService.waitingFile.indexOf(idListen);
//            if (idx <= 0) {
//                ExportService.waitingFile.add(new FileDeSynchro(idListen, Constantes.ETAT_ATTENTE, YvsComContenuDocVente.class));
//            } else {
//                if (ExportService.waitingFile.get(idx).getStatut().equals(Constantes.ETAT_TERMINE)) {
//                    ExportService.waitingFile.get(idx).setStatut(Constantes.ETAT_ATTENTE);
//                }
//            }
//            //trouve id_listen du header
//            id_ = UtilEntityBase.findIdListen(Constantes.TABLE_DOC_VENTE_CODE, entity.getDocVente().getId());
//            idx = ExportService.waitingFile.indexOf(new FileDeSynchro(id_));
//            if (idx <= 0) {
//                ExportService.waitingFile.add(new FileDeSynchro(id_, Constantes.ETAT_ATTENTE, YvsComDocVentes.class));
//            } else {
//                if (ExportService.waitingFile.get(idx).getStatut().equals(Constantes.ETAT_TERMINE)) {
//                    ExportService.waitingFile.get(idx).setStatut(Constantes.ETAT_ATTENTE);
//                }
//            }
//            for (YvsComTaxeContenuVente c : entity.getTaxes()) {
//                id_ = UtilEntityBase.findIdListen(Constantes.TABLE_TAXE_VENTE_CODE, c.getId());
//                idx = ExportService.waitingFile.indexOf(new FileDeSynchro(id_));
//                if (idx <= 0) {
//                    ExportService.waitingFile.add(new FileDeSynchro(id_, Constantes.ETAT_ATTENTE, YvsComTaxeContenuVente.class));
//                } else {
//                    if (ExportService.waitingFile.get(idx).getStatut().equals(Constantes.ETAT_TERMINE)) {
//                        ExportService.waitingFile.get(idx).setStatut(Constantes.ETAT_ATTENTE);
//                    }
//                }
//            }
//        }
//    }

}
