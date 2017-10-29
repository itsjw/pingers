package pingers;

import java.io.IOException;

public class ICMPPinger extends Pinger {

    @Override
    PingResponse ping(String host) throws InterruptedException, IOException {

        Process process = new ProcessBuilder("ping", host, "-c 1").start();
        process.waitFor();

        PingResponse response = new PingResponse();

        if (process.exitValue() == 0)
            response.setSuccess();

        return response;
    }
}
