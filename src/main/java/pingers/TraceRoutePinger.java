package pingers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringJoiner;

public class TraceRoutePinger extends Pinger {

    @Override
    PingResponse ping(String host) throws InterruptedException, IOException {
        PingResponse response = new PingResponse();

        Process process = null;

        try {
            process = new ProcessBuilder("traceroute", host).start();

            process.waitFor();

            response.setHost(host);
            response.setPinger("trace");

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

        } catch (Exception exception) {

            response.setUnsucess();
            response.setResultMessage(exception.getClass().getSimpleName() + ": " + exception.getMessage());
        }

        response.setWhenToNow();

        return response;
    }

    public boolean endsWithTimeout(String resultMessage) {
        return resultMessage.endsWith("* * *\n");
    }

    private void setMessageFromStreamOutput(PingResponse response, BufferedReader reader) {

        StringJoiner output = new StringJoiner(System.getProperty("line.separator"));
        reader.lines().iterator().forEachRemaining(output::add);
        response.setResultMessage(output.toString());
    }
}
