package com.lymytz.lymytzsell.service.application.service;

import com.lymytz.lymytzsell.business.helpers.ResponseAction;
import com.lymytz.lymytzsell.business.helpers.StatutResponse;
import com.lymytz.lymytzsell.persistence.entity.YvsComClient;
import com.lymytz.lymytzsell.persistence.entity.YvsComDocVentes;
import com.lymytz.lymytzsell.persistence.entity.YvsComEnteteDocVente;
import com.lymytz.lymytzsell.persistence.entity.YvsDictionnaire;
import com.lymytz.lymytzsell.service.utils.Constantes;
import com.lymytz.lymytzsell.service.utils.UtilsProject;

import java.util.Date;
import java.util.function.Function;

public class ManagedFactureVente {
    private final YvsComEnteteDocVente headerDoc;
    private final YvsDictionnaire adresse;
    private final YvsComClient client;
    private final String nameClient;
    private final String typeDoc;
    private final Date dateLiv;
    private final String telephone;


    private final Function<YvsComDocVentes, StatutResponse> controle = docVente -> {
        if (docVente.getEnteteDoc() == null) return StatutResponse.ENTETE_FACTURE_NON_TROUVE;
        if (Boolean.TRUE.equals(docVente.getEnteteDoc().getCloturer())) return StatutResponse.FICHE_DEJA_CLOTURE;
        if (docVente.getEnteteDoc().getDateEntete().after(new Date())) return StatutResponse.DATE_FICHE_INCORRECT;
        if (docVente.getNumDoc() == null) return StatutResponse.NUMERO_DOC_NON_GENERE;
        return StatutResponse.OK;
    };
    public static final Function<YvsComDocVentes, StatutResponse> controleBeforeSaveFacture = docVente -> {
        if (docVente.getEnteteDoc() == null) return StatutResponse.ENTETE_FACTURE_NON_TROUVE;
        if (Boolean.TRUE.equals(docVente.getEnteteDoc().getCloturer())) return StatutResponse.FICHE_DEJA_CLOTURE;
        if (docVente.getEnteteDoc().getDateEntete().after(new Date())) return StatutResponse.DATE_FICHE_INCORRECT;
        if (docVente.getNumDoc() == null) return StatutResponse.NUMERO_DOC_NON_GENERE;
        return StatutResponse.OK;
    };

    public ManagedFactureVente(YvsComEnteteDocVente headerDoc, YvsDictionnaire adresse, YvsComClient client, String nameClient, String typeDoc, Date dateLiv, String telephone) {
        this.headerDoc = headerDoc;
        this.adresse = adresse;
        this.client = client;
        this.nameClient = nameClient;
        this.typeDoc = typeDoc;
        this.dateLiv = dateLiv;
        this.telephone = telephone;
    }

    public ResponseAction<YvsComDocVentes> createNonPersistFacture(String numDoc) {
        if(UtilsProject.headerDoc==null){
            return new ResponseAction<>(null, StatutResponse.ENTETE_FACTURE_NON_TROUVE);
        }
        var facture = buildEntityFacture(numDoc);
        var statut = controle.apply(facture);
        return new ResponseAction<>(facture, statut);
    }

    private YvsComDocVentes buildEntityFacture(String numDoc) {
        YvsComDocVentes bean = new YvsComDocVentes();
        bean.setEnteteDoc(this.headerDoc);
        bean.setNumDoc(numDoc);
        bean.setLivraisonAuto(true);
        bean.setAdresse(adresse);
        bean.setAuthor(UtilsProject.currentUser);
        bean.setCategorieComptable(client.getCategorieComptable());
        bean.setClient(client);
        bean.setCloturer(Boolean.FALSE);
        bean.setCommision(0d);
        bean.setDateSave(new Date());
        bean.setDateUpdate(new Date());
        bean.setDateSolder(new Date());
        bean.setDepotLivrer(UtilsProject.depotLivraison);
        bean.setTrancheLivrer(UtilsProject.headerDoc.getCreneau().getCreneauDepot().getTranche());
        bean.setEnteteDoc(UtilsProject.headerDoc);
        bean.setEtapeTotal(1);
        bean.setHeureDoc(new Date());
        bean.setModelReglement(UtilsProject.modelReg);
        bean.setMouvStock(Boolean.TRUE);
        bean.setNomClient(nameClient);
        bean.setStatut(Constantes.ETAT_EDITABLE);
        bean.setStatutLivre(Constantes.ETAT_ATTENTE);
        bean.setStatutRegle(Constantes.ETAT_ATTENTE);
        bean.setTypeDoc(typeDoc);
        bean.setDateLivraisonPrevu(dateLiv);
        bean.setTelephone(telephone);
        bean.setOperateur(UtilsProject.currentUser.getUsers());
        bean.setNature(Constantes.NATURE_DOC_VENTE_VENTE);
        if (Boolean.TRUE.equals(client.getSuiviComptable())) {
            bean.setTiers(client);
        }
        bean.setId(UtilsProject.localId.incrementAndGet());
        return bean;
    }
}
