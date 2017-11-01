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
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class PingScheduler {

    private static final int INTERVAL = 1000;

    private static final int INITIAL_DELAY = 1000;

    private ScheduledExecutorService executor;

    private List<PingResponse> lastPingResponses;

    private Reporter reporter;

    public PingScheduler(Reporter reporter) {
        this.reporter = reporter;

        this.lastPingResponses = new ArrayList<>();

        executor = Executors.newScheduledThreadPool(10);
    }

    public List<PingResponse> getLastPingResponses() {
        return lastPingResponses;
    }

    public void start(String[] hosts) throws IOException {

        startICMP(hosts);

        startTCP();

        startTrace();
    }

    private void startICMP(String[] hosts) throws IOException {
        final Integer delay = ConfigReader.readAsInt("pingDelay");

        for (String host : hosts) {

            Runnable pingTask = () -> {
                try {

                    PingResponse lastResponse = new ICMPPinger().ping(host);
                    saveResponse(lastResponse);

                    if (!lastResponse.getSuccess()) {
                        sendReport(host);
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

    private void sendReport(String host) throws IOException {
        Map<String, String> parameters = new HashMap<>();

        List<PingResponse> lastResultsByHost =
                lastPingResponses
                        .stream()
                        .filter(response -> response.getHost().equals(host))
                        .collect(Collectors.toList());

        for (PingResponse response : lastResultsByHost) {
            parameters.put("host", host);

            if ("icmp".equals(response.getPinger())) {
                parameters.put("last_icmp", response.getResultMessage());
                continue;
            }

            if ("tcp".equals(response.getPinger())) {
                parameters.put("last_tcp", response.getResultMessage());
                continue;
            }

            if ("trace".equals(response.getPinger())) {
                parameters.put("last_trac", response.getResultMessage());
                continue;
            }
        }

        reporter.report(parameters);
    }

    private void saveResponse(PingResponse lastResponse) {

        lastPingResponses.removeIf(result ->
                result.getPinger().equals(lastResponse.getPinger()) &&
                        result.getHost().equals(lastResponse.getHost()));

        lastPingResponses.add(lastResponse);
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
