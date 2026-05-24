package com.lymytz.lymytzsell.synchro.ws;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lymytz.lymytzsell.synchro.ws.dto.DeliveryRequestDto;
import com.lymytz.lymytzsell.synchro.ws.dto.DeliveryResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;

public class LivrerFactureHttpService extends AbstractHttpService {
    private static final Logger LOGGER = LogManager.getLogger(LivrerFactureHttpService.class);

    private static final String PATH_ROOT = "commercial/v1/";

    /** Mapper configuré pour ignorer les propriétés inconnues de la réponse serveur. */
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public LivrerFactureHttpService() {
        super();
    }

    public ResultatAction<DeliveryResponseDto> livrer(Long idFacture) {
        Invocation.Builder invocation = getWebTarget()
                .path(PATH_ROOT + "livrer_facture_vente_caisse")
                .request(MediaType.APPLICATION_JSON);
        try (Response response = invocation.post(Entity.json(new DeliveryRequestDto(idFacture)))) {
            return genericHandleResponse(response);
        } catch (Exception ex) {
            LOGGER.error("Error sending delivery request for facture id {}: {}", idFacture, ex);
        }
        return new ResultatAction<>(false, "Failed to send delivery request for facture id " + idFacture);
    }
}
