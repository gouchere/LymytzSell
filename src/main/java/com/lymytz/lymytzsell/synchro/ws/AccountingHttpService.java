package com.lymytz.lymytzsell.synchro.ws;

import com.lymytz.lymytzsell.synchro.ws.dto.DeliveryResponseDto;
import com.lymytz.lymytzsell.synchro.ws.dto.FactureAComptabiliserRequestDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AccountingHttpService extends AbstractHttpService {
    private static final Logger LOGGER = LogManager.getLogger(AccountingHttpService.class);

    private static final String PATH_ROOT = "compta/";

    public AccountingHttpService() {
        super();
    }

    public ResultatAction<DeliveryResponseDto> comptabilise(Long idFacture, long auteur) {
        Invocation.Builder invocation = getWebTarget()
                .path(PATH_ROOT + "comptabiliseVente")
                .request(MediaType.APPLICATION_JSON);
        try (Response response = invocation.post(Entity.json(new FactureAComptabiliserRequestDto(idFacture, auteur)))) {
            return genericHandleResponse(response);
        } catch (Exception ex) {
            LOGGER.error("Erreur lors du traitement de la requête de comptabilisation {}: {}", idFacture, ex.getMessage());
        }
        return new ResultatAction<>(false, "Echec de la requête de comptabilisation de la facture " + idFacture);
    }


}
