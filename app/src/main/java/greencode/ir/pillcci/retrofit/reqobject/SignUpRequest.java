package greencode.ir.pillcci.retrofit.reqobject;

public class SignUpRequest {
    String username;
    String referrer;

    public SignUpRequest(String username, String referrer) {
        this.username = username;
        this.referrer = referrer;
    }

    public SignUpRequest(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getUsername() {
        return username;
    }

    public String getReferrer() {
        return referrer;
    }
}
