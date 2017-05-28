package de.jverhoelen.notification;

import com.google.common.collect.ImmutableMap;
import de.jverhoelen.timeframe.TimeFrame;

import java.util.Map;

public class CourseAlteration {

    private Growth growth;
    private Growth marketVolumeGrowth;
    private Growth marketCapGrowth;

    public CourseAlteration(Growth growth, Growth marketVolumeGrowth, Growth marketCapGrowth) {
        this.growth = growth;
        this.marketVolumeGrowth = marketVolumeGrowth;
        this.marketCapGrowth = marketCapGrowth;
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

    public Growth getMarketCapGrowth() {
        return marketCapGrowth;
    }

    public void setMarketCapGrowth(Growth marketCapGrowth) {
        this.marketCapGrowth = marketCapGrowth;
    }

    public NotificationReasonCheck evaluatePossibleNotificationReasons(TimeFrame within) {
        Map<CourseNotificationEvent, Boolean> notifiablePerSource = ImmutableMap.
                <CourseNotificationEvent, Boolean>builder()
                .put(CourseNotificationEvent.PRICE, growth.isNotifiable(within))
                .build();

        return new NotificationReasonCheck(notifiablePerSource);
    }
}
