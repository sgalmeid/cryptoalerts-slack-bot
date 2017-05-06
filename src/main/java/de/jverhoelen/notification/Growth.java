package de.jverhoelen.notification;

import de.jverhoelen.timeframe.TimeFrame;
import de.jverhoelen.currency.plot.Plot;

import static de.jverhoelen.util.Utils.roundSmartly;

public class Growth {

    private double percentage;
    private double before;
    private double after;
    private String actionPerformed;

    public Growth(double before, double after) {
        this.before = before;
        this.after = after;

        if (before == after) {
            this.percentage = 0;
        } else {
            this.percentage = ((after / before) - 1) * 100;
        }
        this.actionPerformed = createActionPerformed(percentage);
    }

    public static Growth fromBaseVolumes(Plot before, Plot after) {
        return new Growth(before == null ? 0 : before.getBaseVolume(), after == null ? 0 : after.getBaseVolume());
    }

    public static Growth fromLast(Plot before, Plot after) {
        return new Growth(before == null ? 0 : before.getLast(), after == null ? 0 : after.getLast());
    }

    private String createActionPerformed(double percentage) {
        if (percentage == 0) {
            return "\uD83D\uDCA4";
        } else if (percentage > 0) {
            return "⬆️";
        } else {
            return "⬇️";
        }
    }

    public String getRoundPercentage() {
        return String.format("%.2f", percentage);
    }

    public String buildCourse(String exchangeName) {
        return roundSmartly(before) + " " + exchangeName + " auf " + roundSmartly(after) + " " + exchangeName;
    }

    public boolean isNotifiable(TimeFrame within) {
        return percentage >= within.getGainThreshold() || percentage <= (within.getLossThreshold() * -1);
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getBefore() {
        return before;
    }

    public void setBefore(double before) {
        this.before = before;
    }

    public double getAfter() {
        return after;
    }

    public void setAfter(double after) {
        this.after = after;
    }

    public String toString(String exchangeName) {
        return getRoundPercentage() + " % (" + roundSmartly(before) + " " + exchangeName + " auf " + roundSmartly(after) + " " + exchangeName + ")";
    }
}
