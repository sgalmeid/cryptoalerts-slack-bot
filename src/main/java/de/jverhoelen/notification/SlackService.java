package de.jverhoelen.notification;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import de.jverhoelen.currency.combination.CurrencyCombinationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlackService {

    private static final Logger LOG = LoggerFactory.getLogger(SlackService.class);

    @Value("${slack.bot.token}")
    private String botAuthToken;

    @Value("${environment}")
    private String environment;

    @Autowired
    private SlackSession session;

    @Autowired
    private CurrencyCombinationService currencyCombinations;

    @PostConstruct
    public void init() {
        String environmentResult = isDevelopmentEnvironment()
                ? "the DEV environment and no Slack notifications will be sent"
                : "the PROD environment and Slack notifications will be sent as usual";
        LOG.info("Environment {} was detected, so it is {}", environment, environmentResult);

        joinChannels(getRequiredChannelNames());
    }

    public void refreshSlackSession() {
        session.refetchUsers();
    }

    public void sendUserMessage(String username, String message) {
        SlackUser user = session.findUserByUserName(username);

        if (user != null) {
            LOG.info("Send message {} to username {}", message, username);

            if (!isDevelopmentEnvironment()) {
                session.sendMessageToUser(user, message, null);
            }
        } else {
            throw new RuntimeException("User " + username + " was not recognized!");
        }
    }

    public String getChannelId(String channelName) {
        return session.findChannelByName(channelName).getId();
    }

    public String getFormattedChannelLink(String channelName) {
        return "<#" + getChannelId(channelName) + "|" + channelName + ">";
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

    public boolean isMemberOfChannel(String username, String channelName) {
        SlackUser user = session.findUserByUserName(username);
        SlackChannel channel = session.findChannelByName(channelName);

        if (user != null && channel != null) {
            return channel.getMembers().contains(user);
        }

        throw new RuntimeException("User " + username + " or channel " + channelName + " was not recognized!");
    }

    public boolean channelExists(String channelName) {
        return session.findChannelByName(channelName) != null;
    }

    List<String> getRequiredChannelNames() {
        List<String> requiredChannelNames = currencyCombinations.getAll().stream()
                .map(cc -> cc.getCrypto().getFullName().toLowerCase())
                .collect(Collectors.toList());
        requiredChannelNames.addAll(Arrays.asList("tages-statistik", "statistiken", "markt-kapitalisierung"));
        return requiredChannelNames;
    }

    void joinChannels(List<String> channelNames) {
        channelNames.stream().forEach(channel -> session.joinChannel(channel));
    }

    boolean isDevelopmentEnvironment() {
        return environment.toLowerCase().equals("dev");
    }
}