package pingers;

import org.junit.Test;

import java.io.IOException;

public class PingSchedulerTest {

    @Test
    public void given_list_hosts_when_start_scheduler_should_verify_host() throws IOException, InterruptedException {

        // Arrange
        PingScheduler scheduler = new PingScheduler();

        // Act
        scheduler.start();

        // Assert
        Thread.sleep(2000);
    }
}
