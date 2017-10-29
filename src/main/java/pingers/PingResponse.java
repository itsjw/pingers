package pingers;

public class PingResponse {
    private boolean success;

    public void setSuccess() {
        this.success = true;
    }

    public void setUnsucess() {
        this.success = false;
    }

    public boolean getSuccess() {
        return this.success;
    }
}
