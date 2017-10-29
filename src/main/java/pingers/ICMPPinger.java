package pingers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringJoiner;

public class ICMPPinger extends Pinger {

    @Override
    PingResponse ping(String host) throws InterruptedException, IOException {

        Process process = new ProcessBuilder("ping", host, "-c 1").start();
        process.waitFor();

        PingResponse response = new PingResponse();

        if (process.exitValue() == 0) {
            response.setSuccess();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
            reader.lines().iterator().forEachRemaining(sj::add);
            response.setResultMessage(sj.toString());

            process.destroy();
        }
        else {
            response.setUnsucess();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
            reader.lines().iterator().forEachRemaining(sj::add);
            response.setResultMessage(sj.toString());

            process.destroy();
        }

        return response;
    }
}
