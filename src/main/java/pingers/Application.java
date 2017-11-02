package pingers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Application {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String args[]) throws IOException {

        logger.info("Starting app");

        String[] hosts = ConfigReader.readAsStringArray("hosts");

        PingScheduler scheduler = new PingScheduler(new StatusSender());

        scheduler.start(hosts);

        logger.info("App started");
    }
}
