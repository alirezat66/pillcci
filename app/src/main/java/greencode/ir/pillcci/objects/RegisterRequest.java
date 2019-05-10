package greencode.ir.pillcci.objects;

import greencode.ir.pillcci.utils.ToJsonClass;

/**
 * Created by alireza on 5/11/18.
 */

public class RegisterRequest extends ToJsonClass {
    String userName;
    String pass;
    String retryPass;
    String moarefCode;
    String code;
    int codePhone;
    String phone;

    public void setCodePhone(int codePhone) {
        this.codePhone = codePhone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCodePhone() {
        return codePhone;
    }

    public String getPhone() {
        return phone;
    }

    public RegisterRequest(String userName, String pass, String retryPass, String moarefCode) {
         this.userName = userName;
        this.pass = pass;
        this.retryPass = retryPass;
        this.moarefCode = moarefCode;
    }

    public RegisterRequest() {
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

    public void setMoarefCode(String moarefCode) {
        this.moarefCode = moarefCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
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

    public String getMoarefCode() {
        return moarefCode;
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
}
