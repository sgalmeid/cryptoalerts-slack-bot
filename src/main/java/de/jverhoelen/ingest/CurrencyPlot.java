package de.jverhoelen.ingest;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.CurrencyCombination;
import de.jverhoelen.currency.ExchangeCurrency;

import java.time.LocalDateTime;

public class CurrencyPlot {

    private LocalDateTime time = LocalDateTime.now();
    private ExchangeCurrency exchange;
    private CryptoCurrency currency;
    private Plot plot;

    public CurrencyPlot(String combination, Plot plot) {
        String[] split = combination.split("_");

        this.exchange = ExchangeCurrency.valueOf(split[0]);
        this.currency = CryptoCurrency.valueOf(split[1]);
        this.plot = plot;
    }

    public CurrencyPlot(CurrencyCombination combination, Plot plot) {
        this.exchange = combination.getExchange();
        this.currency = combination.getCrypto();
        this.plot = plot;
    }

    public CurrencyPlot() {
    }

    public CurrencyCombination combination() {
        return new CurrencyCombination(currency, exchange);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyPlot that = (CurrencyPlot) o;

        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (exchange != that.exchange) return false;
        return currency == that.currency;
    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (exchange != null ? exchange.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public ExchangeCurrency getExchange() {
        return exchange;
    }

    public void setExchange(ExchangeCurrency exchange) {
        this.exchange = exchange;
    }

    public CryptoCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(CryptoCurrency currency) {
        this.currency = currency;
    }

    public Plot getPlot() {
        return plot;
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
    }
}
