package de.jverhoelen.ingest;

import de.jverhoelen.config.ConfigurationService;
import de.jverhoelen.config.TimeFrame;
import de.jverhoelen.currency.plot.CurrencyPlot;
import de.jverhoelen.currency.plot.Plot;
import de.jverhoelen.notification.Growth;
import de.jverhoelen.notification.currency.CurrencySlackService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PoloniexIngestService {

    private static final String TICKER_URL = "https://poloniex.com/public?command=returnTicker";
    private static final Logger LOGGER = LoggerFactory.getLogger(PoloniexIngestService.class);

    @Autowired
    private ConfigurationService config;

    @Autowired
    private PlotHistory plotHistory;

    @Autowired
    private CurrencySlackService slack;

    private RestTemplate restTemplate = new RestTemplateBuilder().build();

    @Scheduled(fixedRateString = "#{new Double(${fetch.interval.sec} * 1000).intValue()}", initialDelay = 0)
    public void receivePoloniexTicker() {
        ResponseEntity<HashMap<String, Plot>> entity = getTicker();

        if (entity.getStatusCode().is2xxSuccessful()) {
            Map<String, Plot> body = entity.getBody();
            List<TimeFrame> timeFrames = config.getAllTimeFrames();

            config.getAllCurrencyCombinations().stream()
                    .forEach(combi -> {
                        CurrencyPlot plot = new CurrencyPlot(combi, body.get(combi.toApiKey()));
                        plotHistory.add(plot);

                        timeFrames.stream().forEach(timeFrame -> {
                            Growth growth = plotHistory.getTotalGrowthPercentage(combi, timeFrame.getInMinutes());

                            if (growth != null) {
                                slack.sendCurrencyNews(combi, growth, timeFrame);
                            }
                        });
                    });
        } else {
            LOGGER.warn("Non 2xx status code returned! Maybe got blocked?!");
        }
    }

    private ResponseEntity<HashMap<String, Plot>> getTicker() {
        return restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, buildUri()), new ParameterizedTypeReference<HashMap<String, Plot>>() {
        });
    }

    private URI buildUri() {
        try {
            return new URI(TICKER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error");
        }
    }
}
