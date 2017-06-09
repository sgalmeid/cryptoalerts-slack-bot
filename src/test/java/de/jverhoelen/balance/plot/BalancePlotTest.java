package de.jverhoelen.balance.plot;

import com.google.common.collect.ImmutableMap;
import de.jverhoelen.balance.Balance;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BalancePlotTest {

    @Test
    public void from() throws Exception {
        Map<String, Balance> currencyBalances = ImmutableMap.<String, Balance>builder()
                .put("LTC", new Balance(10.0, 2.0, 36.0))
                .put("XRP", new Balance(3.0, 10.0, 70.0))
                .put("ETH", new Balance(40.0, 0.0, 5120.0))
                .build();

        BalancePlot plot = BalancePlot.from(30.0, "johannes", "johannes", currencyBalances);

        assertThat(plot.getBtcValue(), is(30.0));
        assertThat(plot.getCurrencyBalances().get("LTC"), is(36.0));
        assertThat(plot.getCurrencyBalances().get("XRP"), is(70.0));
        assertThat(plot.getCurrencyBalances().get("ETH"), is(5120.0));
        assertThat(plot.getCurrencyBalances().size(), is(3));

        assertTrue(
                plot.getCurrencies().contains("LTC") &&
                        plot.getCurrencies().contains("XRP") &&
                        plot.getCurrencies().contains("ETH")
        );
    }
}