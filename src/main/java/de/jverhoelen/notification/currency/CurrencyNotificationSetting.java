package de.jverhoelen.notification.currency;

import de.jverhoelen.currency.CurrencyCombination;

public class CurrencyNotificationSetting {

    private CurrencyCombination currencyCombination;
    private int timeframeMinutes;

    public CurrencyNotificationSetting(CurrencyCombination currencyCombination, int timeframeMinutes) {
        this.currencyCombination = currencyCombination;
        this.timeframeMinutes = timeframeMinutes;
    }

    public CurrencyCombination getCurrencyCombination() {
        return currencyCombination;
    }

    public void setCurrencyCombination(CurrencyCombination currencyCombination) {
        this.currencyCombination = currencyCombination;
    }

    public int getTimeframeMinutes() {
        return timeframeMinutes;
    }

    public void setTimeframeMinutes(int timeframeMinutes) {
        this.timeframeMinutes = timeframeMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyNotificationSetting that = (CurrencyNotificationSetting) o;

        if (timeframeMinutes != that.timeframeMinutes) return false;
        return currencyCombination != null ? currencyCombination.equals(that.currencyCombination) : that.currencyCombination == null;
    }

    @Override
    public int hashCode() {
        int result = currencyCombination != null ? currencyCombination.hashCode() : 0;
        result = 31 * result + timeframeMinutes;
        return result;
    }
}
