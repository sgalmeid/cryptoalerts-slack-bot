package de.jverhoelen.currency.plot;

import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.util.repo.AbstractRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurrencyPlotService extends AbstractRepositoryService<CurrencyPlot, Long> {

    @Autowired
    private CurrencyPlotRepository repository;

    public void deleteAllOlderThan(int hours) {
        repository.deleteByTimeBefore(LocalDateTime.now().minusHours(hours));
    }

    public List<CurrencyPlot> getByCombination(CurrencyCombination combination, int maxAgeMinutes) {
        LocalDateTime maxTime = LocalDateTime.now().minusMinutes(maxAgeMinutes);
        return repository.findByCurrencyAndExchangeAndTimeAfterOrderByTimeDesc(combination.getCrypto(), combination.getExchange(), maxTime);
    }

    public CurrencyPlot getPlotBefore(CurrencyCombination combination, int minutes) {
        return getPlotClosestTo(LocalDateTime.now().minusMinutes(minutes), combination);
    }

    public CurrencyPlot getPlotClosestTo(LocalDateTime time, CurrencyCombination combination) {
        LocalDateTime lowerLimit = time.minusSeconds(20);
        LocalDateTime higherLimit = time.plusSeconds(20);
        List<CurrencyPlot> allCandidates = repository.findByTimeBetweenAndCurrencyAndExchangeOrderByTimeDesc(lowerLimit, higherLimit, combination.getCrypto(), combination.getExchange());

        if (allCandidates.isEmpty()) {
            return null;
        }

        return allCandidates.get(0);
    }

    public CurrencyPlot getPlot(LocalDateTime time, CurrencyCombination combination) {
        return repository.getByTimeAndCurrencyAndExchange(time, combination.getExchange(), combination.getCrypto());
    }
}
