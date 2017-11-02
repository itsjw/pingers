package pingers;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TCPPingerTest {

    @Test
    public void given_accessible_host_when_ping_should_return_success()
            throws IOException, InterruptedException {

        // Arrange
        Pinger pinger = new TCPPinger();

        // Act
        PingResponse response = pinger.getResponse("jasmin.com");

        // Assert
        assertTrue(response.getSuccess());
        assertNotEquals("", response.getResultMessage());
    }

    @Test
    public void given_inaccessible_host_when_ping_should_return_unsuccess()
            throws IOException, InterruptedException {

        // Arrange
        Pinger pinger = new TCPPinger();

        // Act
        PingResponse response = pinger.getResponse("inaccessible");

        // Assert
        assertFalse(response.getSuccess());
        assertNotEquals("", response.getResultMessage());
    }
}
