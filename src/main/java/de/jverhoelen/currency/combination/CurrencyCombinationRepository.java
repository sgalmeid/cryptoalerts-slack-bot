package de.jverhoelen.currency.combination;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.util.repo.Repository;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyCombinationRepository extends Repository<CurrencyCombination, Long> {

    CurrencyCombination findByCryptoAndExchange(CryptoCurrency crypto, ExchangeCurrency exchange);
}
