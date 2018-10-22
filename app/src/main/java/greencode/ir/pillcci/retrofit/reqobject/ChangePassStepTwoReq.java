package greencode.ir.pillcci.retrofit.reqobject;

/**
 * Created by alireza on 5/12/18.
 */

public class ChangePassStepTwoReq {

    String code;
    String userName;

    public ChangePassStepTwoReq(String code, String userName) {
        this.code = code;
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public String getUserName() {
        return userName;
    }
}
