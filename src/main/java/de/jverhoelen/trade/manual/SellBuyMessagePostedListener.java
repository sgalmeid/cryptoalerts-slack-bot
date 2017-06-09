package de.jverhoelen.trade.manual;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import de.jverhoelen.balance.notification.BalanceNotification;
import de.jverhoelen.balance.notification.BalanceNotificationService;
import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.notification.SlackService;
import de.jverhoelen.interaction.SlackBotCommand;

public class SellBuyMessagePostedListener implements SlackMessagePostedListener {

    private PoloniexTradeService tradeService;
    private SlackService slack;
    private BalanceNotificationService balanceNotifications;
    
    public SellBuyMessagePostedListener(BalanceNotificationService balanceNotifications, PoloniexTradeService tradeService, SlackService slack) {
        this.balanceNotifications = balanceNotifications;
        this.tradeService = tradeService;
        this.slack = slack;
    }

    @Override
    public void onEvent(SlackMessagePosted slackMessagePosted, SlackSession slackSession) {
        SlackChannel channel = slackMessagePosted.getChannel();
        CryptoCurrency currency = CryptoCurrency.byFullName(channel.getName());
        SlackUser sender = slackMessagePosted.getSender();
        SlackBotCommand cmd = new SlackBotCommand(slackMessagePosted);

        if (currency != null) {
            BalanceNotification notificationSettings = balanceNotifications.getBySlackUserAndOwner(sender.getUserName(), sender.getUserName());

            if (notificationSettings != null) {

                if (cmd.isArgumentsSecondSegment()) {
                    processCommand(channel, currency, sender, cmd);
                }
            }
        }
    }

    private void processCommand(SlackChannel channel, CryptoCurrency currency, SlackUser sender, SlackBotCommand cmd) {
        double amount;

        switch (cmd.getCommand()) {
            case "sell":
                if (cmd.argumentsContain("%")) {
                    double percentage = Double.parseDouble(cmd.getRawArguments().replace("%", "").replaceAll("\\s+", ""));
                    double decimal = percentage / 100;

                    amount = decimal * 5000;

                    // todo fetch current amount in portfolio to calculate how much the amount to sell is
                } else {
                    amount = Double.parseDouble(cmd.getRawArguments().replaceAll("\\s+", ""));
                }

                // todo sell the amount

                slack.sendChannelMessage(channel.getName(), "@" + sender.getUserName() + ", ich habe " + amount + " " + currency.name() + " für Dich *verkauft*.");
                break;

            case "buy":
                amount = Double.parseDouble(cmd.getRawArguments().replaceAll("\\s+", ""));
                slack.sendChannelMessage(channel.getName(), "@" + sender.getUserName() + ", ich habe" + amount + " " + currency.name() + " für Dich *gekauft*.");
                break;

        }
    }
}
