package de.jverhoelen.config;

import de.jverhoelen.util.repo.AbstractRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
public class TimeFrameService extends AbstractRepositoryService<TimeFrame, Long> {

    @Autowired
    private TimeFrameRepository repository;

    public void removeBy(ChronoUnit unit, int count) {
        repository.removeByUnitAndFrame(unit, count);
    }
}
