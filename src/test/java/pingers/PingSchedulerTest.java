package pingers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class PingSchedulerTest {

    @Mock
    StatusSender statusSender;

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
    public void given_started_scheduler_when_ping_failed_should_report_status()
            throws IOException, InterruptedException {

        // Act
        pingScheduler.start(new String[] { "localhosttt" });
        Thread.sleep(1200);

        // Assert
        verify(statusSender, atLeastOnce()).report(argThat(new ReportMapArgumentMatcher()));
    }

    public class ReportMapArgumentMatcher implements ArgumentMatcher<Map<String, String>> {

        @Override
        public boolean matches(Map<String, String> stringStringMap) {
            boolean result = stringStringMap.containsKey("host");
            result &= stringStringMap.get("host").equals("localhosttt");
            result &= stringStringMap.containsKey("last_icmp");
            result &= stringStringMap.containsKey("last_tcp");
            result &= stringStringMap.containsKey("last_trace");
            return result;
        }
    }
}
