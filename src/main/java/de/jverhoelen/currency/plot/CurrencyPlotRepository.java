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

    @Query(value =
            "SELECT * \n" +
                    "FROM currency_plot c \n" +
                    "WHERE c.time >= ?1 AND exchange = ?2 AND currency = ?3 \n" +
                    "ORDER BY c.time DESC\n" +
                    "LIMIT 1",
            nativeQuery = true
    )
    CurrencyPlot getByTimeAndCurrencyAndExchange(LocalDateTime time, ExchangeCurrency exchange, CryptoCurrency currency);

    List<CurrencyPlot> findByCurrencyAndExchangeAndTimeAfter(CryptoCurrency currency, ExchangeCurrency exchange, LocalDateTime time);

    List<CurrencyPlot> findByTimeBetweenAndCurrencyAndExchange(LocalDateTime lowerLimit, LocalDateTime higherLimit, CryptoCurrency currency, ExchangeCurrency exchange);
}
