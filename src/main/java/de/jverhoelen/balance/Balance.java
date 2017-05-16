package de.jverhoelen.balance;

public class Balance {

    private double available;
    private double onOrders;
    private double btcValue;

    public Balance(double available, double onOrders, double btcValue) {
        this.available = available;
        this.onOrders = onOrders;
        this.btcValue = btcValue;
    }

    public Balance() {
    }

    public double getAvailable() {
        return available;
    }

    public String getRoundedAvailable() {
        return String.format("%.2f", available);
    }

    public String getRoundedOnOrders() {
        return String.format("%.2f", onOrders);
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getOnOrders() {
        return onOrders;
    }

    public void setOnOrders(double onOrders) {
        this.onOrders = onOrders;
    }

    public double getBtcValue() {
        return btcValue;
    }

    public String getRoundedBtcValue() {
        return String.format("%.3f", btcValue);
    }

    public void setBtcValue(double btcValue) {
        this.btcValue = btcValue;
    }
}
