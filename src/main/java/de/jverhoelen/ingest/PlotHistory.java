package de.jverhoelen.ingest;

import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.currency.plot.CurrencyPlot;
import de.jverhoelen.currency.plot.CurrencyPlotService;
import de.jverhoelen.currency.plot.Plot;
import de.jverhoelen.notification.CourseAlteration;
import de.jverhoelen.notification.Growth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlotHistory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlotHistory.class);

    @Autowired
    private CurrencyPlotService currencyPlotService;

    public void add(CurrencyPlot plot) {
        currencyPlotService.add(plot);
    }

    public Map<CurrencyCombination, List<CurrencyPlot>> getHistoryFor(List<CurrencyCombination> combinations, int maxAgeMinutes) {
        return combinations.stream()
                .map(combi -> new AbstractMap.SimpleEntry<>(combi, currencyPlotService.getByCombination(combi, maxAgeMinutes)))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public List<CurrencyPlot> getCourse(CurrencyCombination combination, int minutes) {
        return currencyPlotService.getByCombination(combination, minutes);
    }

    public CourseAlteration getCourseAlteration(CurrencyCombination combination, int minutes) {
        CurrencyPlot before = currencyPlotService.getPlotBefore(combination, minutes);

        if (before != null) {
            CurrencyPlot lastRecent = currencyPlotService.getPlotBefore(combination, 0);

            Plot be = before.getPlot();
            Plot afterPlot = lastRecent.getPlot();

            Growth growth = Growth.fromLast(be, afterPlot);
            Growth marketVolumeGrowth;
            if (be.getBaseVolume() != null && afterPlot.getBaseVolume() != null) {
                marketVolumeGrowth = Growth.fromBaseVolumes(be, afterPlot);
            } else {
                marketVolumeGrowth = new Growth(0, 0);
            }

            return new CourseAlteration(growth, marketVolumeGrowth);
        }

        return null;
    }
}
