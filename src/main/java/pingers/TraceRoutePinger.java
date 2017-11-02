package pingers;

import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringJoiner;

public class TraceRoutePinger extends Pinger {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    @Override
    PingResponse ping(String host, PingResponse response) throws InterruptedException, IOException {

        response.setPinger("trace");

        Process process = new ProcessBuilder("traceroute", host).start();

        process.waitFor();

        if (process.exitValue() == 0) {
            response.setSuccess();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            setMessageFromStreamOutput(response, reader);

            if (endsWithTimeout(response.getResultMessage())) {
                response.setUnsucess();
            }
        } else {
            response.setUnsucess();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            setMessageFromStreamOutput(response, reader);
        }

        process.destroy();

        return response;
    }

    public boolean endsWithTimeout(String resultMessage) {
        return resultMessage.endsWith("* * *");
    }

    private void setMessageFromStreamOutput(PingResponse response, BufferedReader reader) {

        StringJoiner output = new StringJoiner(System.getProperty("line.separator"));
        reader.lines().iterator().forEachRemaining(output::add);
        response.setResultMessage(output.toString());
    }
}
