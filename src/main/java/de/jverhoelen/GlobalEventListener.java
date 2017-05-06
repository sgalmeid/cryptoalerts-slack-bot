package de.jverhoelen;

import de.jverhoelen.balance.notification.BalanceNotificationService;
import de.jverhoelen.timeframe.TimeFrameService;
import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.currency.combination.CurrencyCombinationAddedEvent;
import de.jverhoelen.currency.combination.CurrencyCombinationService;
import de.jverhoelen.notification.SlackService;
import de.jverhoelen.balance.notification.BalanceNotificationAddedEvent;
import de.jverhoelen.interaction.FatalErrorEvent;
import de.jverhoelen.timeframe.TimeFrameAddedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GlobalEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalEventListener.class);

    @Autowired
    private BalanceNotificationService balanceNotifications;

    @Autowired
    private SlackService slackService;

    @Autowired
    private TimeFrameService timeFrames;

    @Autowired
    private CurrencyCombinationService currencyCombinations;

    @Value("${admin.slack.name}")
    private String slackAdminName;

    @EventListener
    public void onTimeFrameAdded(TimeFrameAddedEvent event) {
        timeFrames.add(event.getTimeFrame());
    }

    @EventListener
    public void onFatalError(FatalErrorEvent event) {
        String message = "Es ist ein Fehler aufgetreten:\n" + event.getDescription();

        if (event.getThrowable() == null) {
            LOGGER.error(message);
        } else {
            LOGGER.error(message, event.getThrowable());
        }

        String additionalErrorsKnown = (event.getThrowable() == null || event.getThrowable().getMessage() == null) ? "" : event.getThrowable().getMessage();
        slackService.sendChannelMessage("errors", message + additionalErrorsKnown);
    }

    @EventListener
    public void onCurrencyCombinationAddRequest(CurrencyCombinationAddedEvent event) {
        CurrencyCombination combination = event.buildCurrencyCombination();

        if (combination != null) {
            CurrencyCombination lookup = currencyCombinations.find(combination.getCrypto(), combination.getExchange());

            if (lookup == null) {
                currencyCombinations.add(combination);
                slackService.sendChannelMessage(event.getChannelName(), "Die Währung " + combination.getCrypto().getFullName() + " wird jetzt mit ihrem " + combination.getExchange().getFullName() + "-Wert in #" + combination.getCrypto().getFullName().toLowerCase() + " aufgezeichnet.\n@" + slackAdminName + " muss allerdings noch #" + combination.getCrypto().getFullName().toLowerCase() + " erstellen *und mich einladen*. Ich hoffe er vergisst es nicht \uD83D\uDCA9");
            } else {
                slackService.sendChannelMessage(event.getChannelName(), "Augen auf @" + event.getRequesterUsername() + ", für " + getName(event) + " gibt es schon Aufzeichnungen und den Channel #" + combination.getCrypto().getFullName().toLowerCase());
            }
        } else {
            slackService.sendChannelMessage(event.getChannelName(), "Die Währungskombination " + getName(event) + " steht leider nicht zur Verfügung, @" + event.getRequesterUsername());
        }
    }

    private String getName(CurrencyCombinationAddedEvent event) {
        return event.getCrypto() + "/" + event.getExchange();
    }

    @EventListener
    public void onBalanceNotificationAdded(BalanceNotificationAddedEvent event) {
        balanceNotifications.add(event.getBalanceNotification());
    }
}
