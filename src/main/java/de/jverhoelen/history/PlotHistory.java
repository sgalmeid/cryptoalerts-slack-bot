package de.jverhoelen.history;

import de.jverhoelen.ingest.CurrencyCombination;
import de.jverhoelen.ingest.CurrencyPlot;
import de.jverhoelen.notification.Growth;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PlotHistory {

    @Value("${fetch.interval.sec}")
    private int PLOT_INTERVAL_SEC;

    private int MIN_PER_H = 60;
    private int SEC_PER_MIN = 60;
    private int MAX_CACHED_SEC = 3 * MIN_PER_H * SEC_PER_MIN; // 3h

    private static final Logger LOGGER = LoggerFactory.getLogger(PlotHistory.class);
    private Map<CurrencyCombination, CircularFifoQueue<CurrencyPlot>> brain = new HashMap();

    public void put(CurrencyPlot plot) {
        CurrencyCombination combination = plot.combination();
        brain.putIfAbsent(combination, new CircularFifoQueue(MAX_CACHED_SEC / PLOT_INTERVAL_SEC));
        brain.get(combination).add(plot);
    }

    public Growth getTotalGrowthPercentage(CurrencyCombination combination, int minutes) {
        int timeframeSeconds = minutes * SEC_PER_MIN;
        if (timeframeSeconds > MAX_CACHED_SEC) {
            throw new RuntimeException("Requested growth last " + minutes + " min but has only " + (MAX_CACHED_SEC / SEC_PER_MIN) + " min");
        }

        CircularFifoQueue<CurrencyPlot> plots = brain.get(combination);
        int beforeElementIndex = (plots.size() - 1) - (timeframeSeconds / PLOT_INTERVAL_SEC);

        CurrencyPlot before = (beforeElementIndex >= 0) ? plots.get(beforeElementIndex) : plots.peek();
        CurrencyPlot lastRecent = plots.get(plots.size() - 1);

        return new Growth(before.getPlot().getLast(), lastRecent.getPlot().getLast());
    }
}
