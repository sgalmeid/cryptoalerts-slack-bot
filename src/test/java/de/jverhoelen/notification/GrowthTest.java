package de.jverhoelen.notification;

import de.jverhoelen.currency.plot.Plot;
import de.jverhoelen.timeframe.TimeFrame;
import org.junit.Test;

import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class GrowthTest {

    @Test
    public void fromBaseVolumes() throws Exception {
        Growth growth = Growth.fromBaseVolumes(new Plot(0.0, 300.0), new Plot(0.0, 600.0));
        assertThat(growth.getPercentage(), is(100.0));

        Growth noGrowth = Growth.fromBaseVolumes(null, null);
        assertThat(noGrowth.getPercentage(), is(0.0));
    }

    @Test
    public void fromLast() throws Exception {
        Growth growth = Growth.fromLast(new Plot(300.0, 0.0), new Plot(900.0, 0.0));
        assertThat(growth.getPercentage(), is(200.0));

        Growth noGrowth = Growth.fromLast(null, null);
        assertThat(noGrowth.getPercentage(), is(0.0));
    }

    @Test
    public void getRoundPercentage() throws Exception {
        Growth growth = Growth.fromLast(new Plot(300.0, 0.0), new Plot(935.0, 0.0));
        assertThat(growth.getRoundPercentage(), is("211.67"));
    }

    @Test
    public void buildCourse() throws Exception {
        Growth growth = Growth.fromLast(new Plot(300.0, 0.0), new Plot(900.0, 0.0));
        String btcGrowth = growth.buildCourse("BTC");

        assertThat(btcGrowth, is("300.0 BTC auf 900.0 BTC"));
    }

    @Test
    public void isNotifiable() throws Exception {
        TimeFrame timeFrame = new TimeFrame(ChronoUnit.MINUTES, 10, 1.0, 1.0);

        Growth growth = Growth.fromLast(new Plot(300.0, 0.0), new Plot(900.0, 0.0));
        boolean notifiable = growth.isNotifiable(timeFrame);
        assertTrue(notifiable);

        Growth unnotifiableGrowth = Growth.fromLast(new Plot(300.0, 0.0), new Plot(300.1, 0.0));
        boolean notNotifiable = unnotifiableGrowth.isNotifiable(timeFrame);
        assertFalse(notNotifiable);
    }

    @Test
    public void toString_ofGrowth() throws Exception {
        Growth growth = Growth.fromLast(new Plot(300.0, 0.0), new Plot(900.0, 0.0));
        String str = growth.toString("BTC");

        assertThat(str, is("200.00 % (300.0 BTC auf 900.0 BTC)"));
    }
}