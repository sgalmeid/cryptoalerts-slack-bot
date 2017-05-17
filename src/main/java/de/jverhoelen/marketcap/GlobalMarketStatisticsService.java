package de.jverhoelen.marketcap;

import de.jverhoelen.util.repo.AbstractRepositoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalMarketStatisticsService extends AbstractRepositoryService<GlobalMarketStatistics, Long> {
    public GlobalMarketStatistics getLast() {
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        List<GlobalMarketStatistics> page = this.repository.findAll(new PageRequest(0, 1, sort)).getContent();

        return page.isEmpty() ? null : page.get(0);
    }
}
