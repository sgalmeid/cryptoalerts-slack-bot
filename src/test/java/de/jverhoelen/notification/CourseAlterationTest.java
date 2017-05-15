package de.jverhoelen.notification;

import de.jverhoelen.currency.plot.Plot;
import de.jverhoelen.timeframe.TimeFrame;
import org.junit.Test;

import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CourseAlterationTest {

    @Test
    public void evaluatePossibleNotificationReasons() throws Exception {
        TimeFrame timeFrame = new TimeFrame(ChronoUnit.MINUTES, 10, 1.0, 1.0);

        Growth growth = Growth.fromLast(new Plot(300.0, 0.0), new Plot(900.0, 0.0));
        Growth volumeGrowth = Growth.fromBaseVolumes(new Plot(0.0, 300.0), new Plot(0.0, 600.0));

        CourseAlteration alteration = new CourseAlteration(growth, volumeGrowth);
        NotificationReasonCheck reasons = alteration.evaluatePossibleNotificationReasons(timeFrame);

        assertTrue(reasons.isShouldBeNotified());
        assertThat(reasons.getNotificationReason(), is(CourseNotificationEvent.PRICE));
    }
}