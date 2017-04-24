package de.jverhoelen.ingest;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.history.PlotHistory;
import de.jverhoelen.notification.CurrencySlackService;
import de.jverhoelen.notification.Growth;
import de.jverhoelen.notification.TimeFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PoloniexIngestService {

    private static final String TICKER_URL = "https://poloniex.com/public?command=returnTicker";
    private static final Logger LOGGER = LoggerFactory.getLogger(PoloniexIngestService.class);
    private static final List<CurrencyCombination> INTERESTING_COMBINATIONS = Arrays.asList(
            CurrencyCombination.of(CryptoCurrency.BTC, ExchangeCurrency.USDT),
            CurrencyCombination.of(CryptoCurrency.LTC, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.ETC, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.ETH, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.XRP, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.XMR, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.DASH, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.DCR, ExchangeCurrency.BTC)
    );

    private static final List<TimeFrame> TIME_FRAMES = Arrays.asList(
            TimeFrame.of(ChronoUnit.HOURS, 2, 1, 1),
            TimeFrame.of(ChronoUnit.MINUTES, 30, 2, 2),
            TimeFrame.of(ChronoUnit.MINUTES, 15, 2.5, 2.5)
    );

    @Autowired
    private PlotHistory plotHistory;

    @Autowired
    private CurrencySlackService slack;

    private RestTemplate restTemplate = new RestTemplateBuilder().build();

    @Scheduled(fixedRateString = "#{new Double(${fetch.interval.sec} * 1000).intValue()}")
    public void receivePoloniexTicker() {
        ResponseEntity<HashMap<String, Plot>> entity = getTicker();

        if (entity.getStatusCode().is2xxSuccessful()) {
            Map<String, Plot> body = entity.getBody();

            INTERESTING_COMBINATIONS
                    .stream()
                    .forEach(combi -> {
                        CurrencyPlot plot = new CurrencyPlot(combi, body.get(combi.toApiKey()));
                        plotHistory.put(plot);

                        TIME_FRAMES.stream().forEach(timeFrame -> {
                            Growth growth = plotHistory.getTotalGrowthPercentage(combi, timeFrame.getInMinutes());
                            slack.sendCurrencyNews(combi, growth, timeFrame);
                        });
                    });
        } else {
            LOGGER.warn("Non 2xx status code returned! Maybe got blocked?!");
        }
    }

    private ResponseEntity<HashMap<String, Plot>> getTicker() {
        RequestEntity requestEntity = new RequestEntity<>(HttpMethod.GET, buildUri());
        ParameterizedTypeReference<HashMap<String, Plot>> responseType = new ParameterizedTypeReference<HashMap<String, Plot>>() {
        };

        return restTemplate.exchange(requestEntity, responseType);
    }

    private URI buildUri() {
        try {
            return new URI(TICKER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error");
        }
    }
}
