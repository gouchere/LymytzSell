package com.lymytz.lymytzsell.synchro.ws;

import com.lymytz.lymytzsell.service.application.config.Properties;
import com.lymytz.lymytzsell.service.application.config.PropertiesManager;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public abstract class AbstractHttpService {
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

    public <T> T post(String path, Object requestBody, Class<T> response) {
        // send http request and return response
        return null;
    }

}
