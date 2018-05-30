package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/12/18.
 */

public class ChangePassRes {
    boolean isSuccess;
    int id;
    String userName;
    String error;

    public ChangePassRes(boolean isSuccess, int id, String userName, String error) {
        this.isSuccess = isSuccess;
        this.id = id;
        this.userName = userName;
        this.error = error;
    }

    public ChangePassRes(boolean isSuccess, String error) {
        this.isSuccess = isSuccess;
        this.error = error;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getError() {
        return error;
    }
}
