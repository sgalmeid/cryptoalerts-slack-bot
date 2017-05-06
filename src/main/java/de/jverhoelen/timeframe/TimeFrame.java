package de.jverhoelen.timeframe;

import com.google.common.base.MoreObjects;


import javax.persistence.*;
import java.time.temporal.ChronoUnit;

@Entity
public class TimeFrame {

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(value = EnumType.STRING)
    private ChronoUnit unit;
    private int frame;
    private double lossThreshold;
    private double gainThreshold;

    public TimeFrame(ChronoUnit unit, int frame, double lossThreshold, double gainThreshold) {
        this.unit = unit;
        this.frame = frame;
        this.lossThreshold = lossThreshold;
        this.gainThreshold = gainThreshold;
    }

    public TimeFrame() {
    }

    public static TimeFrame of(ChronoUnit unit, int frame, double lossThreshold, double gainThreshold) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public void setUnit(ChronoUnit unit) {
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
        return unit == timeFrame.unit;
    }

    @Override
    public int hashCode() {
        int result = unit != null ? unit.hashCode() : 0;
        result = 31 * result + frame;
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("unit", unit)
                .add("frame", frame)
                .add("lossThreshold", lossThreshold)
                .add("gainThreshold", gainThreshold)
                .toString();
    }
}
