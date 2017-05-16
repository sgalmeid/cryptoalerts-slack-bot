package de.jverhoelen.interaction;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import de.jverhoelen.balance.notification.BalanceNotificationAddedEvent;
import de.jverhoelen.notification.SlackService;
import de.jverhoelen.timeframe.TimeFrame;
import de.jverhoelen.trade.manual.PoloniexTradeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminMessagePostedListenerTest {

    private AdminMessagePostedListener listener;
    private static final String ADMIN_USERNAME = "admin";

    private PoloniexTradeService tradeService = mock(PoloniexTradeService.class);
    private SlackService slackService = Mockito.mock(SlackService.class);
    private ApplicationEventPublisher publisher = Mockito.mock(ApplicationEventPublisher.class);

    @Before
    public void setUp() throws Exception {
        listener = new AdminMessagePostedListener(tradeService, slackService, ADMIN_USERNAME, publisher);
    }

    @Test
    public void onEvent_general() throws Exception {
        SlackSession session = Mockito.mock(SlackSession.class);

        SlackMessagePosted msg = Mockito.mock(SlackMessagePosted.class);
        SlackUser user = Mockito.mock(SlackUser.class);
        SlackChannel channel = Mockito.mock(SlackChannel.class);

        when(msg.getMessageContent()).thenReturn("timeframe add count=2,treshold=1,unit=HOURS");
        when(msg.getSender()).thenReturn(user);
        when(user.getUserName()).thenReturn(ADMIN_USERNAME);
        when(msg.getChannel()).thenReturn(channel);
        when(channel.getName()).thenReturn("general");

        listener.onEvent(msg, session);

        verify(publisher).publishEvent(Mockito.any(BalanceNotificationAddedEvent.class));
        verify(slackService).sendChannelMessage(eq("general"), anyString());
    }

    @Test
    public void onEvent_admin() throws Exception {
        SlackSession session = Mockito.mock(SlackSession.class);

        SlackMessagePosted msg = Mockito.mock(SlackMessagePosted.class);
        SlackUser user = Mockito.mock(SlackUser.class);
        SlackChannel channel = Mockito.mock(SlackChannel.class);

        when(msg.getMessageContent()).thenReturn("adduser username=bert,key=123,secret=not-secret");
        when(msg.getSender()).thenReturn(user);
        when(user.getUserName()).thenReturn(ADMIN_USERNAME);
        when(msg.getChannel()).thenReturn(channel);
        when(channel.getName()).thenReturn("admin");

        listener.onEvent(msg, session);

        verify(publisher).publishEvent(Mockito.any(BalanceNotificationAddedEvent.class));
        verify(slackService).sendChannelMessage(eq("admin"), anyString());
    }

    @Test
    public void timeFrameFromCommand() throws Exception {
        TimeFrame timeFrame = listener.timeFrameFromCommand(new SlackBotCommand("timeframe add count=2,treshold=1,unit=HOURS"));

        assertThat(timeFrame.getUnit(), is(ChronoUnit.HOURS));
        assertThat(timeFrame.getFrame(), is(2));
        assertThat(timeFrame.getGainThreshold(), is(1.0));
        assertThat(timeFrame.getLossThreshold(), is(1.0));
        assertThat(timeFrame.getInMinutes(), is(120));
    }
}