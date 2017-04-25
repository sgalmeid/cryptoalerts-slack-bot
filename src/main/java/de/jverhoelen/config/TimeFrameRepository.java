package de.jverhoelen.config;

import de.jverhoelen.util.repo.Repository;
import org.springframework.stereotype.Component;

@Component
public interface TimeFrameRepository extends Repository<TimeFrame, Long> {
}
