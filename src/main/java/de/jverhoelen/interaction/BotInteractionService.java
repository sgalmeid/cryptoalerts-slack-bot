package de.jverhoelen.interaction;

import com.ullink.slack.simpleslackapi.SlackSession;
import de.jverhoelen.balance.notification.BalanceNotificationService;
import de.jverhoelen.currency.combination.CurrencyCombinationRequestPostedListener;
import de.jverhoelen.notification.SlackService;
import de.jverhoelen.trade.manual.PoloniexTradeService;
import de.jverhoelen.trade.manual.SellBuyMessagePostedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BotInteractionService {

    @Value("${admin.slack.name}")
    private String adminUsername;

    private SlackSession slackSession;
    private SlackService slackService;
    private PoloniexTradeService tradeService;
    private BalanceNotificationService balanceNotifications;
    private ApplicationEventPublisher publisher;

    @Autowired
    public BotInteractionService(SlackService slackService, PoloniexTradeService tradeService, BalanceNotificationService balanceNotifications, SlackSession slackSession, ApplicationEventPublisher publisher) {
        this.slackService = slackService;
        this.tradeService = tradeService;
        this.balanceNotifications = balanceNotifications;
        this.slackSession = slackSession;
        this.publisher = publisher;
    }

    @PostConstruct
    public void init() {
        slackSession.addMessagePostedListener(new SellBuyMessagePostedListener(balanceNotifications, tradeService, slackService));
        slackSession.addMessagePostedListener(new AdminMessagePostedListener(tradeService, slackService, adminUsername, publisher));
        slackSession.addMessagePostedListener(new CurrencyCombinationRequestPostedListener(publisher));
        slackSession.addMessagePostedListener(new DMFeatureToggleRequestPostedListener(publisher, slackService));
    }
}
