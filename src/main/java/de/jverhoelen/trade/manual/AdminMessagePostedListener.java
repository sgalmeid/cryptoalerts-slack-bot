package de.jverhoelen.trade.manual;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import de.jverhoelen.balance.notification.BalanceNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AdminMessagePostedListener extends ContextAwareSlackMessagePostedListener {

    @Value("${admin.slack.name}")
    private String adminUsername;

    @Override
    public void onEvent(SlackMessagePosted slackMessagePosted, SlackSession slackSession) {
        String channelName = slackMessagePosted.getChannel().getName();
        String senderUsername = slackMessagePosted.getSender().getUserName();

        LOGGER.info("Channel {} user {}", channelName, senderUsername);

        processGeneral(slackMessagePosted, channelName, senderUsername);
        processAdmin(slackMessagePosted, channelName, senderUsername);
    }

    private void processAdmin(SlackMessagePosted slackMessagePosted, String channelName, String senderUsername) {
        if (channelName.equals("admin") && senderUsername.equals(adminUsername)) {
            String msg = slackMessagePosted.getMessageContent();
            String[] split = msg.split(" ");

            String command = split[0].toLowerCase();
            String params = split[1].toLowerCase();
            Map<String, String> paramMap = extractParams(params);

            if (command.equals("addUser")) {
                String username = paramMap.get("user");

                balanceNotifications.add(new BalanceNotification(username, paramMap.get("key"), paramMap.get("secret"), true, true, true));
                slack.sendChannelMessage(channelName, "Die API Credentials des Slack Users " + username + " wurden hinterlegt.");
            }
        }
    }

    private void processGeneral(SlackMessagePosted slackMessagePosted, String channelName, String senderUsername) {
        if ((channelName.equals("general") || channelName.equals("admin")) && senderUsername.equals(adminUsername)) {
            String msg = slackMessagePosted.getMessageContent();
            String[] split = msg.split(" ");
            String command = split[0].toLowerCase();
            String subCommand = split[1].toLowerCase();
            String params = split[2].toLowerCase();

            String workResult = "";

            if (command.equals("timeframe")) {
                Map<String, String> paramMap = extractParams(params);

                if (subCommand.equals("delete")) {
//                    timeFrameService.removeBy(ChronoUnit.valueOf(paramMap.get("unit")), Integer.parseInt(paramMap.get("count")));
                    workResult = "Der zu überwachende Zeitintervall wurde gelöscht.";

                } else if (subCommand.equals("add")) {
//                    timeFrameService.add(TimeFrame.of(
//                            ChronoUnit.valueOf(paramMap.get("unit")),
//                            Integer.parseInt(paramMap.get("count")),
//                            Double.parseDouble(paramMap.get("treshold")),
//                            Double.parseDouble(paramMap.get("treshold"))
//                    ));
                    workResult = "Der neue Zeitinvervall würde hinzugefügt.";
                }

                slack.sendChannelMessage(channelName, "Dein Wunsch ist mein Befehl, " + adminUsername + " \uD83D\uDC6E\uD83C\uDFFC. " + workResult);
            }
        }
    }

    private Map<String, String> extractParams(String params) {
        HashMap<String, String> result = new HashMap<>();
        String[] kvPairs = params.split(",");

        for (String pair : kvPairs) {
            String[] kvSplit = pair.split("=");
            result.put(kvSplit[0], kvSplit[1]);
        }

        return result;
    }
}
