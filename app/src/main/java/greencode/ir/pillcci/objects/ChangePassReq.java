package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/12/18.
 */

public class ChangePassReq {
    String userName;
    String pass;
    String retryPass;
    int id;

    public ChangePassReq(String userName, String pass, String retryPass, int id) {
        this.userName = userName;
        this.pass = pass;
        this.retryPass = retryPass;
        this.id = id;
    }
    public int checkValidation(){

        // 1 valid
        //2 pass empty
        //3 pass wrong
        //4 pass and repass not equal
        if(pass.length()>=6 && pass.equals(retryPass)){
            return 1;
        }else {
            if(pass.length()==0){
                return 2;
            }else if (pass.length()<6) {
                return 3;
            }else {
                return 4;
            }

        }
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }

    public String getRetryPass() {
        return retryPass;
    }

    public int getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setRetryPass(String retryPass) {
        this.retryPass = retryPass;
    }

    public void setId(int id) {
        this.id = id;
    }
}
