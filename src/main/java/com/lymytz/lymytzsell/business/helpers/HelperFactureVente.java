package com.lymytz.lymytzsell.business.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lymytz.lymytzsell.business.dto.ArticleDto;
import com.lymytz.lymytzsell.business.dto.ClientDto;
import com.lymytz.lymytzsell.business.dto.ConditionnementDto;
import com.lymytz.lymytzsell.business.dto.ContentFactureDto;
import com.lymytz.lymytzsell.business.dto.FactureDto;
import com.lymytz.lymytzsell.dao.entity.YvsBaseArticles;
import com.lymytz.lymytzsell.dao.entity.YvsComClient;
import com.lymytz.lymytzsell.dao.entity.YvsComContenuDocVente;
import com.lymytz.lymytzsell.dao.entity.YvsComCreneauHoraireUsers;
import com.lymytz.lymytzsell.dao.entity.YvsComDocVentes;
import com.lymytz.lymytzsell.dao.entity.YvsComEnteteDocVente;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.lymytz.lymytzsell.service.utils.Constantes.TYPE_FV;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperFactureVente {

    private static final Logger LOGGER = LogManager.getLogger(HelperFactureVente.class);

    public static EtatMontantPayer isValideMontantPaye(String typeDoc, double netApayer, double montantPaye) {
        if (TYPE_FV.equals(typeDoc)) {
            if (netApayer != montantPaye) {
                // erreur paiement insuffisant
                return EtatMontantPayer.KO_NET_FACTURE;
            }
        } else {
            // contrôle le montant d'avance
            if (montantPaye < netApayer) {
                return EtatMontantPayer.KO_NET_COMMANDE;
            }
        }
        return EtatMontantPayer.OK;
    }

    public static String getStringJsonFromEntity(FactureDto facture) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(facture);
        } catch (JsonProcessingException e) {
            LOGGER.error(e);
        }
        return null;
    }

    public static FactureDto factureDtoFromEntity(YvsComDocVentes facture) {
        return FactureDto.builder()
                .client(new ClientDto(facture.getClient().getId(), facture.getClient().getCodeClient(), facture.getNomClient(), facture.getTelephone()))
                .dateEntete(facture.getEnteteDoc().getDateEntete())
                .dateLivraisonPrevu(facture.getDateLivraisonPrevu())
                .idEntete(facture.getEnteteDoc().getId())
                .typeDoc(facture.getTypeDoc())
                .contentFacture(facture.getContenus().stream().map(HelperFactureVente::fromEntity).toList())
                .build();
    }

    private static ContentFactureDto fromEntity(YvsComContenuDocVente content) {
        return ContentFactureDto.builder()
                .prix(content.getPrix())
                .pr(content.getPr())
                .article(new ArticleDto(content.getArticle().getId(), content.getArticle().getRefArt(), content.getArticle().getDesignation()))
                .conditionnement(new ConditionnementDto(content.getConditionnement().getId(), content.getConditionnement().getUnite().getId(), content.getConditionnement().getUnite().getReference()))
                .puvMin(content.getPuvMin())
                .comission(content.getComission())
                .numSerie(content.getNumSerie())
                .remise(content.getRemise())
                .taxe(content.getTaxe())
                .prixTotal(content.getPrixTotal())
                .quantite(content.getQuantite())
                .quantiteBonus(content.getQuantiteBonus())
                .ristourne(content.getRistourne())
                .tauxRemise(content.getTauxRemise())
                .build();
    }

    private static YvsBaseArticles simplifyArticleToSave(YvsBaseArticles articles) {
        if (articles != null) {
            var art = new YvsBaseArticles();
            art.setId(articles.getId());
            art.setRefArt(articles.getRefArt());
            art.setDesignation(articles.getDesignation());
            return art;
        }
        return null;
    }

    private static YvsComEnteteDocVente simplifyHeaderToSave(YvsComEnteteDocVente docVente) {
        if (docVente != null) {
            var ent = new YvsComEnteteDocVente();
            ent.setId(docVente.getId());
            ent.setCloturer(docVente.getCloturer());
            ent.setCreneau(new YvsComCreneauHoraireUsers(docVente.getCreneau().getId()));
            return ent;
        }
        return null;
    }

}
