package greencode.ir.pillcci.interfaces;

import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.objects.RegisterResponse;

/**
 * Created by alireza on 5/11/18.
 */

public interface SetPassInterface {
    // 1 valid
    //4 pass empty
    //5 pass wrong
    //6 pass and repass not equal
    public void onPassEmpty();
    public void onPassWrong();
    public void onPassNotMatch();
    public void onValid(RegisterRequest registerRequest);

    void onErrorRegister(String error);

    void onSuccessRegister(RegisterResponse registerResponse);
}
