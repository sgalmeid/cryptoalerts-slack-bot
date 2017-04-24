package de.jverhoelen.config;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class TimeFrame {

    private TemporalUnit unit;
    private int frame;
    private double lossThreshold;
    private double gainThreshold;

    public TimeFrame(TemporalUnit unit, int frame, double lossThreshold, double gainThreshold) {
        this.unit = unit;
        this.frame = frame;
        this.lossThreshold = lossThreshold;
        this.gainThreshold = gainThreshold;
    }

    public static TimeFrame of(TemporalUnit unit, int frame, double lossThreshold, double gainThreshold) {
        return new TimeFrame(unit, frame, lossThreshold, gainThreshold);
    }

    public int getInMinutes() {
        if (unit.equals(ChronoUnit.MINUTES)) {
            return frame;
        } else if (unit.equals(ChronoUnit.HOURS)) {
            return frame * 60;
        } else if (unit.equals(ChronoUnit.DAYS)) {
            return frame * 60 * 24;
        }

        throw new RuntimeException("getInMinutes not implemented for " + unit.toString());
    }

    public int getInHours() {
        if (unit.equals(ChronoUnit.MINUTES)) {
            return frame / 60;
        } else if (unit.equals(ChronoUnit.HOURS)) {
            return frame;
        } else if (unit.equals(ChronoUnit.DAYS)) {
            return frame * 24;
        }

        throw new RuntimeException("getInHours not implemented for " + unit.toString());
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public TemporalUnit getUnit() {
        return unit;
    }

    public void setUnit(TemporalUnit unit) {
        this.unit = unit;
    }

    public double getLossThreshold() {
        return lossThreshold;
    }

    public void setLossThreshold(double lossThreshold) {
        this.lossThreshold = lossThreshold;
    }

    public double getGainThreshold() {
        return gainThreshold;
    }

    public void setGainThreshold(double gainThreshold) {
        this.gainThreshold = gainThreshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeFrame timeFrame = (TimeFrame) o;

        if (frame != timeFrame.frame) return false;
        if (Double.compare(timeFrame.lossThreshold, lossThreshold) != 0) return false;
        if (Double.compare(timeFrame.gainThreshold, gainThreshold) != 0) return false;
        return unit != null ? unit.equals(timeFrame.unit) : timeFrame.unit == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = unit != null ? unit.hashCode() : 0;
        result = 31 * result + frame;
        temp = Double.doubleToLongBits(lossThreshold);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(gainThreshold);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
