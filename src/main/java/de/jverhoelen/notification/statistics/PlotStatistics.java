package de.jverhoelen.notification.statistics;

import de.jverhoelen.currency.plot.CurrencyPlot;
import de.jverhoelen.currency.plot.Plot;
import de.jverhoelen.notification.CourseAlteration;
import de.jverhoelen.notification.Growth;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlotStatistics {

    // comparator to compare by last currency price
    private static final Comparator<CurrencyPlot> LAST_PRICE_COMPARATOR = Comparator.comparing(o -> o.getPlot().getLast());

    private double min;
    private double max;
    private double average;
    private CourseAlteration courseAlteration;

    public PlotStatistics(List<CurrencyPlot> entries) {
        if (entries.isEmpty()) {
            this.min = 0;
            this.max = 0;
            this.average = 0;
            this.courseAlteration = new CourseAlteration(new Growth(0, 0), new Growth(0, 0));
        } else {
            // all plot values and their sum
            List<Double> plotValues = entries.stream().map(e -> e.getPlot().getLast()).collect(Collectors.toList());
            Double sum = plotValues.stream().mapToDouble(Double::doubleValue).sum();

            // min and max price plot
            CurrencyPlot minPlot = Collections.min(entries, LAST_PRICE_COMPARATOR);
            CurrencyPlot maxPlot = Collections.max(entries, LAST_PRICE_COMPARATOR);

            this.min = minPlot.getPlot().getLast();
            this.max = maxPlot.getPlot().getLast();
            this.average = sum / plotValues.size();

            // sort entries by time
            entries.sort(Comparator.comparing(o -> o.getTime()));

            Plot oldest = entries.get(0).getPlot();
            Plot newest = entries.get(entries.size() - 1).getPlot();

            Growth growth = Growth.fromLast(oldest, newest);
            Growth marketVolumeGrowth = Growth.fromBaseVolumes(oldest, newest);
            this.courseAlteration = new CourseAlteration(growth, marketVolumeGrowth);
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

    public CourseAlteration getCourseAlteration() {
        return courseAlteration;
    }

    public void setCourseAlteration(CourseAlteration courseAlteration) {
        this.courseAlteration = courseAlteration;
    }
}
