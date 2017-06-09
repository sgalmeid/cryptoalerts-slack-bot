package de.jverhoelen.balance.notification;

import de.jverhoelen.util.repo.Repository;
import org.springframework.stereotype.Component;

@Component
public interface BalanceNotificationRepository extends Repository<BalanceNotification, Long> {

    BalanceNotification findBySlackUserAndOwnerName(String slackUser, String owner);
}
