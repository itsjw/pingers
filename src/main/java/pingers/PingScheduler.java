package pingers;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

public class PingScheduler {

    private static final int INTERVAL = 1000;

    private static final int INITIAL_DELAY = 1000;

    private ScheduledExecutorService executor;

    private PingResponse lastICMPPingResult;

    private Reporter reporter;

    public PingScheduler(Reporter reporter) {
        this.reporter = reporter;
    }

    public PingResponse getLastICMPPingResult() {
        return lastICMPPingResult;
    }

    public void start(String[] hosts) throws IOException {

        startICMP(hosts);
        startTCP();
        startTrace();
    }

    private void startICMP(String[] hosts) throws IOException {
        final Integer delay = ConfigReader.readAsInt("pingDelay");

        executor = Executors.newScheduledThreadPool(10);

        Runnable pingTask = () -> {
            try {
                lastICMPPingResult = new ICMPPinger().ping(hosts[0]);

                if (!lastICMPPingResult.getSuccess()) {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("last_icmp", lastICMPPingResult.getResultMessage());
                    reporter.report(parameters);
                }

                System.out.println(String.format("T:%s, P:%s, %s", Thread.currentThread().getName(), lastICMPPingResult.getSuccess(), LocalTime.now()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        final int period = nonNull(delay) ? delay : INTERVAL;

        executor.scheduleAtFixedRate(pingTask, INITIAL_DELAY, period, TimeUnit.MILLISECONDS);
    }

    private void startTCP() {
    }

    private void startTrace() {

    }

    @Override
    protected void finalize() throws Throwable {

        executor.shutdown();

        super.finalize();
    }
}
