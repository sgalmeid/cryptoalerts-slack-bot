package de.jverhoelen.balance.plot;

import de.jverhoelen.balance.Balance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
public class BalancePlot {

    @Id
    @GeneratedValue
    private long id;

    private LocalDateTime time;
    private String slackUser;
    private double btcValue;
    private String[] currencies;

    public static BalancePlot from(double totalBtcBalance, String ofSlackUser, Map<String, Balance> currencyBalances) {
        BalancePlot plot = new BalancePlot();

        plot.setTime(LocalDateTime.now());
        plot.setSlackUser(ofSlackUser);
        plot.setBtcValue(totalBtcBalance);
        plot.setCurrencies(currencyBalances.keySet().toArray(new String[0]));

        return plot;
    }

    public String[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(String[] currencies) {
        this.currencies = currencies;
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
