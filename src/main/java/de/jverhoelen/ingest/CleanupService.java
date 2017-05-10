package de.jverhoelen.ingest;

import com.ullink.slack.simpleslackapi.SlackSession;
import de.jverhoelen.currency.plot.CurrencyPlotService;
import de.jverhoelen.interaction.FatalErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CleanupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CleanupService.class);

    @Autowired
    private CurrencyPlotService plotService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private SlackSession slackSession;

    @Scheduled(fixedRate = 600000)
    public void cleanUpOldEntries() {
        try {
            plotService.deleteAllOlderThan(26);
        } catch (Exception ex) {
            eventPublisher.publishEvent(new FatalErrorEvent("Beim Säubern der Plot-Tabelle ist ein Fehler aufgetreten", ex));
        }
    }

//    @Scheduled(fixedRate = 180000)
    public void refreshSession() {
        try {
            slackSession.connect();
        } catch (IOException e) {
            LOGGER.error("Was not able to reconnect Slack Session to fresh it", e);
        }
    }
}
