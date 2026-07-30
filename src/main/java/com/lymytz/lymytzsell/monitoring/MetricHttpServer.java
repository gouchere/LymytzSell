package com.lymytz.lymytzsell.monitoring;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

@Getter
public class MetricHttpServer {
    private final Logger LOGGER = LogManager.getLogger(MetricHttpServer.class.getName());
    private final PrometheusMeterRegistry meterRegistry;

    public MetricHttpServer() {
        this.meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        new MetricsConfig(this.meterRegistry);
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(9099), 0);
        server.createContext("/metrics", exchange -> {
            String response = meterRegistry.scrape();
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.length());
            try(OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        LOGGER.info("Starting Prometheus metrics server at {}", server.getAddress());
        server.start();
    }

}
