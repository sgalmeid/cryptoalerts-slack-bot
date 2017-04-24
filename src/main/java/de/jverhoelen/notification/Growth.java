package de.jverhoelen.notification;

public class Growth {

    private double percentage;
    private double before;
    private double after;

    public Growth(double before, double after) {
        this.before = before;
        this.after = after;

        this.percentage = ((after / before) - 1) * 100;
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
