package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/12/18.
 */

public class ChangePassStepOneRes {
    boolean isSuccess;
    int id;
    String error;

    public ChangePassStepOneRes(boolean isSuccess, int id, String error) {
        this.isSuccess = isSuccess;
        this.id = id;
        this.error = error;
    }

    public ChangePassStepOneRes(boolean isSuccess, String error) {
        this.isSuccess = isSuccess;
        this.error = error;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getId() {
        return id;
    }

    public String getError() {
        return error;
    }
}
