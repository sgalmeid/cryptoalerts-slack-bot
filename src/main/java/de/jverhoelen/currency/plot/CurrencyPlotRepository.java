package de.jverhoelen.currency.plot;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.util.repo.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface CurrencyPlotRepository extends Repository<CurrencyPlot, Long> {

    @Modifying
    void deleteByTimeBefore(LocalDateTime time);

    CurrencyPlot findTopByCurrencyAndExchangeOrderByTimeDesc(CryptoCurrency currency, ExchangeCurrency exchange);

    List<CurrencyPlot> findByCurrencyAndExchangeAndTimeAfterOrderByTimeDesc(CryptoCurrency currency, ExchangeCurrency exchange, LocalDateTime time);

    List<CurrencyPlot> findByTimeBetweenAndCurrencyAndExchangeOrderByTimeDesc(LocalDateTime lowerLimit, LocalDateTime higherLimit, CryptoCurrency currency, ExchangeCurrency exchange);
}
