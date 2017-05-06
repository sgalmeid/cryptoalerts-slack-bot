package de.jverhoelen.trade.manual;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import de.jverhoelen.balance.notification.BalanceNotification;
import de.jverhoelen.currency.CryptoCurrency;
import org.springframework.stereotype.Component;

@Component
public class SellBuyMessagePostedListener extends ContextAwareSlackMessagePostedListener {

    @Override
    public void onEvent(SlackMessagePosted slackMessagePosted, SlackSession slackSession) {
        SlackChannel channel = slackMessagePosted.getChannel();
        CryptoCurrency currency = CryptoCurrency.byName(channel.getName());
        SlackUser sender = slackMessagePosted.getSender();

        if (currency != null) {
            BalanceNotification notificationSettings = balanceNotifications.getBySlackUser(sender.getUserName());

            if (notificationSettings != null) {
                String content = slackMessagePosted.getMessageContent();
                String[] commandSplit = content.split(" ");
                String command = commandSplit[0];
                String commandParams = commandSplit[1];

                if (commandSplit.length == 2) {
                    processCommand(channel, currency, sender, command, commandParams);
                }
            }
        }
    }

    private void processCommand(SlackChannel channel, CryptoCurrency currency, SlackUser sender, String command, String commandParams) {
        double amount;

        switch (command.toLowerCase()) {
            case "sell":
                if (commandParams.contains("%")) {
                    double percentage = Double.parseDouble(commandParams.replace("%", "").replaceAll("\\s+", ""));
                    double decimal = percentage / 100;

                    amount = decimal * 5000;

                    // todo fetch current amount in portfolio to calculate how much the amount to sell is
                } else {
                    amount = Double.parseDouble(commandParams.replaceAll("\\s+", ""));
                }

                // todo sell the amount

                slack.sendChannelMessage(channel.getName(), "@" + sender.getUserName() + ", ich habe " + amount + " " + currency.name() + " für Dich *verkauft*.");
                break;

            case "buy":
                amount = Double.parseDouble(commandParams.replaceAll("\\s+", ""));
                slack.sendChannelMessage(channel.getName(), "@" + sender.getUserName() + ", ich habe" + amount + " " + currency.name() + " für Dich *gekauft*.");
                break;

        }
    }
}
