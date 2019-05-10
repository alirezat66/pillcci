package greencode.ir.pillcci.retrofit.reqobject;

public class LoginRequest {
    String username;
    String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int validationCheck() {
        //0 is true
        //1 pass empty
        //2 number empty
        //3 number not valid
        if(this.username.length()>0) {

                if (this.password.length() > 0) {
                    return 0;
                } else {
                    return 1;
                }

        }else {
            return 3;
        }
    }
}
