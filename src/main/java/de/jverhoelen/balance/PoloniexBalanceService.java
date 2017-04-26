package de.jverhoelen.balance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PoloniexBalanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoloniexBalanceService.class);
    private static final String TRADING_API_URI = "https://poloniex.com/tradingApi";

    @Value("${poloniex.apiKey}")
    private String apiKey;

    @Value("${poloniex.secretKey}")
    private String secretKey;

    private ObjectMapper mapper = new ObjectMapper();

    public double getBtcSumOfBalances(Map<String, Balance> currencyBalances) {
        return currencyBalances.entrySet().stream()
                .map(b -> b.getValue().getBtcValue())
                .reduce(0.0, Double::sum);
    }

    public Map<String, Balance> getBalancesOf(String apiKey, String secretKey) {
        try {
            String nonce = String.valueOf(System.currentTimeMillis());
            String queryArgs = "command=returnCompleteBalances&nonce=" + nonce;

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
            params.add(new BasicNameValuePair("command", "returnCompleteBalances"));
            params.add(new BasicNameValuePair("nonce", nonce));
            post.setEntity(new UrlEncodedFormEntity(params));

            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity responseEntity = response.getEntity();
            String resultJson = EntityUtils.toString(responseEntity);

            return mapper.readValue(resultJson, new TypeReference<Map<String, Balance>>() {
            });
        } catch (Exception e) {
            return null;
        }
    }
}
