package de.jverhoelen.interaction;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import de.jverhoelen.balance.notification.BalanceNotification;
import de.jverhoelen.balance.notification.BalanceNotificationAddedEvent;
import de.jverhoelen.notification.SlackService;
import de.jverhoelen.timeframe.TimeFrame;
import de.jverhoelen.timeframe.TimeFrameAddedEvent;
import de.jverhoelen.trade.manual.PoloniexTradeService;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.StringUtils;

import java.time.temporal.ChronoUnit;

public class AdminMessagePostedListener implements SlackMessagePostedListener {

    private PoloniexTradeService tradeService;
    private SlackService slack;
    private String adminUsername;
    private ApplicationEventPublisher publisher;

    public AdminMessagePostedListener(PoloniexTradeService tradeService, SlackService slack, String adminUsername, ApplicationEventPublisher publisher) {
        this.tradeService = tradeService;
        this.slack = slack;
        this.adminUsername = adminUsername;
        this.publisher = publisher;
    }

    @Override
    public void onEvent(SlackMessagePosted slackMessagePosted, SlackSession slackSession) {
        String channelName = slackMessagePosted.getChannel().getName();
        String senderUsername = slackMessagePosted.getSender().getUserName();

        SlackBotCommand cmd = new SlackBotCommand(slackMessagePosted);
        LoggerFactory.getLogger(AdminMessagePostedListener.class).info("{} came in", cmd);

        processGeneral(cmd, channelName, senderUsername);
        processAdmin(cmd, channelName, senderUsername);
    }

    private void processAdmin(SlackBotCommand command, String channelName, String senderUsername) {
        if (channelName.equals("admin") && senderUsername.equals(adminUsername)) {

            if (command.getCommand().equals("adduser")) {
                String username = command.getArgument("user");
                String ownerParam = command.getArgument("owner");
                String owner = StringUtils.isEmpty(ownerParam) ? username : ownerParam;

                BalanceNotification balanceNotification = new BalanceNotification(username, owner, command.getArgument("key"), command.getArgument("secret"), true, true, true, true);
                publisher.publishEvent(new BalanceNotificationAddedEvent(balanceNotification));
                slack.sendChannelMessage(channelName, "Die API Credentials von @" + username + " wurden hinterlegt.");
            }
        }
    }

    TimeFrame timeFrameFromCommand(SlackBotCommand command) {
        return TimeFrame.of(
                ChronoUnit.valueOf(command.getArgument("unit").toUpperCase()),
                Integer.parseInt(command.getArgument("count")),
                Double.parseDouble(command.getArgument("treshold")),
                Double.parseDouble(command.getArgument("treshold"))
        );
    }

    private void processGeneral(SlackBotCommand command, String channelName, String senderUsername) {
        if ((channelName.equals("general") || channelName.equals("admin")) && senderUsername.equals(adminUsername)) {
            String workResult = "";

            if (command.getCommand().equals("timeframe")) {
                if (command.getSubCommand().equals("add")) {
                    TimeFrame timeFrame = timeFrameFromCommand(command);
                    publisher.publishEvent(new TimeFrameAddedEvent(timeFrame));
                    workResult = "Der neue Zeitinvervall würde hinzugefügt.";
                }

                slack.sendChannelMessage(channelName, "Dein Wunsch ist mein Befehl, @" + adminUsername + " \uD83D\uDC6E\uD83C\uDFFC. " + workResult);
            }
        }
    }
}
