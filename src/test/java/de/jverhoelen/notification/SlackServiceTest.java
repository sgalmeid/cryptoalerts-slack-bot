package de.jverhoelen.notification;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import de.jverhoelen.currency.CryptoCurrency;
import de.jverhoelen.currency.ExchangeCurrency;
import de.jverhoelen.currency.combination.CurrencyCombination;
import de.jverhoelen.currency.combination.CurrencyCombinationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SlackServiceTest {

    private static final String SOME_USER_NAME = "someUser";
    private static final SlackUser SOME_USER = Mockito.mock(SlackUser.class);

    private static final String SOME_CHANNEL_NAME = "someChannel";
    private static final SlackChannel SOME_CHANNEL = Mockito.mock(SlackChannel.class);

    @Mock
    SlackSession session;

    @Mock
    CurrencyCombinationService currencyCombinations;

    @InjectMocks
    SlackService slackService = new SlackService();

    @Before
    public void init() {
        initMocks(this);

        when(session.findUserByUserName(SOME_USER_NAME)).thenReturn(SOME_USER);
        when(session.findChannelByName(SOME_CHANNEL_NAME)).thenReturn(SOME_CHANNEL);
        when(SOME_CHANNEL.getMembers()).thenReturn(Arrays.asList(SOME_USER));
        ReflectionTestUtils.setField(slackService, "environment", "prod");

        when(currencyCombinations.getAll()).thenReturn(
                Arrays.asList(
                        CurrencyCombination.of(CryptoCurrency.BTC, ExchangeCurrency.USDT),
                        CurrencyCombination.of(CryptoCurrency.LTC, ExchangeCurrency.BTC)
                )
        );
    }

    @Test
    public void sendUserMessage() throws Exception {
        slackService.sendUserMessage(SOME_USER_NAME, "someMessage");
        verify(session).findUserByUserName(SOME_USER_NAME);
        verify(session).sendMessageToUser(SOME_USER, "someMessage", null);
    }

    @Test
    public void sendUserMessage_doNotOnDevEnvironment() throws Exception {
        ReflectionTestUtils.setField(slackService, "environment", "dev");
        slackService.sendUserMessage(SOME_USER_NAME, "someMessage");

        verify(session).findUserByUserName(SOME_USER_NAME);
        verify(session, times(0)).sendMessageToUser(SOME_USER, "someMessage", null);
    }

    @Test
    public void sendChannelMessage() throws Exception {
        slackService.sendChannelMessage(SOME_CHANNEL_NAME, "message");

        verify(session).findChannelByName(SOME_CHANNEL_NAME);
        verify(session).sendMessage(SOME_CHANNEL, "message");

    }

    @Test
    public void isMemberOfChannel() throws Exception {
        boolean result = slackService.isMemberOfChannel(SOME_USER_NAME, SOME_CHANNEL_NAME);

        assertTrue(result);
        verify(session).findChannelByName(SOME_CHANNEL_NAME);
        verify(session).findUserByUserName(SOME_USER_NAME);
    }

    @Test(expected = RuntimeException.class)
    public void isMemberOfChannel_userOrChannelNotExisting() throws Exception {
        slackService.isMemberOfChannel("other", "other");
    }

    @Test
    public void channelExists() throws Exception {
        assertTrue(slackService.channelExists(SOME_CHANNEL_NAME));
        assertFalse(slackService.channelExists("someOtherChannel"));
    }

    @Test
    public void getRequiredChannelNames() throws Exception {
        List<String> channelNames = slackService.getRequiredChannelNames();

        assertTrue(channelNames.contains("litecoin"));
        assertTrue(channelNames.contains("bitcoin"));
        assertTrue(channelNames.contains("statistiken"));
        assertTrue(channelNames.contains("tages-statistik"));
        assertTrue(channelNames.contains("bitcoin"));
        assertThat(channelNames.size(), is(4));
    }

    @Test
    public void joinChannels() throws Exception {
        List<String> channelNames = Arrays.asList("bitcoin", "litecoin", "funcoin");
        slackService.joinChannels(channelNames);

        channelNames.stream().forEach(cn -> verify(session).joinChannel(cn));
    }

    @Test
    public void isDevelopmentEnvironment() throws Exception {
        assertFalse(slackService.isDevelopmentEnvironment());

        ReflectionTestUtils.setField(slackService, "environment", "dev");
        assertTrue(slackService.isDevelopmentEnvironment());
    }
}