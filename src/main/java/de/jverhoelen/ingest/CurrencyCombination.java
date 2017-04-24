package de.jverhoelen.ingest;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;

public class CurrencyCombination {

    private CryptoCurrency crypto;
    private ExchangeCurrency exchange;

    public CurrencyCombination(CryptoCurrency crypto, ExchangeCurrency exchange) {
        this.crypto = crypto;
        this.exchange = exchange;
    }

    public CurrencyCombination() {
    }

    public static CurrencyCombination of(CryptoCurrency crypto, ExchangeCurrency exchange) {
        return new CurrencyCombination(crypto, exchange);
    }

    public ExchangeCurrency getExchange() {
        return exchange;
    }

    public void setExchange(ExchangeCurrency exchange) {
        this.exchange = exchange;
    }

    public CryptoCurrency getCrypto() {
        return crypto;
    }

    public void setCrypto(CryptoCurrency crypto) {
        this.crypto = crypto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyCombination that = (CurrencyCombination) o;

        if (crypto != that.crypto) return false;
        return exchange == that.exchange;
    }

    @Override
    public int hashCode() {
        int result = crypto != null ? crypto.hashCode() : 0;
        result = 31 * result + (exchange != null ? exchange.hashCode() : 0);
        return result;
    }

    public String toApiKey() {
        return exchange.name() + "_" + crypto.name();
    }
}
