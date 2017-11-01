package pingers;

import java.time.LocalDateTime;

public class PingResponse {
    private boolean success;

    private String resultMessage;

    private String host;

    private String pinger;

    private LocalDateTime when = LocalDateTime.now();

    public void setSuccess() {
        this.success = true;
    }

    public void setUnsucess() {
        this.success = false;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getResultMessage() {
        return this.resultMessage;
    }

    public void setPinger(String pinger) {
        this.pinger = pinger;
    }

    public String getPinger() {
        return this.pinger;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return  this.host;
    }

    public LocalDateTime getWhen() {
        return when;
    }
}
