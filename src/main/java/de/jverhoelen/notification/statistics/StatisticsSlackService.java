package de.jverhoelen.notification.statistics;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.history.PlotHistory;
import de.jverhoelen.ingest.CurrencyCombination;
import de.jverhoelen.notification.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsSlackService {

    @Autowired
    private SlackService slack;

    @Autowired
    private PlotHistory history;

    @Value("${statistics.interval.minutes}")
    private int intervalMinutes;

    @Scheduled(fixedRateString = "#{new Double(${statistics.interval.minutes} * 60 * 1000).intValue()}", initialDelay = 180000)
    public void sendRegularStatistics() {
        Map<CurrencyCombination, PlotStatistics> allStatistics = history.getHistory(intervalMinutes)
                .entrySet()
                .stream()
                .map(combi -> {
                    PlotStatistics statistics = new PlotStatistics(combi.getValue());
                    return new AbstractMap.SimpleEntry<>(combi.getKey(), statistics);
                })
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        String message = buildMessage(allStatistics, intervalMinutes);
        slack.sendChannelMessage("statistiken", message);
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void sendDaySummary() {
        int minutesOfADay = 24 * 60;
        Map<CurrencyCombination, PlotStatistics> allStatistics = history.getHistory(minutesOfADay)
                .entrySet()
                .stream()
                .map(combi -> {
                    PlotStatistics statistics = new PlotStatistics(combi.getValue());
                    return new AbstractMap.SimpleEntry<>(combi.getKey(), statistics);
                })
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        String message = buildMessage(allStatistics, minutesOfADay);
        slack.sendChannelMessage("tages-statistik", message);
    }

    private String buildMessage(Map<CurrencyCombination, PlotStatistics> statistics, int minutes) {
        StringBuilder builder = new StringBuilder("Statistiken der letzten *" + minutes + " Minuten*:\n\n");

        statistics.entrySet().stream().forEach(currStat -> {
            PlotStatistics stat = currStat.getValue();
            ExchangeCurrency exchange = currStat.getKey().getExchange();
            CryptoCurrency crypto = currStat.getKey().getCrypto();

            builder.append(buildCurrencySummary(stat, exchange, crypto));
        });

        return builder.toString();
    }

    private String buildCurrencySummary(PlotStatistics stat, ExchangeCurrency exchange, CryptoCurrency crypto) {
        String exchangeName = exchange.getFullName();
        String actionPerformed = stat.getGrowth().getPercentage() > 0 ? "⬆️" : "⬇️";
        return "\uD83D\uDCE2 *" + crypto.getFullName() + "*\n" +
                "&gt; _Wachstum:_ " + stat.getGrowth().toString(exchangeName) + " " + actionPerformed + " \n" +
                "&gt; _MIN:_ " + stat.getMin() + " " + exchangeName + "\n" +
                "&gt; _MAX:_ " + stat.getMax() + " " + exchange + "\n" +
                "&gt; _Durchschnitt:_ " + stat.getAverage() + " " + exchangeName + "\n" +
                "&gt; Mehr Infos: " + getPoloniexExchangeLink(exchange, crypto) + "\n" +
                "\n\n\n";
    }

    private String getPoloniexExchangeLink(ExchangeCurrency exchange, CryptoCurrency crypto) {
        return ("https://poloniex.com/exchange#" + String.format("%s_%s", exchange.name(), crypto.name())).toLowerCase();
    }
}
