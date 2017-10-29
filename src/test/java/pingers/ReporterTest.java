package pingers;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class ReporterTest {

    @Test
    public void when_report_message_should_log() throws IOException {

        // Arrange
        HashMap<String, String> message = new HashMap<>();
        message.put("kk", "vvv");

        // Act
        Reporter.report(message);
    }
}
