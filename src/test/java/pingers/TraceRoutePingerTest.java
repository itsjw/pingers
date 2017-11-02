package pingers;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TraceRoutePingerTest {

    @Test
    public void given_accessible_host_when_traceroute_should_return_success()
            throws IOException, InterruptedException {

        // Arrange
        Pinger pinger = new TraceRoutePinger();

        // Act
        PingResponse response = pinger.getResponse("localhost");

        // Assert
        assertTrue("Success is false", response.getSuccess());
        assertNotEquals("Message is empty", "", response.getResultMessage());
    }

    @Test
    public void given_inaccessible_host_when_traceroute_should_return_unsuccess()
            throws IOException, InterruptedException {

        // Arrange
        Pinger pinger = new TraceRoutePinger();

        // Act
        PingResponse response = pinger.getResponse("inaccessible.com");

        // Assert
        assertFalse("Success is true", response.getSuccess());
        assertNotEquals("Message is empty", "", response.getResultMessage());
    }

    @Test
    public void given_result_message_ending_with_stars_when_verify_should_return_true() {

        // Arrange
        TraceRoutePinger pinger = new TraceRoutePinger();

        // Act
        boolean result = pinger.endsWithTimeout("28  aa.bb cc.dd ee.ff\n29  * * *\n30  * * *");

        // Assert
        assertTrue(result);
    }

    @Test
    public void given_result_message_ending_without_stars_when_verify_should_return_false() {

        // Arrange
        TraceRoutePinger pinger = new TraceRoutePinger();

        // Act
        boolean result = pinger.endsWithTimeout("1  localhost (127.0.0.1)  0.073 ms  0.017 ms  0.016 ms");

        // Assert
        assertFalse(result);
    }
}
