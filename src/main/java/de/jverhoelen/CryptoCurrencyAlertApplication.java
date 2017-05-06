package de.jverhoelen;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class CryptoCurrencyAlertApplication {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoCurrencyAlertApplication.class);

    @Value("${slack.bot.token}")
    private String botAuthToken;

    public static void main(String[] args) {
        SpringApplication.run(CryptoCurrencyAlertApplication.class, args);
    }

    @Bean
    public SlackSession slackSession() {
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession(botAuthToken);
        try {
            session.connect();
        } catch (IOException e) {
            LOG.error("Slack session could not be created with token {}", botAuthToken, e);
        }

        return session;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
