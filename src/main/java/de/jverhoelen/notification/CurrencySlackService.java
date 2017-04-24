package de.jverhoelen.notification;

import de.jverhoelen.ingest.CurrencyCombination;
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
                String message = String.format(
                        "*%s* ist in den letzten %s %s um *%s %%* gewachsen (%s --> %s)",
                        combination.getCrypto().getFullName(),
                        frame.getFrame(),
                        frame.getUnit().toString(),
                        growth.getPercentage(),
                        growth.getBefore() + " " + combination.getExchange().name(),
                        growth.getAfter() + " " + combination.getExchange().name()
                );
                String cryptoCurrencyName = combination.getCrypto().getFullName().trim().toLowerCase();

                slackService.sendChannelMessage(cryptoCurrencyName, message);
                notificationsSent.put(notificationSetting, now);
            }
        }
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
