package de.jverhoelen.timeframe;

import de.jverhoelen.util.repo.Repository;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public interface TimeFrameRepository extends Repository<TimeFrame, Long> {

    void deleteByUnitAndFrame(ChronoUnit unit, int count);
}
