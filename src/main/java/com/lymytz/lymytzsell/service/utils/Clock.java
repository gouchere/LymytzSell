package com.lymytz.lymytzsell.service.utils;

import com.lymytz.lymytzsell.business.helpers.Helpers;
import javafx.concurrent.Task;

import java.util.Date;
import java.util.function.Consumer;

public class Clock extends Task<Long> {
    long time = System.currentTimeMillis();
    Date d;
    Consumer<Long> consumer;

    public Clock(Consumer<Long> consumer) {
        this.consumer = consumer;
    }

    @Override
    protected Long call() throws Exception {
        while (true) {
            long duree = System.currentTimeMillis() - time - 3600000;
            d = new Date(duree);
            consumer.accept(duree);
            Helpers.sleepFor(1000);
        }
    }
}
