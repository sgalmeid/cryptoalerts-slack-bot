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
    private String ownerName;
    private String apiKey;
    private String secretKey;

    private boolean reportBuys;
    private boolean reportSells;
    private boolean reportBalance;

    public BalanceNotification(String slackUser, String ownerName, String apiKey, String secretKey, boolean reportBuys, boolean reportSells, boolean reportBalance) {
        this.slackUser = slackUser;
        this.ownerName = ownerName;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.reportBuys = reportBuys;
        this.reportSells = reportSells;
        this.reportBalance = reportBalance;
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

    public boolean isReportBuys() {
        return reportBuys;
    }

    public void setReportBuys(boolean reportBuys) {
        this.reportBuys = reportBuys;
    }

    public boolean isReportSells() {
        return reportSells;
    }

    public void setReportSells(boolean reportSells) {
        this.reportSells = reportSells;
    }

    public boolean isReportBalance() {
        return reportBalance;
    }

    public void setReportBalance(boolean reportBalance) {
        this.reportBalance = reportBalance;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
