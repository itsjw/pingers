package pingers;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

public class PingScheduler {

    private static final int INTERVAL = 1000;

    private static final int INITIAL_DELAY = 1000;

    private ScheduledExecutorService executor;

    private List<PingResponse> lastICMPPingResults;

    private Reporter reporter;

    public PingScheduler(Reporter reporter) {
        this.reporter = reporter;

        executor = Executors.newScheduledThreadPool(10);
    }

    public List<PingResponse> getLastICMPPingResults() {
        return lastICMPPingResults;
    }

    public void start(String[] hosts) throws IOException {

        startICMP(hosts);

        startTCP();

        startTrace();
    }

    private void startICMP(String[] hosts) throws IOException {
        final Integer delay = ConfigReader.readAsInt("pingDelay");

        if (lastICMPPingResults == null) {
            this.lastICMPPingResults = new ArrayList<>();
        }

        for (String host : hosts) {

            Runnable pingTask = () -> {
                try {

                    PingResponse lastResponse = new ICMPPinger().ping(host);
                    lastICMPPingResults.add(lastResponse);

                    if (!lastResponse.getSuccess()) {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("last_icmp", lastResponse.getResultMessage());
                        reporter.report(parameters);
                    }

                    System.out.println(String.format("T:%s, P:%s, %s", Thread.currentThread().getName(), lastResponse.getSuccess(), LocalTime.now()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };


            final int period = nonNull(delay) ? delay : INTERVAL;

            executor.scheduleAtFixedRate(pingTask, INITIAL_DELAY, period, TimeUnit.MILLISECONDS);
        }
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
