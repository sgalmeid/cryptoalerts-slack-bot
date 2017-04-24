package de.jverhoelen.notification.statistics;

import de.jverhoelen.ingest.CurrencyPlot;
import de.jverhoelen.ingest.Plot;
import de.jverhoelen.notification.Growth;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlotStatistics {

    private double min;
    private double max;
    private double average;
    private Growth growth;

    public PlotStatistics(List<CurrencyPlot> entries) {
        if (entries.isEmpty()) {
            this.min = -1;
            this.max = -1;
            this.average = -1;
            this.growth = new Growth(-1, -1);
        } else {
            Comparator<CurrencyPlot> lastPlotComparator = Comparator.comparing(o -> o.getPlot().getLast());
            List<Double> plotValues = entries.stream().map(e -> e.getPlot().getLast()).collect(Collectors.toList());
            Double sum = plotValues.stream().mapToDouble(Double::doubleValue).sum();

            CurrencyPlot minPlot = Collections.min(entries, lastPlotComparator);
            CurrencyPlot maxPlot = Collections.max(entries, lastPlotComparator);

            this.min = minPlot.getPlot().getLast();
            this.max = maxPlot.getPlot().getLast();
            this.average = sum / plotValues.size();

            Plot oldest = entries.get(0).getPlot();
            Plot newest = entries.get(entries.size() - 1).getPlot();

            this.growth = new Growth(oldest.getLast(), newest.getLast());
        }
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public Growth getGrowth() {
        return growth;
    }

    public void setGrowth(Growth growth) {
        this.growth = growth;
    }
}
