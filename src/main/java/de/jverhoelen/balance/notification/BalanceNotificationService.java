package de.jverhoelen.balance.notification;

import de.jverhoelen.util.repo.AbstractRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceNotificationService extends AbstractRepositoryService<BalanceNotification, Long> {

    @Autowired
    private BalanceNotificationRepository repository;

    public BalanceNotification getBySlackUserAndOwner(String name, String owner) {
        return repository.findBySlackUserAndOwnerName(name, owner);
    }
}
