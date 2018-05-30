package greencode.ir.pillcci.interfaces;

import greencode.ir.pillcci.objects.ChangePassReq;
import greencode.ir.pillcci.objects.ChangePassRes;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.objects.RegisterResponse;

/**
 * Created by alireza on 5/11/18.
 */

public interface ChangePassIterface {
    public void onPassEmpty();
    public void onPassWrong();
    public void onPassNotMatch();
    public void onValid(ChangePassReq registerRequest);

    void onErrorRegister(String error);

    void onSuccessRegister(ChangePassRes changePassRes);
}
