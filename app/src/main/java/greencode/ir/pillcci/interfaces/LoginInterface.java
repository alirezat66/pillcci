package greencode.ir.pillcci.interfaces;

import greencode.ir.pillcci.objects.LoginReq;
import greencode.ir.pillcci.objects.LoginResponse;

/**
 * Created by alireza on 5/11/18.
 */

public interface LoginInterface {
    public void onErrorLogin(String error);
    public void onSuccessLogin(LoginResponse resp);

    //0 is true
    //1 pass empty
    //2 number empty
    //3 number not valid

    public void onValidUserPass(LoginReq req);
    public void onPassEmpty();
    public void onNumberEmpty();
    public void onNumberWrong();


}
