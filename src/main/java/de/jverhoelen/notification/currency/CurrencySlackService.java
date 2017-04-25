package de.jverhoelen.notification.currency;

import de.jverhoelen.config.TimeFrame;
import de.jverhoelen.currency.CurrencyCombination;
import de.jverhoelen.notification.Growth;
import de.jverhoelen.notification.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencySlackService {

    private Map<CurrencyNotificationSetting, LocalDateTime> notificationsSent = new HashMap<>();

    @Autowired
    SlackService slackService;

    public void sendCurrencyNews(CurrencyCombination combination, Growth growth, TimeFrame frame) {
        CurrencyNotificationSetting notificationSetting = new CurrencyNotificationSetting(combination, frame.getInMinutes());

        if (isNotifiableCourse(growth, frame)) {

            LocalDateTime lastSent = notificationsSent.get(notificationSetting);
            LocalDateTime now = LocalDateTime.now();

            if (shouldBeAlertedFirstOrAgain(frame, lastSent, now)) {
                performCurrencyAlert(combination, growth, frame, notificationSetting, now);
            }
        }
    }

    private void performCurrencyAlert(CurrencyCombination combination, Growth growth, TimeFrame frame, CurrencyNotificationSetting notificationSetting, LocalDateTime now) {
        String actionPerformed = growth.getPercentage() > 0 ? "⬆️" : "⬇️";
        String message = String.format(
                "*%s* hat sich in den letzten %s %s um *%s %%* verändert (%s): %s auf %s",
                combination.getCrypto().getFullName(),
                frame.getFrame(),
                frame.getUnit().toString(),
                growth.getPercentage(),
                actionPerformed,
                growth.getBefore() + " " + combination.getExchange().name(),
                growth.getAfter() + " " + combination.getExchange().name()
        );
        String cryptoCurrencyName = combination.getCrypto().getFullName().trim().toLowerCase();

        slackService.sendChannelMessage(cryptoCurrencyName, message);
        notificationsSent.put(notificationSetting, now);
    }

    private boolean shouldBeAlertedFirstOrAgain(TimeFrame frame, LocalDateTime lastSent, LocalDateTime now) {
        return lastSent == null || shouldBeAlertedAgain(frame, lastSent, now);
    }

    private boolean shouldBeAlertedAgain(TimeFrame frame, LocalDateTime lastSent, LocalDateTime now) {
        LocalDateTime toBeNotifiedTime = lastSent.plusMinutes(frame.getInMinutes() / 2);
        return now.isAfter(toBeNotifiedTime);
    }

    private boolean isNotifiableCourse(Growth growth, TimeFrame frame) {
        return growth.getPercentage() >= frame.getGainThreshold() || growth.getPercentage() <= (frame.getLossThreshold() * -1);
    }
}
