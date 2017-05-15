package de.jverhoelen.interaction;

import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SlackBotCommandTest {

    SlackMessagePosted slackMsg = Mockito.mock(SlackMessagePosted.class);

    @Test
    public void constructor() {
        when(slackMsg.getMessageContent()).thenReturn("sell currency=GOLD,exchange=BTC");

        SlackBotCommand msg = new SlackBotCommand(slackMsg);
        assertThat(msg.getCommand(), is("sell"));
        assertThat(msg.getSubCommand(), is(nullValue()));
        assertThat(msg.getArgument("currency"), is("GOLD"));
        assertThat(msg.getArgument("exchange"), is("BTC"));
        assertThat(msg.getArguments().size(), is(2));
    }

    @Test
    public void constructor_withSubCommand() {
        when(slackMsg.getMessageContent()).thenReturn("buy nice currency=GOLD");

        SlackBotCommand msg = new SlackBotCommand(slackMsg);
        assertThat(msg.getCommand(), is("buy"));
        assertThat(msg.getSubCommand(), is("nice"));
        assertThat(msg.getArgument("currency"), is("GOLD"));
        assertThat(msg.getArguments().size(), is(1));
    }

    @Test
    public void isParamsSection() throws Exception {
        assertTrue(SlackBotCommand.isParamsSection("some=thing"));
        assertFalse(SlackBotCommand.isParamsSection("something"));
    }
}