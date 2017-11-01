package pingers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TCPPinger extends Pinger {

    @Override
    PingResponse ping(String host) throws InterruptedException, IOException {

        PingResponse response = new PingResponse();

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