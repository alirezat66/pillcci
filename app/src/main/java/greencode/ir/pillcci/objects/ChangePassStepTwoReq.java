package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/12/18.
 */

public class ChangePassStepTwoReq {
    int id;
    String pass;
    String userName;

    public ChangePassStepTwoReq(int id, String pass, String userName) {
        this.id = id;
        this.pass = pass;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public String getPass() {
        return pass;
    }

    public String getUserName() {
        return userName;
    }

}
