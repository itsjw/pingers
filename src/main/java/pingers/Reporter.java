package pingers;

import java.io.IOException;
import java.util.Map;

import static java.util.Objects.nonNull;

public class Reporter {

    private Reporter() {
        throw new IllegalStateException("This is a helper class. It is not necessary instantiated it.");
    }

    public static void report(Map<String, String> messageValues) throws IOException {

        if (messageValues == null)
            return;

        logMessage(messageValues);

        reportMessage(messageValues);
    }

    private static void reportMessage(Map<String, String> messageValues) throws IOException {

        String urlReport = ConfigReader.readAsString("urlHttpReport");

        if (nonNull(urlReport)) {

            messageValues.forEach((key, value) ->
                    System.out.println(String.format("%s: %s", key, value)));
        }
    }

    private static void logMessage(Map<String, String> messageValues) {

        messageValues.forEach((key, value) ->
                System.out.println(String.format("%s: %s", key, value)));
    }
}
