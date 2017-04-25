package de.jverhoelen.currency.plot;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.currency.ExchangeCurrency;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "exchange_index", columnList = "exchange"),
        @Index(name = "currency_index", columnList = "currency"),
        @Index(name = "time_index", columnList = "time")
})
public class CurrencyPlot {

    @Id
    @GeneratedValue
    private long id;

    private LocalDateTime time;


    @Enumerated(value = EnumType.STRING)
    private ExchangeCurrency exchange;

    @Enumerated(value = EnumType.STRING)
    private CryptoCurrency currency;
    private Plot plot;

    public CurrencyPlot(CurrencyCombination combination, Plot plot) {
        this.time = LocalDateTime.now();
        this.exchange = combination.getExchange();
        this.currency = combination.getCrypto();
        this.plot = plot;
    }

    public CurrencyPlot() {
    }

    public CurrencyCombination combination() {
        return new CurrencyCombination(currency, exchange);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
