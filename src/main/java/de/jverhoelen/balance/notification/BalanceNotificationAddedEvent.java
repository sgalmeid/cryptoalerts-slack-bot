package de.jverhoelen.balance.notification;

public class BalanceNotificationAddedEvent {

    private BalanceNotification balanceNotification;

    public BalanceNotificationAddedEvent(BalanceNotification balanceNotification) {
        this.balanceNotification = balanceNotification;
    }

    public BalanceNotification getBalanceNotification() {
        return balanceNotification;
    }
}
