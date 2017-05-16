package de.jverhoelen.marketcap;

import de.jverhoelen.util.repo.Repository;
import org.springframework.stereotype.Component;

@Component
public interface GlobalMarketStatisticsRepository extends Repository<GlobalMarketStatistics, String> {
}
