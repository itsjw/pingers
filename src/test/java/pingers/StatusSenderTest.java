package pingers;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatusSenderTest {

    @Test
    public void when_report_message_should_log() throws IOException {

        // Arrange
        StatusSender statusSender = new StatusSender();

        Map<String, String> message = new HashMap<>();
        message.put("kk", "vvv");

        // Act
        statusSender.report(message);
    }
}
