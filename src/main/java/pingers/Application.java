package pingers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    final static Logger logger = Logger.getLogger(Application.class.getSimpleName());

    public static void main(String args[]) throws IOException {

        String[] hosts = ConfigReader.readAsStringArray("hosts");

        PingScheduler scheduler = new PingScheduler(new Reporter());

        scheduler.start(hosts);

        logger.log(Level.INFO, "App started");
    }
}
