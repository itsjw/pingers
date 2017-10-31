package pingers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static java.util.Objects.nonNull;

public class Reporter {

    public void report(Map<String, String> messageValues) throws IOException {

        if (messageValues == null)
            return;

        logMessage(messageValues);

        reportMessage(messageValues);
    }

    private void reportMessage(Map<String, String> messageValues) throws IOException {

        String urlReport = ConfigReader.readAsString("urlHttpReport");

        if (nonNull(urlReport)) {
            HttpURLConnection httpConnection = buildHttpConnection(urlReport);

            fillBodyRequest(messageValues, httpConnection);

            httpConnection.getResponseCode();
        }
    }

    private static void fillBodyRequest(Map<String, String> messageValues, HttpURLConnection httpConnection) throws IOException {
        httpConnection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(httpConnection.getOutputStream());
        outputStream.writeBytes(convertToJson(messageValues));
        outputStream.flush();
        outputStream.close();
    }

    private static HttpURLConnection buildHttpConnection(String urlReport) throws IOException {
        URL url = new URL(urlReport);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("GET");
        return connection;
    }

    private static void logMessage(Map<String, String> messageValues) {

        messageValues.forEach((key, value) ->
                System.out.println(String.format("%s: %s", key, value)));
    }

    private static String convertToJson(Map<String, String> params) {

        StringBuilder json = new StringBuilder();

        json.append("{");

        params.forEach((key, value) ->
            json.append(String.format("\"%s\":\"%s\",", key, value))
        );

        // deleting last comma
        json.deleteCharAt(json.length() - 1);

        json.append("}");

        return json.toString();
    }
}
