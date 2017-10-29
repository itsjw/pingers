package pingers;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ICMPPingerTest {

    @Test
    public void given_accessible_host_when_ping_should_return_success() throws IOException, InterruptedException {

        // Arrange
        Pinger pinger = new ICMPPinger();

        // Act
        PingResponse response = pinger.ping("localhost");

        // Assert
        assertTrue(response.getSuccess());
        assertNotEquals("", response.getResultMessage());
    }

    @Test
    public void given_inaccessible_host_when_ping_should_return_success() throws IOException, InterruptedException {

        // Arrange
        Pinger pinger = new ICMPPinger();

        // Act
        PingResponse response = pinger.ping("inaccessible");

        // Assert
        assertFalse(response.getSuccess());
        assertNotEquals("", response.getResultMessage());
    }
}
