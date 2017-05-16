package de.jverhoelen.marketcap;

import de.jverhoelen.notification.Growth;

public class GlobalMarketGrowth {

    private Growth totalCapGrowth;
    private Growth dayVolumeGrowth;
    private Growth bitcoinCapGrowth;
    private Growth activeCurrenciesGrowth;
    private Growth activeAssetsGrowth;
    private Growth activeMarketsGrowth;

    public GlobalMarketGrowth(GlobalMarketStatistics now, GlobalMarketStatistics yesterday) {
        totalCapGrowth = new Growth(now.getTotalMarketCap(), yesterday.getTotalMarketCap());
        dayVolumeGrowth = new Growth(now.getTotalDayVolume(), yesterday.getTotalDayVolume());
        bitcoinCapGrowth = new Growth(now.getBitcoinPercentageOfCap(), yesterday.getBitcoinPercentageOfCap());
        activeCurrenciesGrowth = new Growth(now.getActiveCurrencies(), yesterday.getActiveCurrencies());
        activeAssetsGrowth = new Growth(now.getActiveAssets(), yesterday.getActiveAssets());
        activeMarketsGrowth = new Growth(now.getActiveMarkets(), yesterday.getActiveMarkets());
    }

    public Growth getTotalCapGrowth() {
        return totalCapGrowth;
    }

    public void setTotalCapGrowth(Growth totalCapGrowth) {
        this.totalCapGrowth = totalCapGrowth;
    }

    public Growth getDayVolumeGrowth() {
        return dayVolumeGrowth;
    }

    public void setDayVolumeGrowth(Growth dayVolumeGrowth) {
        this.dayVolumeGrowth = dayVolumeGrowth;
    }

    public Growth getBitcoinCapGrowth() {
        return bitcoinCapGrowth;
    }

    public void setBitcoinCapGrowth(Growth bitcoinCapGrowth) {
        this.bitcoinCapGrowth = bitcoinCapGrowth;
    }

    public Growth getActiveCurrenciesGrowth() {
        return activeCurrenciesGrowth;
    }

    public void setActiveCurrenciesGrowth(Growth activeCurrenciesGrowth) {
        this.activeCurrenciesGrowth = activeCurrenciesGrowth;
    }

    public Growth getActiveAssetsGrowth() {
        return activeAssetsGrowth;
    }

    public void setActiveAssetsGrowth(Growth activeAssetsGrowth) {
        this.activeAssetsGrowth = activeAssetsGrowth;
    }

    public Growth getActiveMarketsGrowth() {
        return activeMarketsGrowth;
    }

    public void setActiveMarketsGrowth(Growth activeMarketsGrowth) {
        this.activeMarketsGrowth = activeMarketsGrowth;
    }
}
