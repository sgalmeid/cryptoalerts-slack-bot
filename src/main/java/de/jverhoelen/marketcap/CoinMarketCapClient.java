package de.jverhoelen.marketcap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class CoinMarketCapClient {

    private static final String HOST = "https://api.coinmarketcap.com/v1/";
    private RestTemplate restTemplate = new RestTemplate();

    public GlobalMarketStatistics getGlobalStatistics() throws Exception {
        ResponseEntity<GlobalMarketStatistics> result = restTemplate.getForEntity(HOST + "global", GlobalMarketStatistics.class);

        if (result.getStatusCode().is2xxSuccessful()) {
            GlobalMarketStatistics stats = result.getBody();
            stats.setDate(LocalDateTime.now());
            return stats;
        } else {
            throw new Exception();
        }
    }
}
