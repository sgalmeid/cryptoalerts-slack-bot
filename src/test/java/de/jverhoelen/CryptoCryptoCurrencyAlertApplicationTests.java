package de.jverhoelen;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CryptoCurrencyAlertApplication.class})
@ActiveProfiles("test")
public class CryptoCryptoCurrencyAlertApplicationTests {

    @MockBean
    private SlackSession slackSession;

    @Test
    public void contextLoads() {
    }
}
