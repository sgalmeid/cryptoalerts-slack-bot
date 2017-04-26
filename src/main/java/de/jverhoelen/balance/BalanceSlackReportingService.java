package de.jverhoelen.balance;

import de.jverhoelen.balance.notification.BalanceNotification;
import de.jverhoelen.balance.notification.BalanceNotificationService;
import de.jverhoelen.balance.plot.BalancePlot;
import de.jverhoelen.balance.plot.BalancePlotService;
import de.jverhoelen.notification.Growth;
import de.jverhoelen.notification.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BalanceSlackReportingService {

    private static final int MINUTES_OF_A_DAY = 24 * 60;

    @Value("${report.balance.interval.min}")
    private int reportingRateMinutes;

    @Autowired
    private PoloniexBalanceService balanceService;

    @Autowired
    private SlackService slackService;

    @Autowired
    private BalanceNotificationService notificationService;

    @Autowired
    private BalancePlotService balancePlotService;

    @Scheduled(fixedRateString = "#{new Double(${report.balance.interval.min} * 60 * 1000).intValue()}")
    public void reportToAll() {
        notificationService.getAll().stream().forEach(person -> reportFor(person));
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void reportDailyGrowthToAll() {
        notificationService.getAll().stream().forEach(person -> reportDailyFor(person));
    }

    private void reportDailyFor(BalanceNotification person) {
        BalancePlot oneDayBefore = balancePlotService.getFromMinutesAgo(person.getSlackUser(), MINUTES_OF_A_DAY);

        if (oneDayBefore != null) {
            Map<String, Balance> balancesOverZero = getBalancesOverZero(person);
            double totalBitcoins = balanceService.getBtcSumOfBalances(balancesOverZero);
            persistBalancePlot(person, totalBitcoins);

            String message = buildDailyReportMessage(balancesOverZero, totalBitcoins, oneDayBefore);
            slackService.sendUserMessage(person.getSlackUser(), message);
        }
    }

    private String buildDailyReportMessage(Map<String, Balance> balances, double totalBitcoins, BalancePlot yesterdayPlot) {
        Growth btcGrowth = new Growth(yesterdayPlot.getBtcValue(), totalBitcoins);

        StringBuilder builder = new StringBuilder("⏰ *Tagesreport*");

        builder.append("\nDein aktuelles Poloniex Guthaben beträgt *" + totalBitcoins + "* BTC");
        builder.append("\nDas sind " + btcGrowth.getPercentage() + " % " + btcGrowth.getActionPerformed() + ") im Vergleich zu gestern");

        balances.entrySet().stream().forEach(b -> {
            Balance balance = b.getValue();
            String currency = b.getKey();
            builder.append("\n&gt; " + balance.getAvailable() + " " + currency + " (" + balance.getBtcValue() + " BTC)");
        });

        return builder.toString();
    }

    private void reportFor(BalanceNotification person) {
        Map<String, Balance> balancesOverZero = getBalancesOverZero(person);
        double totalBitcoins = balanceService.getBtcSumOfBalances(balancesOverZero);
        BalancePlot lastPlot = balancePlotService.getFromMinutesAgo(person.getSlackUser(), reportingRateMinutes);

        persistBalancePlot(person, totalBitcoins);

        String message = buildMessage(balancesOverZero, totalBitcoins, lastPlot);
        slackService.sendUserMessage(person.getSlackUser(), message);
    }

    private String buildMessage(Map<String, Balance> balances, double totalBitcoins, BalancePlot lastPlot) {
        Growth btcGrowth = new Growth(0, 0);
        if (lastPlot != null) {
            btcGrowth = new Growth(lastPlot.getBtcValue(), totalBitcoins);
        }

        StringBuilder builder = new StringBuilder(
                "Dein aktuelles Poloniex Guthaben beträgt *" + totalBitcoins + "* BTC (" + btcGrowth.getPercentage() + " % " + btcGrowth.getActionPerformed() + ")"
        );

        balances.entrySet().stream().forEach(b -> {
            Balance balance = b.getValue();
            String currency = b.getKey();
            builder.append("\n&gt; " + balance.getAvailable() + " " + currency + " (" + balance.getBtcValue() + " BTC)");
        });

        builder.append("\nDeposit/Withdraw: https://poloniex.com/balances");

        return builder.toString();
    }

    private Map<String, Balance> getBalancesOverZero(BalanceNotification person) {
        Map<String, Balance> balances = balanceService.getBalancesOf(person.getApiKey(), person.getSecretKey());
        return filterOutEmpties(balances);
    }

    private Map<String, Balance> filterOutEmpties(Map<String, Balance> balances) {
        return balances.entrySet().stream().filter(balance -> balance.getValue().getBtcValue() > 0).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    private void persistBalancePlot(BalanceNotification person, double totalBitcoins) {
        balancePlotService.add(BalancePlot.from(totalBitcoins, person.getSlackUser()));
    }
}
