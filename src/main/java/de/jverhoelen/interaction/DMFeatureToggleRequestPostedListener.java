package de.jverhoelen.interaction;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import de.jverhoelen.notification.SlackService;
import org.springframework.context.ApplicationEventPublisher;

public class DMFeatureToggleRequestPostedListener implements SlackMessagePostedListener {

    private ApplicationEventPublisher publisher;
    private SlackService slack;

    public DMFeatureToggleRequestPostedListener(ApplicationEventPublisher publisher, SlackService slack) {
        this.publisher = publisher;
        this.slack = slack;
    }

    @Override
    public void onEvent(SlackMessagePosted slackMessagePosted, SlackSession slackSession) {
        slack.sendUserMessage("jonas", slackMessagePosted.getMessageContent() + " from " + slackMessagePosted.getSender().getUserName());
    }
}
