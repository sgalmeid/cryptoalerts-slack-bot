package de.jverhoelen.marketcap;

import de.jverhoelen.util.repo.AbstractRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalMarketStatisticsService extends AbstractRepositoryService<GlobalMarketStatistics, Long> {

    @Autowired
    private GlobalMarketStatisticsRepository repository;

    public GlobalMarketStatistics getLast() {
        return repository.findTopByOrderByDateDesc();
    }
}
