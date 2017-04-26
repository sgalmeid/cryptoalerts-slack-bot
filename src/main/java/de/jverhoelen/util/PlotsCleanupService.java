package de.jverhoelen.util;

import de.jverhoelen.currency.plot.CurrencyPlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PlotsCleanupService {

    @Autowired
    private CurrencyPlotService plotService;

    @Scheduled(fixedRate = 600000)
    public void cleanUpOldEntries() {
        plotService.deleteAllOlderThan(26);
    }
}
