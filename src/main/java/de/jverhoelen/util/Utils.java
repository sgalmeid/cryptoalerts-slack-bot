package de.jverhoelen.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static double roundSmartly(double value) {
        if (value > 1) {
            return round(value, 3);
        } else if (value > 0.01) {
            return round(value, 5);
        } else if (value > 0.001) {
            return round(value, 6);
        } else {
            return value;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static URI getUri(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static void threadSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("Letting the thread sleep went wrong", e);
        }
    }
}
