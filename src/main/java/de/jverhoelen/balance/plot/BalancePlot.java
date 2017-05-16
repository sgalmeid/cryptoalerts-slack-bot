package de.jverhoelen.balance.plot;

import de.jverhoelen.balance.Balance;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
public class BalancePlot {

    @Id
    @GeneratedValue
    private long id;

    private LocalDateTime time;
    private String slackUser;
    private double btcValue;

    @Column(length = 1000)
    private Map<String, Double> currencyBalances;

    public static BalancePlot from(double totalBtcBalance, String ofSlackUser, Map<String, Balance> currencyBalances) {
        BalancePlot plot = new BalancePlot();

        plot.setTime(LocalDateTime.now());
        plot.setSlackUser(ofSlackUser);
        plot.setBtcValue(totalBtcBalance);
        plot.setCurrencyBalances(currencyBalances.entrySet()
                .stream()
                .map(cb -> new AbstractMap.SimpleEntry<>(cb.getKey(), cb.getValue().getBtcValue()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()))
        );

        return plot;
    }

    public Map<String, Double> getCurrencyBalances() {
        return currencyBalances;
    }

    public void setCurrencyBalances(Map<String, Double> currencyBalances) {
        this.currencyBalances = currencyBalances;
    }

    @Transient
    public List<String> getCurrencies() {
        return new ArrayList<>(currencyBalances.keySet());
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSlackUser() {
        return slackUser;
    }

    public void setSlackUser(String slackUser) {
        this.slackUser = slackUser;
    }

    public double getBtcValue() {
        return btcValue;
    }

    public void setBtcValue(double btcValue) {
        this.btcValue = btcValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BalancePlot that = (BalancePlot) o;

        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        return slackUser != null ? slackUser.equals(that.slackUser) : that.slackUser == null;
    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (slackUser != null ? slackUser.hashCode() : 0);
        return result;
    }
}
