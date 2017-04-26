package de.jverhoelen.balance.notification;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class BalanceNotification {

    @Id
    @GeneratedValue
    private long id;

    private String slackUser;
    private String apiKey;
    private String secretKey;

    public BalanceNotification(String slackUser, String apiKey, String secretKey) {
        this.slackUser = slackUser;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    public BalanceNotification() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSlackUser() {
        return slackUser;
    }

    public void setSlackUser(String slackUser) {
        this.slackUser = slackUser;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
