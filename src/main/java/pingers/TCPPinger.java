package pingers;

import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TCPPinger extends Pinger {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(StatusSender.class);

    @Override
    PingResponse ping(String host) throws InterruptedException, IOException {

        PingResponse response = new PingResponse();
        response.setHost(host);
        response.setPinger("tcp");

        URL url = new URL(buildURL(host));
        long startTime = System.currentTimeMillis();

        try {

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Integer httpStatusCode = connection.getResponseCode();
            long elapsedTime = System.currentTimeMillis() - startTime;

            response.setSuccess();
            response.setResultMessage(String.format("HTTP Status Code: %s, Elapsed Time: %s", httpStatusCode, elapsedTime));

            if (isHttpStatusCodeInErrorRange(httpStatusCode)) {
                response.setUnsucess();
            }

        } catch (Exception exception) {

            logger.error(exception);
            response.setUnsucess();
            response.setResultMessage(exception.getClass().getSimpleName() + ": " + exception.getMessage());
        }

        return response;
    }

    private boolean isHttpStatusCodeInErrorRange(Integer httpStatusCode) {
        return httpStatusCode >= 400;
    }

    private String buildURL(String host) {
        return host.startsWith("http://") ? host : String.format("http://%s", host);
    }
}
