package de.jverhoelen.config;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.CurrencyCombinationService;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.currency.CurrencyCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Component
public class ConfigurationService {

    @Autowired
    private TimeFrameService timeFrameService;

    @Autowired
    private CurrencyCombinationService currencyCombinationService;

    private final List<CurrencyCombination> interestingCombinations = Arrays.asList(
            CurrencyCombination.of(CryptoCurrency.BTC, ExchangeCurrency.USDT),
            CurrencyCombination.of(CryptoCurrency.LTC, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.ETC, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.ETH, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.XRP, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.XMR, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.DASH, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.DCR, ExchangeCurrency.BTC),
            CurrencyCombination.of(CryptoCurrency.GAME, ExchangeCurrency.BTC)
    );

    private final List<TimeFrame> timeFrames = Arrays.asList(
            TimeFrame.of(ChronoUnit.HOURS, 2, 1, 1),
            TimeFrame.of(ChronoUnit.MINUTES, 30, 2, 2),
            TimeFrame.of(ChronoUnit.MINUTES, 15, 2.5, 2.5)
    );

    @PostConstruct
    public void init() {
        if (timeFrameService.isEmpty()) {
            timeFrameService.add(timeFrames);
        }

        if (currencyCombinationService.isEmpty()) {
            currencyCombinationService.add(interestingCombinations);
        }
    }

    public List<TimeFrame> getAllTimeFrames() {
        return timeFrameService.getAll();
    }

    public List<CurrencyCombination> getAllCurrencyCombinations() {
        return currencyCombinationService.getAll();
    }
}
