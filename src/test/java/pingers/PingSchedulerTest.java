package pingers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;

public class PingSchedulerTest {

    @Mock
    Reporter reporter;

    @InjectMocks
    PingScheduler pingScheduler;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void given_list_hosts_when_start_scheduler_should_fill_last_status()
            throws IOException, InterruptedException {

        // Act
        pingScheduler.start(new String[] { "localhost", "172.0.0.1" });
        Thread.sleep(1100);

        // Assert
        assertNotNull("Status is null", pingScheduler.getLastPingResponses());
    }

    @Test
    public void given_scheduler_started_when_ping_failed_should_report_status()
            throws IOException, InterruptedException {

        // Act
        pingScheduler.start(new String[] { "localhosttt" });
        Thread.sleep(1100);

        // Assert
        verify(reporter).report(anyMap());
    }
}
