package de.jverhoelen.util;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;

public class Calculations {

    private static final Logger LOGGER = LoggerFactory.getLogger(Calculations.class);

    public static String hmac512Digest(String msg, String keyString) {
        Mac shaMac;
        try {
            shaMac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(keyString.getBytes(), "HmacSHA512");

            shaMac.init(keySpec);
            final byte[] macData = shaMac.doFinal(msg.getBytes());
            return Hex.encodeHexString(macData);

        } catch (Exception e1) {
            LOGGER.error("Error while hmac512Digest");
        }
        return null;
    }

    public static URI getUri(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
