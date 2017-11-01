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
        PingResponse response = pinger.ping("localhost");

        // Assert
        assertTrue("Success is false", response.getSuccess());
        assertNotEquals("Message is empty", "", response.getResultMessage());
        System.out.println(response.getResultMessage());
    }

    @Test
    public void given_inaccessible_host_when_traceroute_should_return_unsuccess()
            throws IOException, InterruptedException {

        // Arrange
        Pinger pinger = new TraceRoutePinger();

        // Act
        PingResponse response = pinger.ping("inaccessible.com");

        // Assert
        assertFalse("Success is true", response.getSuccess());
        assertNotEquals("Message is empty", "", response.getResultMessage());
        System.out.println(response.getResultMessage());
    }
}
