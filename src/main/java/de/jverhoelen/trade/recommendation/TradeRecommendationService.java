package de.jverhoelen.trade.recommendation;

import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.currency.combination.CurrencyCombinationService;
import de.jverhoelen.ingest.PlotHistory;
import de.jverhoelen.notification.CourseAlteration;
import de.jverhoelen.notification.SlackService;
import de.jverhoelen.timeframe.TimeFrame;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class TradeRecommendationService {

    private static final int WATCHED_TIMEFRAMES = 5;
    private static final int MIN_POSITIVE_TIMEFRAMES = 2;
    private static final int TIMEFRAME_MINUTES = 1;

    @Value("${fetch.interval.sec}")
    private long recommendIntervalSeconds;

    @Autowired
    private CurrencyCombinationService currencyCombinations;

    @Autowired
    private PlotHistory plotHistory;

    @Autowired
    private SlackService slack;

    private Map<CurrencyCombination, CircularFifoQueue<CourseAlteration>> currencyAlterations = new HashMap<>();

    // every minute,
    //@Scheduled(fixedRate = 60000, initialDelay = 5000)
    public void recommendBuyDecisions() {
        TimeFrame frame = TimeFrame.of(ChronoUnit.MINUTES, TIMEFRAME_MINUTES, 99, 1);

        currencyCombinations.getAll().stream().forEach(cc -> {
            CourseAlteration alteration = plotHistory.getCourseAlteration(cc, frame.getInMinutes());

            if (alteration.evaluatePossibleNotificationReasons(frame).shouldBeNotified()) {
                currencyAlterations.putIfAbsent(cc, new CircularFifoQueue<>(WATCHED_TIMEFRAMES));
                currencyAlterations.get(cc).add(alteration);

                int grewTimes = currencyAlterations.get(cc).size();
                if (grewTimes >= MIN_POSITIVE_TIMEFRAMES) {

                    String message = "*" + cc.getCrypto() + "* ist in jeder der " + grewTimes + " letzten Minuten gestiegen";

                    slack.sendChannelMessage("realtime-trade", message);
                }
            }
        });
    }
}
