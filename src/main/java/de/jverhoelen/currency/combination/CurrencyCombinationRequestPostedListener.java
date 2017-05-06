package de.jverhoelen.currency.combination;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import de.jverhoelen.interaction.SlackBotCommand;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

public class CurrencyCombinationRequestPostedListener implements SlackMessagePostedListener {

    private ApplicationEventPublisher publisher;

    public CurrencyCombinationRequestPostedListener(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onEvent(SlackMessagePosted slackMessagePosted, SlackSession slackSession) {
        String channelName = slackMessagePosted.getChannel().getName();
        String senderUsername = slackMessagePosted.getSender().getUserName();

        SlackBotCommand cmd = new SlackBotCommand(slackMessagePosted);
        LoggerFactory.getLogger(CurrencyCombinationRequestPostedListener.class).info("{} came in", cmd);

        processRequest(cmd, channelName, senderUsername);
    }

    private void processRequest(SlackBotCommand command, String channelName, String senderUsername) {
        if (channelName.equals("general") || channelName.equals("admin")) {

            if (command.getCommand().equals("addcurrency")) {
                String crypto = command.getArgument("crypto");
                String exchange = command.getArgument("exchange");

                publisher.publishEvent(new CurrencyCombinationAddedEvent(crypto, exchange, senderUsername, channelName));
            }
        }
    }
}
