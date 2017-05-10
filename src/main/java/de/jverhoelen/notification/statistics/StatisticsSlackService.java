package de.jverhoelen.notification.statistics;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.currency.combination.CurrencyCombinationService;
import de.jverhoelen.ingest.PlotHistory;
import de.jverhoelen.interaction.FatalErrorEvent;
import de.jverhoelen.notification.CourseAlteration;
import de.jverhoelen.notification.Growth;
import de.jverhoelen.notification.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

import static de.jverhoelen.util.Utils.roundSmartly;

@Service
public class StatisticsSlackService {

    @Autowired
    private SlackService slack;

    @Autowired
    private PlotHistory history;

    @Autowired
    private CurrencyCombinationService currencyCombinations;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Value("${statistics.interval.minutes}")
    private int intervalMinutes;
    private int minutesOfADay = 24 * 60;

    @Scheduled(fixedRateString = "#{new Double(${statistics.interval.minutes} * 60 * 1000).intValue()}", initialDelay = 180000)
    public void sendRegularStatistics() {
        try {
            Map<CurrencyCombination, PlotStatistics> allStatistics = getCurrencyCombinationStatistics(intervalMinutes);

            String message = buildMessage(allStatistics, intervalMinutes);
            slack.sendChannelMessage("statistiken", message);
        } catch (Exception ex) {
            eventPublisher.publishEvent(new FatalErrorEvent("Beim Generieren oder Senden der Statistik ist ein Fehler aufgetreten", ex));
        }
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void sendDaySummary() {
        try {
            Map<CurrencyCombination, PlotStatistics> allStatistics = getCurrencyCombinationStatistics(minutesOfADay);

            String message = buildMessage(allStatistics, minutesOfADay);
            slack.sendChannelMessage("tages-statistik", message);
        } catch (Exception ex) {
            eventPublisher.publishEvent(new FatalErrorEvent("Beim Generieren oder Senden der Tages-Statistik ist ein Fehler aufgetreten", ex));
        }
    }

    private Map<CurrencyCombination, PlotStatistics> getCurrencyCombinationStatistics(int minutes) {
        return history.getHistoryFor(currencyCombinations.getAll(), minutes).entrySet()
                .stream()
                .map(combiHistory -> {
                    PlotStatistics statistics = new PlotStatistics(combiHistory.getValue());
                    return new AbstractMap.SimpleEntry<>(combiHistory.getKey(), statistics);
                })
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
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
        CourseAlteration courseAlteration = stat.getCourseAlteration();
        Growth growth = courseAlteration.getGrowth();
        Growth marketVolumeGrowth = courseAlteration.getMarketVolumeGrowth();

        return "\uD83D\uDCE2 *" + crypto.getFullName() + "*\n" +
                "&gt; _Wachstum:_ " + growth.toString(exchangeName) + " " + growth.getActionPerformed() + " \n" +
                "&gt; _MIN:_ " + roundSmartly(stat.getMin()) + " " + exchangeName + "\n" +
                "&gt; _MAX:_ " + roundSmartly(stat.getMax()) + " " + exchange + "\n" +
                "&gt; _Durchschnitt:_ " + roundSmartly(stat.getAverage()) + " " + exchangeName + "\n" +
                "&gt; _Marktvolumen:_ " + marketVolumeGrowth.getRoundPercentage() + " % " + marketVolumeGrowth.getActionPerformed() + "\n" +
                "&gt; Mehr Infos: " + getPoloniexExchangeLink(exchange, crypto) + "\n" +
                "\n\n";
    }

    public static String getPoloniexExchangeLink(ExchangeCurrency exchange, CryptoCurrency crypto) {
        return ("https://poloniex.com/exchange#" + String.format("%s_%s", exchange.name(), crypto.name())).toLowerCase();
    }
}
