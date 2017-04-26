package de.jverhoelen.notification;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class SlackService {

    private static final Logger LOG = LoggerFactory.getLogger(SlackService.class);

    @Value("${slack.bot.token}")
    private String botAuthToken;

    @Value("${environment}")
    private String environment;

    private SlackSession session;

    @PostConstruct
    public void init() {
        this.session = createSession();
        String environmentResult = isDevelopmentEnvironment()
                ? "the DEV environment and no Slack notifications will be sent"
                : "the PROD environment and Slack notifications will be sent as usual";
        LOG.info("Environment {} was detected, so it is {}", environment, environmentResult);
    }

    private SlackSession createSession() {
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession(botAuthToken);
        try {
            session.connect();
        } catch (IOException e) {
            LOG.error("Slack session could not be created with token {}", botAuthToken, e);
        }

        return session;
    }

    public void sendChannelMessage(String channelName, String message) {
        SlackChannel channel = session.findChannelByName(channelName);

        if (channel != null) {
            LOG.info("Send message {} to channel {}", message, channelName);

            if (!isDevelopmentEnvironment()) {
                session.sendMessage(channel, message);
            }
        } else {
            throw new RuntimeException("Channel " + channelName + " was not recognized!");
        }
    }

    public void sendUserMessage(String username, String message) {
        SlackUser user = session.findUserByUserName(username);

        if (user != null) {
            LOG.info("Send message {} to username {}", message, user);

            if (!isDevelopmentEnvironment()) {
                session.sendMessageToUser(user, message, null);
            }
        } else {
            throw new RuntimeException("User " + username + " was not recognized!");
        }
    }

    private boolean isDevelopmentEnvironment() {
        return environment.toLowerCase().equals("dev");
    }
}