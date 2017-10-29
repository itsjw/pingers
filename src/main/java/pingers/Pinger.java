package pingers;

import java.io.IOException;

public abstract class Pinger {
    abstract PingResponse ping(String host) throws InterruptedException, IOException;
}
