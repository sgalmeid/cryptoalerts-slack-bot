package de.jverhoelen.notification.statistics;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.currency.plot.CurrencyPlot;
import de.jverhoelen.currency.plot.Plot;
import de.jverhoelen.notification.CourseAlteration;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PlotStatisticsTest {

    private static final CurrencyCombination COMBI = CurrencyCombination.of(CryptoCurrency.BURST, ExchangeCurrency.BTC);

    @Test
    public void constructor() {
        testWithKnownPlots(generateSome());
    }

    @Test
    public void constructor_unordered() {
        List<CurrencyPlot> orderedPlots = generateSome();
        Collections.shuffle(orderedPlots);

        testWithKnownPlots(orderedPlots);
    }

    private List<CurrencyPlot> generateSome() {
        return IntStream.range(1, 21)
                .mapToObj(i -> {
                    CurrencyPlot currencyPlot = new CurrencyPlot(COMBI, new Plot(i * 1.01, i * 1.02));
                    currencyPlot.setTime(currencyPlot.getTime().plusSeconds(i * 2));

                    return currencyPlot;
                })
                .collect(Collectors.toList());
    }

    @Test
    public void constructor_empty() {
        PlotStatistics emptyStats = new PlotStatistics(Arrays.asList());

        assertTrue(emptyStats != null);
        assertThat(emptyStats.getMin(), is(0.0));
        assertThat(emptyStats.getMax(), is(0.0));
        assertThat(emptyStats.getAverage(), is(0.0));

        CourseAlteration alteration = emptyStats.getCourseAlteration();
        assertThat(alteration.getGrowth().getPercentage(), is(0.0));
        assertThat(alteration.getMarketVolumeGrowth().getPercentage(), is(0.0));
    }

    private void testWithKnownPlots(List<CurrencyPlot> plots) {
        PlotStatistics statistics = new PlotStatistics(plots);

        assertThat(statistics.getMin(), is(1.01));
        assertThat(statistics.getMax(), is(20.2));
        assertThat(statistics.getAverage(), is(10.605));

        CourseAlteration alteration = statistics.getCourseAlteration();
        assertThat(alteration.getGrowth().getPercentage(), is(1900.0));
        assertThat(alteration.getMarketVolumeGrowth().getPercentage(), is(1900.0));
    }
}