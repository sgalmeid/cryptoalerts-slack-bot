package de.jverhoelen.marketcap;

import de.jverhoelen.interaction.FatalErrorEvent;
import de.jverhoelen.notification.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GlobalMarketStatisticsReporter {

    @Autowired
    private CoinMarketCapClient client;

    @Autowired
    private SlackService slack;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private GlobalMarketStatisticsService statisticsService;

    @Scheduled(cron = "0 0 3,9,16,23 * * *")
    public void reportMarketChanges() {
        try {
            GlobalMarketStatistics currentStats = client.getGlobalStatistics();
            GlobalMarketStatistics yesterdaysStats = statisticsService.getLast();

            GlobalMarketGrowth totalGrowth = null;

            if (yesterdaysStats != null) {
                totalGrowth = new GlobalMarketGrowth(currentStats, yesterdaysStats);
            }

            String message = buildGrowthMessage(currentStats, totalGrowth);
            slack.sendChannelMessage("markt-kapitalisierung", message);

            statisticsService.add(currentStats);
        } catch (Exception ex) {
            eventPublisher.publishEvent(
                    new FatalErrorEvent("Beim Abfragen und Reporten der global market capitalization statistics ist ein Fehler aufgetreten", ex)
            );
        }
    }

    private String buildGrowthMessage(GlobalMarketStatistics stats, GlobalMarketGrowth growths) {
        StringBuilder builder = new StringBuilder("🌎💰 Markt-Kapitalisierung am " + LocalDateTime.now().toString() + ":\n");

        builder.append("\n&gt; Gesamte Markt-Kapitalisierung: " + stats.getTotalMarketCapInMillions() + " Millionen $" + (growths != null ? (" (" + growths.getTotalCapGrowth().getPercentagePart() + ")") : ""));
        builder.append("\n&gt; 24h Volumen: " + stats.getTotalDayVolumeInMillions() + " Millionen $" + (growths != null ? (" (" + growths.getDayVolumeGrowth().getPercentagePart() + ")") : ""));
        builder.append("\n&gt; Bitcoin-Anteil am Markt: " + stats.getBitcoinPercentageOfCap() + "%" + (growths != null ? (" (" + growths.getBitcoinCapGrowth().getPercentagePart() + ")") : ""));
        builder.append("\n&gt; " + stats.getActiveCurrencies() + " aktive Krypto-Währungen" + (growths != null ? (" (" + growths.getActiveCurrenciesGrowth().getPercentagePart() + ")") : ""));
        builder.append("\n&gt; " + stats.getActiveMarkets() + " aktive Märkte" + (growths != null ? (" (" + growths.getActiveMarketsGrowth().getPercentagePart() + ")") : ""));
        builder.append("\n&gt; " + stats.getActiveAssets() + " aktive Assets" + (growths != null ? (" (" + growths.getActiveAssetsGrowth().getPercentagePart() + ")") : ""));

        return builder.toString();
    }
}
