package com.lymytz.lymytzsell.business.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Helpers {

    public static void sleepFor(int timeMillis) {
        try {
            sleep(timeMillis);
        } catch (InterruptedException ex) {
            Logger.getLogger(Helpers.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
    }
}
