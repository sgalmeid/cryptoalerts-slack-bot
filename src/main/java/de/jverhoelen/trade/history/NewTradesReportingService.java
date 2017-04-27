package de.jverhoelen.trade.history;

import de.jverhoelen.balance.notification.BalanceNotification;
import de.jverhoelen.balance.notification.BalanceNotificationService;
import de.jverhoelen.notification.SlackService;
import de.jverhoelen.trade.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NewTradesReportingService {

    @Autowired
    private SlackService slack;

    @Autowired
    private TradeHistoryService tradeHistory;

    @Autowired
    private BalanceNotificationService balanceNotificationService;

    @Value("${report.trades.interval.min}")
    private int intervalMinutes;

    @Scheduled(fixedRateString = "#{new Double(${report.trades.interval.min} * 60 * 1000).intValue()}")
    public void reportNewSells() {
        List<BalanceNotification> toNotify = balanceNotificationService.getAll();

        toNotify.stream().forEach(n -> {
            Map<String, List<Trade>> sellHistory = tradeHistory.getSellHistoryOf(n.getApiKey(), n.getSecretKey(), intervalMinutes);
            if (!sellHistory.isEmpty()) {
                String message = buildNewSellsMessage(sellHistory);
                slack.sendUserMessage(n.getSlackUser(), message);
            }
        });
    }

    private String buildNewSellsMessage(Map<String, List<Trade>> sellHistory) {
        Integer totalTradesSize = sellHistory.entrySet().stream().map(e -> e.getValue().size()).collect(Collectors.summingInt(i -> i.intValue()));
        StringBuilder builder = new StringBuilder("Es wurden *" + totalTradesSize + " neue Verkäufe* getätigt:");

        sellHistory.entrySet().forEach(currencySells -> {
            builder.append("\n&gt; " + currencySells.getValue().size() + "x " + currencySells.getKey().replace("_", "/"));
        });
        builder.append("\nAusführliche Trade-History: https://poloniex.com/tradeHistory");

        return builder.toString();
    }
}
