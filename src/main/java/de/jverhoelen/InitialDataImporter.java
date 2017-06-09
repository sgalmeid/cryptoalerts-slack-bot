package de.jverhoelen;

import de.jverhoelen.balance.notification.BalanceNotification;
import de.jverhoelen.balance.notification.BalanceNotificationService;
import de.jverhoelen.timeframe.TimeFrame;
import de.jverhoelen.timeframe.TimeFrameService;
import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.currency.combination.CurrencyCombinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Component
public class InitialDataImporter {

    @Autowired
    private TimeFrameService timeFrameService;

    @Autowired
    private CurrencyCombinationService currencyCombinationService;

    @Autowired
    private BalanceNotificationService balanceNotificationService;

    @Value("${poloniex.apiKey}")
    private String myApiKey;

    @Value("${poloniex.secretKey}")
    private String myApiSecret;

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
            TimeFrame.of(ChronoUnit.MINUTES, 20, 1, 1),
            TimeFrame.of(ChronoUnit.MINUTES, 40, 1, 1),
            TimeFrame.of(ChronoUnit.HOURS, 1, 1, 1)
    );

    @PostConstruct
    public void initializeDataRepositories() {
        if (timeFrameService.isEmpty()) {
            timeFrameService.add(timeFrames);
        }

        if (currencyCombinationService.isEmpty()) {
            currencyCombinationService.add(interestingCombinations);
        }

        if (balanceNotificationService.isEmpty()) {
            balanceNotificationService.add(Arrays.asList(
                    new BalanceNotification("jonas", "jonas", myApiKey, myApiSecret, true, true, true, true)
            ));
        }
    }
}
