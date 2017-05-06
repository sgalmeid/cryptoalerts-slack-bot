package de.jverhoelen.currency.combination;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;

public class CurrencyCombinationAddedEvent {

    private String requesterUsername;
    private String crypto;
    private String exchange;
    private String channelName;

    public CurrencyCombinationAddedEvent(String crypto, String exchange, String requesterUsername, String channelName) {
        this.crypto = crypto;
        this.exchange = exchange;
        this.requesterUsername = requesterUsername;
        this.channelName = channelName;
    }

    public CurrencyCombination buildCurrencyCombination() {
        CryptoCurrency cryptoCurr = CryptoCurrency.byShortName(crypto);
        ExchangeCurrency exchangeCurr = ExchangeCurrency.byShortName(exchange);

        if (cryptoCurr != null && exchangeCurr != null) {
            return new CurrencyCombination(cryptoCurr, exchangeCurr);
        }

        return null;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public String getCrypto() {
        return crypto;
    }

    public String getExchange() {
        return exchange;
    }
}
