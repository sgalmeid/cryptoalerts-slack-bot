package de.jverhoelen.config;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.ingest.CurrencyCombination;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Component
public class Configuration {

    private final List<CurrencyCombination> interestingCombinations = Arrays.asList(
            CurrencyCombination.of(CryptoCurrency.BTC, ExchangeCurrency.USDT),
            CurrencyCombination.of(CryptoCurrency.LTC, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.ETC, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.ETH, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.XRP, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.XMR, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.DASH, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.DCR, ExchangeCurrency.BTC)
    );

    private final List<TimeFrame> timeFrames = Arrays.asList(
            TimeFrame.of(ChronoUnit.HOURS, 4, 3, 3),
            TimeFrame.of(ChronoUnit.HOURS, 2, 1, 1),
            TimeFrame.of(ChronoUnit.MINUTES, 30, 2, 2),
            TimeFrame.of(ChronoUnit.MINUTES, 15, 2.5, 2.5)
    );

    public List<CurrencyCombination> getInterestingCombinations() {
        return interestingCombinations;
    }

    public List<TimeFrame> getTimeFrames() {
        return timeFrames;
    }
}
