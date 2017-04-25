package de.jverhoelen.notification;

import de.jverhoelen.config.TimeFrame;

public class Growth {

    private double percentage;
    private double before;
    private double after;
    private String actionPerformed;

    public Growth(double before, double after) {
        this.before = before;
        this.after = after;

        this.percentage = ((after / before) - 1) * 100;
        this.actionPerformed = percentage > 0 ? "⬆️" : "⬇️";
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
        return percentage + " % (" + before + " " + exchangeName + " auf " + after + " " + exchangeName + ")";
    }
}
