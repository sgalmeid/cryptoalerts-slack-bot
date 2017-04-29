package de.jverhoelen.trade.history;

import de.jverhoelen.balance.notification.BalanceNotification;
import de.jverhoelen.balance.notification.BalanceNotificationService;
import de.jverhoelen.notification.SlackService;
import de.jverhoelen.trade.Trade;
import de.jverhoelen.trade.TradeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
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

        Predicate<BalanceNotification> shouldBeNotifiedOnSell = person -> person.isReportSells();
        toNotify.stream()
                .filter(shouldBeNotifiedOnSell)
                .forEach(n -> {
                    Map<String, List<Trade>> sellHistory = tradeHistory.getSellHistoryOf(n.getApiKey(), n.getSecretKey(), intervalMinutes);
                    if (!sellHistory.isEmpty()) {
                        String message = buildNewTradeMessage(sellHistory, TradeType.sell);
                        slack.sendUserMessage(n.getSlackUser(), message);
                    }
                });

        toNotify.stream()
                .filter(person -> person.isReportBuys())
                .forEach(n -> {
                    Map<String, List<Trade>> buyHistory = tradeHistory.getBuyHistoryOf(n.getApiKey(), n.getSecretKey(), intervalMinutes);
                    if (!buyHistory.isEmpty()) {
                        String message = buildNewTradeMessage(buyHistory, TradeType.buy);
                        slack.sendUserMessage(n.getSlackUser(), message);
                    }
                });
    }

    private String buildNewTradeMessage(Map<String, List<Trade>> sellHistory, TradeType tradeType) {
        Integer totalTradesSize = sellHistory.entrySet().stream().map(e -> e.getValue().size()).collect(Collectors.summingInt(i -> i.intValue()));
        StringBuilder builder = new StringBuilder("Es wurden *" + totalTradesSize + " neue " + tradeType.getNounPlural() + "* getätigt:");

        sellHistory.entrySet().forEach(currencySells -> {
            List<Trade> trades = currencySells.getValue();
            builder.append("\n&gt; " + trades.size() + "x " + formatCurrencyCombi(currencySells.getKey(), tradeType) + ": " + formatCurrencyTradesSummary(trades, tradeType));
        });
        builder.append("\nAusführliche Trade-History: https://poloniex.com/tradeHistory");

        return builder.toString();
    }

    private String formatCurrencyTradesSummary(List<Trade> trades, TradeType tradeType) {
        List<Double> totals = trades.stream().map(t -> t.getTotal()).collect(Collectors.toList());
        List<Double> amounts = trades.stream().map(t -> t.getAmount()).collect(Collectors.toList());
        List<Double> rates = trades.stream().map(t -> t.getRate()).collect(Collectors.toList());

        Double total = totals.stream().reduce(0.0, Double::sum);
        Double amount = amounts.stream().reduce(0.0, Double::sum);
        Double avgRate = rates.stream().reduce(0.0, Double::sum) / trades.size();

        return amount + " in " + total + " " + tradeType.getVerbPast() + " (Ø-Rate: " + avgRate + ")";
    }

    private String formatCurrencyCombi(String combination, TradeType tradeType) {
        String[] split = combination.split("_");

        if (tradeType.equals(TradeType.buy)) {
            return split[0] + " ➡ " + split[1];
        } else {
            return split[1] + " ➡ " + split[0];
        }
    }
}
