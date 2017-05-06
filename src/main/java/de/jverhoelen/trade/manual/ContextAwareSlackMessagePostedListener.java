package de.jverhoelen.trade.manual;

import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import de.jverhoelen.balance.notification.BalanceNotificationService;
import de.jverhoelen.config.TimeFrameService;
import de.jverhoelen.notification.SlackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ContextAwareSlackMessagePostedListener implements SlackMessagePostedListener {

    protected final Logger LOGGER = LoggerFactory.getLogger(ContextAwareSlackMessagePostedListener.class);

    @Autowired
    protected BalanceNotificationService balanceNotifications;

    @Autowired
    protected PoloniexTradeService tradeService;

    @Autowired
    protected SlackService slack;

    @Autowired
    protected TimeFrameService timeFrameService;
}
