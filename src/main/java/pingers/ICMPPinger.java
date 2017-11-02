package pingers;

import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ICMPPinger extends Pinger {

    public static final String PACKET_LOSS_REGEX = "(\\d+)\\% packet loss";

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    @Override
    PingResponse ping(String host) throws InterruptedException, IOException {

        PingResponse response = new PingResponse();
        response.setHost(host);
        response.setPinger("icmp");

        try {

            Process process = new ProcessBuilder("ping", host, "-c 5").start();
            process.waitFor();

            if (process.exitValue() == 0) {
                response.setSuccess();

                final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                setMessageFromStreamOutput(response, reader);

                if (hasPacketLost(response.getResultMessage())) {
                    response.setUnsucess();
                }
            } else {
                response.setUnsucess();

                final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                setMessageFromStreamOutput(response, reader);
            }

            process.destroy();
        } catch (Exception exception) {

            logger.error(exception);
            response.setUnsucess();
            response.setResultMessage(exception.getClass().getSimpleName() + ": " + exception.getMessage());
        }

        response.setWhenToNow();

        return response;
    }

    public boolean hasPacketLost(String outputtMessage) {

        Pattern pattern = Pattern.compile(PACKET_LOSS_REGEX);
        Matcher matcher = pattern.matcher(outputtMessage);

        if (matcher.find()) {
            String number = matcher.group(1);
            return Integer.parseInt(number) > 0;
        }

        return false;
    }

    private void setMessageFromStreamOutput(PingResponse response, BufferedReader reader) {

        StringJoiner output = new StringJoiner(System.getProperty("line.separator"));
        reader.lines().iterator().forEachRemaining(output::add);
        response.setResultMessage(output.toString());
    }
}
