package de.jverhoelen.notification;

import de.jverhoelen.config.TimeFrame;

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

    public boolean isNotifiable(TimeFrame within) {
        return growth.isNotifiable(within);
    }
}
