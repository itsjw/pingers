package pingers;

import org.apache.logging.log4j.LogManager;

import java.io.IOException;

public abstract class Pinger {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    abstract PingResponse ping(String host, PingResponse response) throws InterruptedException, IOException;

    public PingResponse getResponse(String host) {

        PingResponse response = new PingResponse();
        response.setHost(host);

        try {

            this.ping(host, response);

        } catch (Exception exception) {

            logger.error(exception);
            response.setUnsucess();
            response.setResultMessage(exception.getClass().getSimpleName() + ": " + exception.getMessage());
        }

        response.setWhenToNow();

        return response;
    }
}
