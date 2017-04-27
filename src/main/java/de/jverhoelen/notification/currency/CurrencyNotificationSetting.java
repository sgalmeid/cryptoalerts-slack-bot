package de.jverhoelen.notification.currency;

import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.notification.CourseNotificationEvent;

public class CurrencyNotificationSetting {

    private CurrencyCombination currencyCombination;
    private int timeframeMinutes;
    private CourseNotificationEvent event;

    public CurrencyNotificationSetting(CurrencyCombination currencyCombination, int timeframeMinutes, CourseNotificationEvent event) {
        this.currencyCombination = currencyCombination;
        this.timeframeMinutes = timeframeMinutes;
        this.event = event;
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

    public CourseNotificationEvent getEvent() {
        return event;
    }

    public void setEvent(CourseNotificationEvent event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyNotificationSetting that = (CurrencyNotificationSetting) o;

        if (timeframeMinutes != that.timeframeMinutes) return false;
        if (currencyCombination != null ? !currencyCombination.equals(that.currencyCombination) : that.currencyCombination != null) return false;
        return event == that.event;
    }

    @Override
    public int hashCode() {
        int result = currencyCombination != null ? currencyCombination.hashCode() : 0;
        result = 31 * result + timeframeMinutes;
        result = 31 * result + (event != null ? event.hashCode() : 0);
        return result;
    }
}
