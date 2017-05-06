package de.jverhoelen.trade.manual;

import de.jverhoelen.notification.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ManualTradeService {

    @Autowired
    private SlackService slack;

    @Autowired
    private SellBuyMessagePostedListener saleListener;

    @PostConstruct
    public void init() {
        slack.registerMessagePostedListener(saleListener);
    }
}
