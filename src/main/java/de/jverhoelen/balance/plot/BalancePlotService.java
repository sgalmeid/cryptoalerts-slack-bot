package de.jverhoelen.balance.plot;

import de.jverhoelen.util.repo.AbstractRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BalancePlotService extends AbstractRepositoryService<BalancePlot, Long> {

    @Autowired
    private BalancePlotRepository repository;

    public BalancePlot getFromMinutesAgo(String slackUser, int minutes) {
        LocalDateTime backThen = LocalDateTime.now().minusMinutes(minutes);
        LocalDateTime from = backThen.minusSeconds(60);
        LocalDateTime to = backThen.plusSeconds(60);

        List<BalancePlot> allChoices = repository.findBySlackUserAndTimeBetween(slackUser, from, to);

        if (allChoices.isEmpty()) {
            return null;
        }

        return allChoices.get(0);
    }

    public BalancePlot getLast(String slackUser) {
        return repository.findTopBySlackUserOrderByTimeDesc(slackUser);
    }

    public BalancePlot getNextToLast(String slackUser) {
        List<BalancePlot> plots = repository.findTop2BySlackUserOrderByTimeDesc(slackUser);

        if (plots.isEmpty()) {
            return null;
        }

        if (plots.size() == 1) {
            return plots.get(0);
        }

        return plots.get(1);
    }
}
