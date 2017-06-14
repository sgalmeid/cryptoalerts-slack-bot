package de.jverhoelen.trade.history;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jverhoelen.interaction.FatalErrorEvent;
import de.jverhoelen.trade.Trade;
import de.jverhoelen.trade.TradeType;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.stream.Collectors;

import static de.jverhoelen.util.Utils.threadSleep;

@Service
public class TradeHistoryService {

    private static final String TRADING_API_URI = "https://poloniex.com/tradingApi";
    private static final String COMMAND = "returnTradeHistory";

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private ObjectMapper mapper = new ObjectMapper();

    public Map<String, List<Trade>> filterHistoryByType(Map<String, List<Trade>> history, TradeType type) {
        return history
                .entrySet().stream()
                .map(currencyCombi -> {
                    List<Trade> filteredTrades = currencyCombi.getValue().stream()
                            .filter(trade -> trade.getType().equals(type))
                            .collect(Collectors.toList());

                    return new AbstractMap.SimpleEntry<>(currencyCombi.getKey(), filteredTrades);
                })
                .filter(currencyCombi -> currencyCombi.getValue().size() > 0)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public Map<String, List<Trade>> getTradeHistoryOf(String apiKey, String secretKey, int timeFrameMinutes) throws Exception {
        threadSleep(1000);

        long currentUnixTimestamp = System.currentTimeMillis() / 1000;
        long startUnixTimestamp = currentUnixTimestamp - (timeFrameMinutes * 60);

        try {
            String nonce = String.valueOf(System.currentTimeMillis());
            String queryArgs = "command=" + COMMAND + "&nonce=" + nonce + "&currencyPair=all&start=" + startUnixTimestamp;

            Mac shaMac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
            shaMac.init(keySpec);
            final byte[] macData = shaMac.doFinal(queryArgs.getBytes());
            String sign = Hex.encodeHexString(macData);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(TRADING_API_URI);
            post.addHeader("Key", apiKey);
            post.addHeader("Sign", sign);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("command", COMMAND));
            params.add(new BasicNameValuePair("nonce", nonce));
            params.add(new BasicNameValuePair("currencyPair", "all"));
            params.add(new BasicNameValuePair("start", startUnixTimestamp + ""));
            post.setEntity(new UrlEncodedFormEntity(params));

            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity responseEntity = response.getEntity();
            String resultJson = EntityUtils.toString(responseEntity);

            if (resultJson.equals("[]")) {
                return new HashMap<>();
            }

            return mapper.readValue(resultJson, new TypeReference<Map<String, List<Trade>>>() {
            });
        } catch (Exception e) {
            eventPublisher.publishEvent(new FatalErrorEvent("Retrieval of trade history for an account was not successful", e));
            throw new Exception();
        }
    }
}
