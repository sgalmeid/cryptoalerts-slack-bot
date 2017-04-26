package de.jverhoelen.balance.plot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class BalancePlot {

    @Id
    @GeneratedValue
    private long id;

    private LocalDateTime time;
    private String slackUser;
    private double btcValue;

    public static BalancePlot from(double totalBtcBalance, String ofSlackUser) {
        BalancePlot plot = new BalancePlot();

        plot.setTime(LocalDateTime.now());
        plot.setSlackUser(ofSlackUser);
        plot.setBtcValue(totalBtcBalance);

        return plot;
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
