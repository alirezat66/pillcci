package greencode.ir.pillcci.retrofit.reqobject;

/**
 * Created by alireza on 5/12/18.
 */

public class ChangePassReq {
    String user_id;
    String pass;


    public ChangePassReq(String user_id, String pass) {
        this.user_id = user_id;
        this.pass = pass;
    }

    public int checkValidation(String retryPass){

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

    public String getUser_id() {
        return user_id;
    }

    public String getPass() {
        return pass;
    }
}
