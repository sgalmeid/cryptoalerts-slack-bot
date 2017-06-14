package de.jverhoelen.balance;

import de.jverhoelen.balance.notification.BalanceNotification;
import de.jverhoelen.balance.notification.BalanceNotificationService;
import de.jverhoelen.balance.plot.BalancePlot;
import de.jverhoelen.balance.plot.BalancePlotService;
import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.currency.plot.CurrencyPlot;
import de.jverhoelen.currency.plot.CurrencyPlotService;
import de.jverhoelen.interaction.FatalErrorEvent;
import de.jverhoelen.notification.Growth;
import de.jverhoelen.notification.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BalanceSlackReportingService {

    @Value("${report.balance.interval.min}")
    private int reportingRateMinutes;

    @Autowired
    private PoloniexBalanceService balanceService;

    @Autowired
    private SlackService slack;

    @Autowired
    private BalanceNotificationService notificationService;

    @Autowired
    private BalancePlotService balancePlotService;

    @Autowired
    private CurrencyPlotService currencyPlotService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRateString = "#{new Double(${report.balance.interval.min} * 60 * 1000).intValue()}", initialDelay = (60 * 1000 * 3))
    public void reportToAll() {
        CurrencyPlot btcDollar = currencyPlotService.findLastOf(CurrencyCombination.of(CryptoCurrency.BTC, ExchangeCurrency.USDT));

        notificationService.getAll().stream()
                .forEach(person -> {
                    if (person.isReportBalance()) {
                        reportFor(person, btcDollar);
                    }
                    if (person.isReportChannels()) {
                        suggestChannelsFor(person);
                    }
                });
    }

    private void reportFor(BalanceNotification person, CurrencyPlot btcDollar) {
        try {
            Map<String, Balance> balancesOverZero = getBalancesOverZero(person);
            double totalBtc = balanceService.getBtcSumOfBalances(balancesOverZero);
            BalancePlot lastPlot = balancePlotService.getLast(person.getSlackUser(), person.getOwnerName());

            persistBalancePlot(person, totalBtc, balancesOverZero);

            String message = buildMessage(balancesOverZero, totalBtc, lastPlot, person.getOwnerName(), btcDollar);
            slack.sendUserMessage(person.getSlackUser(), message);
        } catch (Exception e) {
            eventPublisher.publishEvent(new FatalErrorEvent("Balance for " + person.getSlackUser() + "/" + person.getOwnerName() + " could not be fetched or reported"));
        }
    }

    private void suggestChannelsFor(BalanceNotification person) {
        BalancePlot now = balancePlotService.getNextToLast(person.getSlackUser(), person.getOwnerName());

        if (now != null) {
            // all currencies the user has some of
            List<CryptoCurrency> possessingCryptoCurrencies = now.getCurrencies().stream()
                    .map(currencyName -> CryptoCurrency.byShortName(currencyName))
                    .filter(cc -> cc != null)
                    .collect(Collectors.toList());

            List<CryptoCurrency> notPossessingCryptoCurrencies = Arrays.stream(CryptoCurrency.values())
                    .filter(cc -> !possessingCryptoCurrencies.contains(cc))
                    .collect(Collectors.toList());

            // all currencies he has but is no member of the channel
            List<CryptoCurrency> currenciesWhereNotChannelMember = possessingCryptoCurrencies.stream()
                    .filter(possessedCc -> {
                        String channelName = possessedCc.getFullName().toLowerCase();
                        return slack.channelExists(channelName) && !slack.isMemberOfChannel(person.getSlackUser(), channelName);
                    })
                    .collect(Collectors.toList());

            // all currencies he does NOT have but is member of the channel
            List<CryptoCurrency> currenciesWhereMemberButNotPossessing = notPossessingCryptoCurrencies.stream()
                    .filter(cc -> !cc.equals(CryptoCurrency.BTC))
                    .filter(notPossessedCc -> {
                        String channelName = notPossessedCc.getFullName().toLowerCase();
                        return slack.channelExists(channelName) && slack.isMemberOfChannel(person.getSlackUser(), channelName);
                    })
                    .collect(Collectors.toList());

            if (!currenciesWhereNotChannelMember.isEmpty() || !currenciesWhereMemberButNotPossessing.isEmpty()) {
                StringBuilder builder = new StringBuilder("\uD83D\uDCA1 Wie wäre es mit *Channel-Vorschlägen*");

                currenciesWhereNotChannelMember.stream().forEach(cc -> {
                    builder.append("\n&gt; Du besitzt " + cc.name() + ", bist aber nicht in " + slack.getFormattedChannelLink(cc.getFullName().toLowerCase()));
                });

                if (!currenciesWhereMemberButNotPossessing.isEmpty() && !currenciesWhereNotChannelMember.isEmpty()) {
                    builder.append("\n");
                }

                currenciesWhereMemberButNotPossessing.stream().forEach(cc -> {
                    builder.append("\n&gt; Du bist in  " + slack.getFormattedChannelLink(cc.getFullName().toLowerCase()) + ", besitzt aber keine " + cc.name());
                });

                slack.sendUserMessage(person.getSlackUser(), builder.toString());
            }
        }
    }

    private String buildMessage(Map<String, Balance> balances, double totalBtc, BalancePlot lastPlot, String owner, CurrencyPlot btcDollar) {
        long totalDollars = new Double(totalBtc * btcDollar.getPlot().getLast()).longValue();
        Growth btcGrowth = new Growth(0, 0);
        if (lastPlot != null) {
            btcGrowth = new Growth(lastPlot.getBtcValue(), totalBtc);
        }

        StringBuilder builder = new StringBuilder(
                "Das Poloniex Guthaben für *" + owner + "* beträgt *" + String.format("%.3f", totalBtc) + "* BTC oder *" + String.format("%,d", totalDollars) + "* $ (" + btcGrowth.getRoundPercentage() + " % " + btcGrowth.getActionPerformed() + ")"
        );

        balances.entrySet().stream().forEach(balanceEntry -> {
            Balance b = balanceEntry.getValue();
            String currency = balanceEntry.getKey();

            Growth cryptoGrowth = new Growth(0, 0);
            if (lastPlot != null) {
                Double before = lastPlot.getCurrencyBalances().get(currency);
                if (before != null) {
                    cryptoGrowth = new Growth(before, b.getBtcValue());
                }
            }

            builder.append(
                    "\n&gt; " + b.getRoundedAvailable() + " *" + currency + "* + " + b.getRoundedOnOrders() + " on Sale =️ *" + b.getRoundedBtcValue() + " BTC* (" + cryptoGrowth.getRoundPercentage() + " % " + cryptoGrowth.getActionPerformed() + ")"
            );
        });

        builder.append("\nDeposit/Withdraw: https://poloniex.com/balances");

        return builder.toString();
    }

    private Map<String, Balance> getBalancesOverZero(BalanceNotification person) throws Exception {
        Map<String, Balance> balances = balanceService.getBalancesOf(person.getApiKey(), person.getSecretKey());
        return filterOutEmpties(balances);
    }

    private Map<String, Balance> filterOutEmpties(Map<String, Balance> balances) {
        return balances.entrySet().stream()
                .filter(balance -> balance.getValue().getBtcValue() > 0)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    private void persistBalancePlot(BalanceNotification person, double totalBitcoins, Map<String, Balance> currencyBalances) {
        balancePlotService.add(BalancePlot.from(totalBitcoins, person.getSlackUser(), person.getOwnerName(), currencyBalances));
    }
}
