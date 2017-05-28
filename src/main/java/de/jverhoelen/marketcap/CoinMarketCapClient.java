package de.jverhoelen.marketcap;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.util.SourceNotAvailableException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static de.jverhoelen.util.Utils.getUri;

@Component
public class CoinMarketCapClient {

    private static final String HOST = "https://api.coinmarketcap.com/v1/";
    private RestTemplate restTemplate = new RestTemplate();
    private static final ParameterizedTypeReference<List<CurrencyMarketStatistics>> CURRENCY_MARKET_STATS_REFERENCE = new ParameterizedTypeReference<List<CurrencyMarketStatistics>>() {
    };

    public GlobalMarketStatistics getGlobalStatistics() throws SourceNotAvailableException {
        ResponseEntity<GlobalMarketStatistics> result = restTemplate.getForEntity(HOST + "global", GlobalMarketStatistics.class);
        HttpStatus statusCode = result.getStatusCode();

        if (statusCode.is2xxSuccessful()) {
            GlobalMarketStatistics stats = result.getBody();
            stats.setDate(LocalDateTime.now());
            return stats;
        } else {
            throw new SourceNotAvailableException(
                    "Global statistics from coinmarketcap.com were not available (status code " + statusCode.value() + " " + statusCode.name() + ")"
            );
        }
    }

    public Map<CryptoCurrency, CurrencyMarketStatistics> getCurrencyStatistics() throws Exception {
        ResponseEntity<List<CurrencyMarketStatistics>> result = restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, getUri(HOST + "ticker")), CURRENCY_MARKET_STATS_REFERENCE);
        HttpStatus statusCode = result.getStatusCode();

        if (result.getStatusCode().is2xxSuccessful()) {
            Map<String, List<CurrencyMarketStatistics>> statsPerCurrency = result.getBody()
                    .stream()
                    .collect(Collectors.groupingBy(b -> b.getSymbol()));

            return Arrays.stream(CryptoCurrency.values())
                    .map(crypto -> {
                        List<CurrencyMarketStatistics> statistics = statsPerCurrency.get(crypto.name());
                        CurrencyMarketStatistics matchingEntry = statistics == null ? null : statistics.get(0);
                        return new AbstractMap.SimpleEntry<>(crypto, matchingEntry);
                    })
                    .filter(e -> e.getKey() != null && e.getValue() != null)
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        } else {
            throw new SourceNotAvailableException(
                    "Currency cap statistics from coinmarketcap.com were not available (status code " + statusCode.value() + " " + statusCode.name() + ")"
            );
        }
    }
}
