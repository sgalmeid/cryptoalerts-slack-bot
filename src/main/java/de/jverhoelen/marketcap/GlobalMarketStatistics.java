package de.jverhoelen.marketcap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GlobalMarketStatistics {

    @Id
    private String date;

    @JsonProperty("total_market_cap_usd")
    private long totalMarketCap;

    @JsonProperty("total_24h_volume_usd")
    private long totalDayVolume;

    @JsonProperty("bitcoin_percentage_of_market_cap")
    private double bitcoinPercentageOfCap;

    @JsonProperty("active_currencies")
    private long activeCurrencies;

    @JsonProperty("active_assets")
    private long activeAssets;

    @JsonProperty("active_markets")
    private long activeMarkets;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTotalMarketCap() {
        return totalMarketCap;
    }

    public void setTotalMarketCap(long totalMarketCap) {
        this.totalMarketCap = totalMarketCap;
    }

    public long getTotalDayVolume() {
        return totalDayVolume;
    }

    public void setTotalDayVolume(long totalDayVolume) {
        this.totalDayVolume = totalDayVolume;
    }

    public double getBitcoinPercentageOfCap() {
        return bitcoinPercentageOfCap;
    }

    public void setBitcoinPercentageOfCap(double bitcoinPercentageOfCap) {
        this.bitcoinPercentageOfCap = bitcoinPercentageOfCap;
    }

    public long getActiveCurrencies() {
        return activeCurrencies;
    }

    public void setActiveCurrencies(long activeCurrencies) {
        this.activeCurrencies = activeCurrencies;
    }

    public long getActiveAssets() {
        return activeAssets;
    }

    public void setActiveAssets(long activeAssets) {
        this.activeAssets = activeAssets;
    }

    public long getActiveMarkets() {
        return activeMarkets;
    }

    public void setActiveMarkets(long activeMarkets) {
        this.activeMarkets = activeMarkets;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("date", date)
                .add("totalMarketCap", totalMarketCap)
                .add("totalDayVolume", totalDayVolume)
                .add("bitcoinPercentageOfCap", bitcoinPercentageOfCap)
                .add("activeCurrencies", activeCurrencies)
                .add("activeAssets", activeAssets)
                .add("activeMarkets", activeMarkets)
                .toString();
    }
}
