package de.jverhoelen.ingest;

import de.jverhoelen.currency.plot.CurrencyPlotService;
import de.jverhoelen.interaction.FatalErrorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OldPlotsCleanupService {

    @Autowired
    private CurrencyPlotService plotService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 600000)
    public void cleanUpOldEntries() {
        try {
            plotService.deleteAllOlderThan(26);
        } catch (Exception ex) {
            eventPublisher.publishEvent(new FatalErrorEvent("Beim Säubern der Plot-Tabelle ist ein Fehler aufgetreten", ex));
        }
    }
}
