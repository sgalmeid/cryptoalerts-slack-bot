package de.jverhoelen.currency.combination;

import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.util.repo.AbstractRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyCombinationService extends AbstractRepositoryService<CurrencyCombination, Long> {

    @Autowired
    private CurrencyCombinationRepository repository;

    public CurrencyCombination find(CryptoCurrency crypto, ExchangeCurrency exchange) {
        return repository.findByCryptoAndExchange(crypto, exchange);
    }
}
