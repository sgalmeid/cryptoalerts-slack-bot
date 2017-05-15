package de.jverhoelen.notification;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class NotificationReasonCheckTest {

    @Test
    public void constructor() {
        Map<CourseNotificationEvent, Boolean> notifiable = ImmutableMap.<CourseNotificationEvent, Boolean>builder()
                .put(CourseNotificationEvent.PRICE, true)
                .put(CourseNotificationEvent.MARKET_VOLUME, false)
                .build();

        NotificationReasonCheck check = new NotificationReasonCheck(notifiable);
        assertTrue(check.shouldBeNotified());
        assertThat(check.getNotificationReason(), is(CourseNotificationEvent.PRICE));
    }
}