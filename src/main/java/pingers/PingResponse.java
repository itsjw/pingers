package pingers;

public class PingResponse {
    private boolean success;

    private String resultMessage;

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
}
