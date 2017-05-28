package de.jverhoelen.currency.plot;

public class Plot {

    private Double last;
    private Double baseVolume;
    private Double marketCap;

    public Plot(Double last, Double baseVolume, Double marketCap) {
        this.last = last;
        this.baseVolume = baseVolume;
        this.marketCap = marketCap;
    }

    public Plot(Double last, Double baseVolume) {
        this(last, baseVolume, 0.0);
    }

    public Plot() {
    }

    public Double getLast() {
        return last;
    }

    public void setLast(Double last) {
        this.last = last;
    }

    public Double getBaseVolume() {
        return baseVolume;
    }

    public void setBaseVolume(Double baseVolume) {
        this.baseVolume = baseVolume;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }
}
