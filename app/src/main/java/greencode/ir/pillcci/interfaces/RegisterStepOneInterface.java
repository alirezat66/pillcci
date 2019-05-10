package greencode.ir.pillcci.interfaces;

import greencode.ir.pillcci.objects.LoginResponse;
import greencode.ir.pillcci.objects.RegisterResponse;
import greencode.ir.pillcci.retrofit.reqobject.SignUpRequest;

/**
 * Created by alireza on 5/11/18.
 */

public interface RegisterStepOneInterface {
    public void onInvalid();
    public void onInvalid(String message);
    public void onValidPhone(SignUpRequest request);

    void onEmptyUser();

    void onSuccessPhone(RegisterResponse resp);

    void onLoginValid(LoginResponse data);

    void onUpdateInvalidToken();

    void onUpdateToken();
}
