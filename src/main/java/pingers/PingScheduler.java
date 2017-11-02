package pingers;

import org.apache.logging.log4j.LogManager;

import java.io.IOException;
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
    private static final String ICMP = "icmp";
    private static final String TCP = "tcp";
    private static final String TRACE = "trace";

    private ScheduledExecutorService executor;

    private List<PingResponse> lastPingResponses;

    private StatusSender statusSender;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    public PingScheduler(StatusSender statusSender) {
        this.statusSender = statusSender;

        this.lastPingResponses = new ArrayList<>();

        executor = Executors.newScheduledThreadPool(12);
    }

    public List<PingResponse> getLastPingResponses() {
        return lastPingResponses;
    }

    public void start(String[] hosts) throws IOException {

        startPinger(ICMP, hosts);
        startPinger(TCP, hosts);
        startPinger(TRACE, hosts);
    }

    private void startPinger(String pinger, String[] hosts) throws IOException {

        for (String host : hosts) {

            Runnable pingTask = getRunnable(pinger, host);

            Integer delay = getDelay(pinger);

            final int period = nonNull(delay) ? delay : INTERVAL;

            executor.scheduleAtFixedRate(pingTask, INITIAL_DELAY, period, TimeUnit.MILLISECONDS);
        }
    }

    private Integer getDelay(String pinger) throws IOException {
        return ConfigReader.readAsInt(pinger + ".PingDelay");
    }

    private Runnable getRunnable(String pinger, String host) {
        return () -> {
                    try {

                        PingResponse lastResponse = buildPinger(pinger).ping(host);
                        saveResponse(lastResponse);

                        if (!lastResponse.getSuccess()) {
                            sendReport(host);
                        }

                    } catch (InterruptedException e) {
                        logger.error(e);
                    } catch (IOException e) {
                        logger.error(e);
                    }
                };
    }

    private static Pinger buildPinger(String type) {

        if (ICMP.equals(type))
            return  new ICMPPinger();

        if (TCP.equals(type))
            return  new TCPPinger();

        if (TRACE.equals(type))
            return  new TraceRoutePinger();

        return null;
    }

    private void sendReport(String host) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("host", host);
        parameters.put("last_icmp", "");
        parameters.put("last_tcp", "");
        parameters.put("last_trace", "");

        List<PingResponse> lastResultsByHost =
                lastPingResponses
                        .stream()
                        .filter(response -> response.getHost().equals(host))
                        .collect(Collectors.toList());

        for (PingResponse response : lastResultsByHost) {

            if (ICMP.equals(response.getPinger())) {
                parameters.replace("last_icmp", response.getResultMessage());
                continue;
            }

            if (TCP.equals(response.getPinger())) {
                parameters.replace("last_tcp", response.getResultMessage());
                continue;
            }

            if (TRACE.equals(response.getPinger())) {
                parameters.replace("last_trace", response.getResultMessage());
                continue;
            }
        }

        statusSender.report(parameters);
    }

    private void saveResponse(PingResponse lastResponse) {

        lastPingResponses.removeIf(result ->
                result.getPinger().equals(lastResponse.getPinger()) &&
                        result.getHost().equals(lastResponse.getHost()));

        lastPingResponses.add(lastResponse);
    }

    @Override
    protected void finalize() throws Throwable {

        executor.shutdownNow();

        super.finalize();
    }
}
