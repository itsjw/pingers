package pingers;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

public class PingScheduler {

    public static final int DELAY_DEFAULT = 1000;

    private ScheduledExecutorService executor;

    private PingResponse lastICMPPingResult;

    public void start() throws IOException {

        startICMP();
        startTCP();
        startTrace();
    }

    private void startICMP() throws IOException {
        final String hosts = ConfigReader.readAsStringArray("hosts")[0];

        final Integer delay = ConfigReader.readAsInt("pingDelay");

        executor = Executors.newScheduledThreadPool(10);

        Runnable pingTask = () -> {
            try {
                lastICMPPingResult = new ICMPPinger().ping(hosts);

                System.out.println(String.format("T:%s, P:%s, %s", Thread.currentThread().getName(), lastICMPPingResult.getSuccess(), LocalTime.now()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        final int period = nonNull(delay) ? delay : DELAY_DEFAULT;

        executor.scheduleAtFixedRate(pingTask, 0, period, TimeUnit.MILLISECONDS);
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
