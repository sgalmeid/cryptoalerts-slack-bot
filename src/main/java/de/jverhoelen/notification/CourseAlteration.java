package de.jverhoelen.notification;

import com.google.common.collect.ImmutableMap;
import de.jverhoelen.config.TimeFrame;

import java.util.Map;

public class CourseAlteration {

    private Growth growth;
    private Growth marketVolumeGrowth;

    public CourseAlteration(Growth growth, Growth marketVolumeGrowth) {
        this.growth = growth;
        this.marketVolumeGrowth = marketVolumeGrowth;
    }

    public Growth getMarketVolumeGrowth() {
        return marketVolumeGrowth;
    }

    public void setMarketVolumeGrowth(Growth marketVolumeGrowth) {
        this.marketVolumeGrowth = marketVolumeGrowth;
    }

    public Growth getGrowth() {
        return growth;
    }

    public void setGrowth(Growth growth) {
        this.growth = growth;
    }

    public NotificationReasonCheck evaluatePossibleNotificationReasons(TimeFrame within) {
        Map<CourseNotificationEvent, Boolean> notifiablePerSource = ImmutableMap.
                <CourseNotificationEvent, Boolean>builder()
//                .put(CourseNotificationEvent.MARKET_VOLUME, marketVolumeGrowth.isNotifiable(within))
                .put(CourseNotificationEvent.PRICE, growth.isNotifiable(within))
                .build();

        return new NotificationReasonCheck(notifiablePerSource);
    }
}
