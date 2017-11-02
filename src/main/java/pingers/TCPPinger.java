package pingers;

import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.util.Objects.nonNull;

public class TCPPinger extends Pinger {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

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
            connection.setConnectTimeout(getTimeout());

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

    private int getTimeout() throws IOException {
        Integer timeout = ConfigReader.readAsInt("tcp.HttpTimeout");

        // 2000 by default
        return nonNull(timeout) ? timeout : 2000;
    }

    private boolean isHttpStatusCodeInErrorRange(Integer httpStatusCode) {
        return httpStatusCode >= 400;
    }

    private String buildURL(String host) {
        return host.startsWith("http://") ? host : String.format("http://%s", host);
    }
}
