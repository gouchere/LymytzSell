package com.lymytz.lymytzsell.synchro.ws;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lymytz.lymytzsell.service.application.config.Properties;
import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import com.lymytz.lymytzsell.synchro.ws.dto.DeliveryResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;

public abstract class AbstractHttpService {
    private static final Logger LOGGER = LogManager.getLogger(AbstractHttpService.class);

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final WebTarget webTarget;
    private static final String PATH_ROOT = "/Lymytz_Web/ws/services/";

    public AbstractHttpService() {
        try {
            Properties properties = PropertiesManager.getInstance().getProperties();
            String serviceUrl = properties.getHostWeb();
            String servicePort = properties.getPortWeb();
            Client httpClient = ClientBuilder.newClient(new ClientConfig());
            String baseUrl = serviceUrl.matches("https?://.*") ? serviceUrl : "http://" + serviceUrl;
            webTarget = httpClient.target(baseUrl + ":" + servicePort + PATH_ROOT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize http rest client: " + e.getMessage(), e);
        }
    }

    protected WebTarget getWebTarget() {
        return webTarget;
    }

   protected <T extends Serializable> ResultatAction<T> genericHandleResponse(Response response) {
       if (response.getStatus() < 200 || response.getStatus() >= 300) {
           String body = response.readEntity(String.class);
           LOGGER.error("Unexpected HTTP {} response {}", response.getStatus(), body);
           return new ResultatAction<>(false, "Erreur HTTP " + response.getStatus());
       }
       try {
           String body = response.readEntity(String.class);
           JavaType type = MAPPER.getTypeFactory()
                   .constructParametricType(ResultatAction.class, DeliveryResponseDto.class);
           return MAPPER.readValue(body, type);
       } catch (Exception e) {
           LOGGER.error("Error deserializing delivery response: {}", e.getMessage());
           return new ResultatAction<>(false, "Erreur de désérialisation de la réponse");
       }
    }

}
