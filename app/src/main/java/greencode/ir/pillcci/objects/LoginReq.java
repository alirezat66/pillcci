package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/11/18.
 */

public class LoginReq {
    String userName;
    String pass;

    public LoginReq(String userName, String pass) {
        this.userName = userName;
        this.pass = pass;
    }
    public int validationCheck(){
        //0 is true
        //1 pass empty
        //2 number empty
        //3 number not valid
        if(this.userName.length()>0) {
            if (this.userName.length() == 11) {
                if (this.pass.length() > 0) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 2;
            }
        }else {
            return 3;
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }
}
