package de.jverhoelen.timeframe;

public class TimeFrameAddedEvent {

    private TimeFrame timeFrame;

    public TimeFrameAddedEvent(TimeFrame timeFrame) {
        this.timeFrame = timeFrame;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }
}
