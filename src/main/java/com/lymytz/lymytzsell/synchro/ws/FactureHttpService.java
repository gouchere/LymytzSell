package com.lymytz.lymytzsell.synchro.ws;

import com.lymytz.lymytzsell.synchro.ws.dto.DeliveryRequestDto;
import com.lymytz.lymytzsell.synchro.ws.dto.DeliveryResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class FactureHttpService extends AbstractHttpService {
    private static final Logger LOGGER = LogManager.getLogger(FactureHttpService.class);

    private static final String PATH_ROOT = "commercial/v1/";

    public FactureHttpService() {
        super();
    }

    public ResultatAction<DeliveryResponseDto> sendDeliveryRequest(Long idFacture) {
        Invocation.Builder invocation = getWebTarget()
                .path(PATH_ROOT + "livrer_facture_vente_caisse")
                .request(MediaType.APPLICATION_JSON);
        try (Response response = invocation.post(Entity.json(new DeliveryRequestDto(idFacture)))) {
            if (response.getStatus() < 200 || response.getStatus() >= 300) {
                String body = response.readEntity(String.class);
                LOGGER.error("Unexpected HTTP {} response for facture id {}: {}", response.getStatus(), idFacture, body);
                return new ResultatAction<>(false, "Erreur HTTP " + response.getStatus() + " pour la facture " + idFacture);
            }
            return response.readEntity(new GenericType<>() {
            });
        } catch (Exception ex) {
            LOGGER.error("Error sending delivery request for facture id {}: {}", idFacture, ex.getMessage());
        }
        return new ResultatAction<>(false, "Failed to send delivery request for facture id " + idFacture);
    }

}
