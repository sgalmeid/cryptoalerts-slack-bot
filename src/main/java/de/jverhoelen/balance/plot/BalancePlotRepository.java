package de.jverhoelen.balance.plot;

import de.jverhoelen.util.repo.Repository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface BalancePlotRepository extends Repository<BalancePlot, Long> {

    List<BalancePlot> findBySlackUserAndTimeBetween(String slackUser, LocalDateTime from, LocalDateTime to);
}
