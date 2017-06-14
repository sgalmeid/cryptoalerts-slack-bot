package de.jverhoelen.ingest;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.currency.combination.CurrencyCombinationService;
import de.jverhoelen.currency.plot.CurrencyPlot;
import de.jverhoelen.currency.plot.Plot;
import de.jverhoelen.interaction.FatalErrorEvent;
import de.jverhoelen.marketcap.CoinMarketCapClient;
import de.jverhoelen.marketcap.CurrencyMarketStatistics;
import de.jverhoelen.notification.CourseAlteration;
import de.jverhoelen.notification.currency.CurrencySlackService;
import de.jverhoelen.timeframe.TimeFrame;
import de.jverhoelen.timeframe.TimeFrameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.jverhoelen.util.Utils.getUri;

@Service
public class PoloniexIngestService {

    private static final String TICKER_URL = "https://poloniex.com/public?command=returnTicker";
    private static final ParameterizedTypeReference<HashMap<String, Plot>> STRING_PLOT_REFERENCE = new ParameterizedTypeReference<HashMap<String, Plot>>() {
    };

    private CurrencyCombinationService currencyCombinations;
    private ApplicationEventPublisher eventPublisher;
    private TimeFrameService timeFrames;
    private PlotHistory plotHistory;
    private CurrencySlackService slack;
    private CoinMarketCapClient marketCapClient;
    private RestTemplate restTemplate = new RestTemplateBuilder().build();

    @Autowired
    public PoloniexIngestService(CurrencyCombinationService currencyCombinations,
                                 ApplicationEventPublisher eventPublisher,
                                 TimeFrameService timeFrames,
                                 PlotHistory plotHistory,
                                 CurrencySlackService slack,
                                 CoinMarketCapClient marketCapClient) {
        this.currencyCombinations = currencyCombinations;
        this.eventPublisher = eventPublisher;
        this.timeFrames = timeFrames;
        this.plotHistory = plotHistory;
        this.slack = slack;
        this.marketCapClient = marketCapClient;
    }

    @Scheduled(fixedRateString = "#{new Double(${fetch.interval.sec} * 1000).intValue()}", initialDelay = 0)
    public void receivePoloniexTicker() {
        try {
            ResponseEntity<HashMap<String, Plot>> entity = getTicker();
            Map<CryptoCurrency, CurrencyMarketStatistics> marketCaps = new HashMap<>();
            try {
                marketCaps = marketCapClient.getCurrencyStatistics();
            } catch (Exception e) {
                eventPublisher.publishEvent(new FatalErrorEvent("Market Cap Statistiken von coinmarketcap.com konnten nicht abgefragt werden", e));
            }

            if (entity.getStatusCode().is2xxSuccessful()) {
                Map<String, Plot> body = entity.getBody();
                List<TimeFrame> allTimeFrames = timeFrames.getAll();

                Map<CryptoCurrency, CurrencyMarketStatistics> finalMarketCaps = marketCaps;
                currencyCombinations.getAll().stream().forEach(combi ->
                        compareAndReport(body, allTimeFrames, combi, finalMarketCaps)
                );
            } else {
                throw new RuntimeException("Non 2xx status code returned! Maybe got blocked?!");
            }
        } catch (Exception ex) {
            eventPublisher.publishEvent(new FatalErrorEvent("Beim Abfragen des Poloniex Tickers, berechnen der Werte oder senden der Alerts ist ein Fehler aufgetreten", ex));
        }
    }

    private void compareAndReport(Map<String, Plot> body, List<TimeFrame> allTimeFrames, CurrencyCombination combi, Map<CryptoCurrency, CurrencyMarketStatistics> marketCaps) {
        CurrencyPlot plot = new CurrencyPlot(combi, body.get(combi.toApiKey()));
        plot.registerMarketCapitalization(marketCaps.get(combi.getCrypto().getMarketCapName()));
        plotHistory.add(plot);

        CurrencyPlot usdtPlot = findUsDollarPlot(body, combi, plot);
        allTimeFrames.stream().forEach(timeFrame -> {
            CourseAlteration courseAlteration = plotHistory.getCourseAlteration(combi, timeFrame.getInMinutes());

            if (courseAlteration != null) {
                slack.sendCurrencyNews(combi, courseAlteration, usdtPlot, timeFrame);
            }
        });
    }

    private CurrencyPlot findUsDollarPlot(Map<String, Plot> plotsPayload, CurrencyCombination regularCombination, CurrencyPlot regularPlot) {
        CurrencyPlot usDollarPlot = null;

        // set this plot if the exchange of the regular plot is not already USDT
        if (!regularPlot.getExchange().equals(ExchangeCurrency.USDT)) {
            CurrencyCombination combiWithUsdt = CurrencyCombination.of(regularCombination.getCrypto(), ExchangeCurrency.USDT);
            Plot actualPlot = plotsPayload.get(combiWithUsdt.toApiKey());
            if (actualPlot != null) {
                usDollarPlot = new CurrencyPlot(combiWithUsdt, actualPlot);
            }
        }

        return usDollarPlot;
    }

    private ResponseEntity<HashMap<String, Plot>> getTicker() {
        return restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, getUri(TICKER_URL)), STRING_PLOT_REFERENCE);
    }
}
