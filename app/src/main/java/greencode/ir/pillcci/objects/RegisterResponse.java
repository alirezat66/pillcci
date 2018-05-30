package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/11/18.
 */

public class RegisterResponse {
    boolean isSuccess;
    int id;
    String userName;
    String ValidCode;
    String error;

    public RegisterResponse(boolean isSuccess, int id, String userName, String validCode, String error) {
        this.isSuccess = isSuccess;
        this.id = id;
        this.userName = userName;
        ValidCode = validCode;
        this.error = error;
    }

    public String getUserName() {
        return userName;
    }

    public String getValidCode() {
        return ValidCode;
    }

    public RegisterResponse(boolean isSuccess, String error) {
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
