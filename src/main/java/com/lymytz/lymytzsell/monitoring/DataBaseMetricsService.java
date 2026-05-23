package com.lymytz.lymytzsell.monitoring;


import com.lymytz.lymytzsell.LymytzSellApplication;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

import java.util.concurrent.TimeUnit;

public class DataBaseMetricsService implements SessionCustomizer {
    private final PrometheusMeterRegistry meterRegistry;

    public DataBaseMetricsService() {
        this.meterRegistry = LymytzSellApplication.getMeterRegistry();
    }

    public void recordExecutionTime(Runnable sqlQuery) {
        //this.timer.record(sqlQuery);
    }

    @Override
    public void customize(Session session) throws Exception {
        session.getEventManager().addListener(new SessionEventAdapter() {
            @Override
            public void postExecuteQuery(SessionEvent event) {
                long startTime = (long) event.getQuery().getProperty("executionStartTime");
                long elapsedTime = System.nanoTime() - startTime;

                Timer.builder("jpa.query.execution.time")
                        .description("Time taken to execute JPA queries")
                        .tags("database", "lymytz_demo_0")
                        .register(meterRegistry)
                        .record(elapsedTime, TimeUnit.NANOSECONDS);
            }

            public void preExecuteQuery(SessionEvent event) {
                event.getQuery().setProperty("executionStartTime", System.nanoTime());
            }
        });

    }
}
