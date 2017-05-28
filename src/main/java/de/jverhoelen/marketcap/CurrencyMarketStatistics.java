package de.jverhoelen.marketcap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyMarketStatistics {

    private String id;
    private String name;
    private String symbol;
    private int rank;

    @JsonProperty("price_usd")
    private double priceUsd;

    @JsonProperty("price_btc")
    private double priceBtc;

    @JsonProperty("24h_volume_usd")
    private double dayVolumeUsd;

    @JsonProperty("market_cap_usd")
    private double marketCapUsd;

    @JsonProperty("available_supply")
    private double availableSupply;

    @JsonProperty("total_supply")
    private double totalSupply;

    @JsonProperty("percent_change_1h")
    private double percentChangeLastHour;

    @JsonProperty("percent_change_24h")
    private double percentChangeLast24Hours;

    @JsonProperty("percent_change_7d")
    private double percentChangeLast7Days;

    @JsonProperty("last_updated")
    private long lastUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(double priceUsd) {
        this.priceUsd = priceUsd;
    }

    public double getPriceBtc() {
        return priceBtc;
    }

    public void setPriceBtc(double priceBtc) {
        this.priceBtc = priceBtc;
    }

    public double getDayVolumeUsd() {
        return dayVolumeUsd;
    }

    public void setDayVolumeUsd(double dayVolumeUsd) {
        this.dayVolumeUsd = dayVolumeUsd;
    }

    public double getMarketCapUsd() {
        return marketCapUsd;
    }

    public void setMarketCapUsd(double marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    public double getAvailableSupply() {
        return availableSupply;
    }

    public void setAvailableSupply(double availableSupply) {
        this.availableSupply = availableSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public double getPercentChangeLastHour() {
        return percentChangeLastHour;
    }

    public void setPercentChangeLastHour(double percentChangeLastHour) {
        this.percentChangeLastHour = percentChangeLastHour;
    }

    public double getPercentChangeLast24Hours() {
        return percentChangeLast24Hours;
    }

    public void setPercentChangeLast24Hours(double percentChangeLast24Hours) {
        this.percentChangeLast24Hours = percentChangeLast24Hours;
    }

    public double getPercentChangeLast7Days() {
        return percentChangeLast7Days;
    }

    public void setPercentChangeLast7Days(double percentChangeLast7Days) {
        this.percentChangeLast7Days = percentChangeLast7Days;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyMarketStatistics that = (CurrencyMarketStatistics) o;

        if (lastUpdated != that.lastUpdated) return false;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (lastUpdated ^ (lastUpdated >>> 32));
        return result;
    }
}
