package de.jverhoelen.marketcap;

import de.jverhoelen.util.repo.Repository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public interface GlobalMarketStatisticsRepository extends Repository<GlobalMarketStatistics, Long> {
}
