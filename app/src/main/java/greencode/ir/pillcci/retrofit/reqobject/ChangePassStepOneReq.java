package greencode.ir.pillcci.retrofit.reqobject;

/**
 * Created by alireza on 5/12/18.
 */

public class ChangePassStepOneReq {
    String userName;

    public ChangePassStepOneReq(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
