package pingers;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class PingSchedulerTest {

    @Test
    public void given_list_hosts_when_start_scheduler_should_fill_last_status()
            throws IOException, InterruptedException {

        // Arrange
        PingScheduler scheduler = new PingScheduler();

        // Act
        scheduler.start(new String[] { "locahost" });

        // Assert
        Thread.sleep(1600);
        assertNotNull(scheduler.getLastICMPPingResult());
    }

    @Test
    public void given_scheduler_started_when_ping_failed_should_report_status() throws IOException {

        // Arrange
        PingScheduler scheduler = new PingScheduler();
        scheduler.start(new String[] { "localhost" });

        // Act


        // Assert
    }
}
