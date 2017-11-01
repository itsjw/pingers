package pingers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringJoiner;

public class TraceRoutePinger extends Pinger {

    @Override
    PingResponse ping(String host) throws InterruptedException, IOException {
        Process process = new ProcessBuilder("traceroute", host).start();
        process.waitFor();

        PingResponse response = new PingResponse();
        response.setHost(host);
        response.setPinger("trace");

        if (process.exitValue() == 0) {
            response.setSuccess();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            setMessageFromStreamOutput(response, reader);
        }
        else {
            response.setUnsucess();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            setMessageFromStreamOutput(response, reader);
        }

        response.setWhenToNow();

        process.destroy();

        return response;
    }

    private void setMessageFromStreamOutput(PingResponse response, BufferedReader reader) {

        StringJoiner output = new StringJoiner(System.getProperty("line.separator"));
        reader.lines().iterator().forEachRemaining(output::add);
        response.setResultMessage(output.toString());
    }
}
