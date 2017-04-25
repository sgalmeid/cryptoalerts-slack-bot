package de.jverhoelen.currency;

import de.jverhoelen.util.Repository;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyCombinationRepository extends Repository<CurrencyCombination, Long> {
}
