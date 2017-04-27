package de.jverhoelen.notification.currency;

import de.jverhoelen.config.TimeFrame;
import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.notification.CourseAlteration;
import de.jverhoelen.notification.Growth;
import de.jverhoelen.notification.SlackService;
import de.jverhoelen.notification.statistics.StatisticsSlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencySlackService {

    private Map<CurrencyNotificationSetting, LocalDateTime> notificationsSent = new HashMap<>();

    @Autowired
    private SlackService slackService;

    public void sendCurrencyNews(CurrencyCombination combination, CourseAlteration course, TimeFrame frame) {
        CurrencyNotificationSetting notificationSetting = new CurrencyNotificationSetting(combination, frame.getInMinutes());

        if (course.isNotifiable(frame)) {

            LocalDateTime lastSent = notificationsSent.get(notificationSetting);
            LocalDateTime now = LocalDateTime.now();

            if (shouldBeAlertedFirstOrAgain(frame, lastSent, now)) {
                performCurrencyAlert(combination, course, frame, notificationSetting, now);
            }
        }
    }

    private void performCurrencyAlert(CurrencyCombination combination, CourseAlteration course, TimeFrame frame, CurrencyNotificationSetting notificationSetting, LocalDateTime now) {
        String message = buildMessage(combination, course, frame);
        String cryptoCurrencyName = combination.getCrypto().getFullName().trim().toLowerCase();

        slackService.sendChannelMessage(cryptoCurrencyName, message);
        notificationsSent.put(notificationSetting, now);
    }

    private String buildMessage(CurrencyCombination combination, CourseAlteration courseAlteration, TimeFrame frame) {
        String exchangeName = combination.getExchange().name();
        Growth g = courseAlteration.getGrowth();
        Growth mvG = courseAlteration.getMarketVolumeGrowth();

        StringBuilder builder = new StringBuilder()
                .append("\uD83D\uDCE2 *" + combination.getCrypto().getFullName() + "* der letzten " + frame.getFrame() + " " + frame.getUnit().toString() + " \n")
                .append("&gt; _Wachstum:_ " + g.getRoundPercentage() + " % " + g.getActionPerformed() + "\n")
                .append("&gt; _Verlauf:_ " + g.buildCourse(exchangeName) + "\n")
                .append("&gt; _Marktvolumen:_ " + mvG.getRoundPercentage() + " % " + mvG.getActionPerformed() + "\n")
                .append("&gt; Mehr Infos: " + StatisticsSlackService.getPoloniexExchangeLink(combination.getExchange(), combination.getCrypto()));

        return builder.toString();
    }

    private boolean shouldBeAlertedFirstOrAgain(TimeFrame frame, LocalDateTime lastSent, LocalDateTime now) {
        return lastSent == null || shouldBeAlertedAgain(frame, lastSent, now);
    }

    private boolean shouldBeAlertedAgain(TimeFrame frame, LocalDateTime lastSent, LocalDateTime now) {
        LocalDateTime toBeNotifiedTime = lastSent.plusMinutes(frame.getInMinutes());
        return now.isAfter(toBeNotifiedTime);
    }
}
