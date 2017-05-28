package de.jverhoelen.marketcap;

import de.jverhoelen.notification.Growth;

public class GlobalMarketGrowth {

    private Growth totalCapGrowth;
    private Growth dayVolumeGrowth;
    private Growth bitcoinCapGrowth;
    private Growth activeCurrenciesGrowth;
    private Growth activeAssetsGrowth;
    private Growth activeMarketsGrowth;

    public GlobalMarketGrowth(GlobalMarketStatistics now, GlobalMarketStatistics previous) {
        totalCapGrowth = new Growth(previous.getTotalMarketCap(), now.getTotalMarketCap());
        dayVolumeGrowth = new Growth(previous.getTotalDayVolume(), now.getTotalDayVolume());
        bitcoinCapGrowth = new Growth(previous.getBitcoinPercentageOfCap(), now.getBitcoinPercentageOfCap());
        activeCurrenciesGrowth = new Growth(previous.getActiveCurrencies(), now.getActiveCurrencies());
        activeAssetsGrowth = new Growth(previous.getActiveAssets(), now.getActiveAssets());
        activeMarketsGrowth = new Growth(previous.getActiveMarkets(), now.getActiveMarkets());
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
