package greencode.ir.pillcci.presenters;

import android.content.Context;

import greencode.ir.pillcci.Model.POJOModel;
import greencode.ir.pillcci.interfaces.RegisterStepOneInterface;
import greencode.ir.pillcci.objects.LoginResponse;
import greencode.ir.pillcci.objects.RegisterResponse;
import greencode.ir.pillcci.retrofit.reqobject.LoginRequest;
import greencode.ir.pillcci.retrofit.reqobject.SignUpRequest;

/**
 * Created by alireza on 5/11/18.
 */

public class PresenterRegisterStepOne {
    RegisterStepOneInterface myInterface;
    Context context;
    POJOModel model ;
    public PresenterRegisterStepOne(RegisterStepOneInterface myInterface) {
        this.myInterface = myInterface;
    }
    public void checkUser(String userName,String moaref,Context context){

            myInterface.onValidPhone(new SignUpRequest(userName,moaref));

    }

    public void callService(SignUpRequest request) {
        model = new POJOModel(this);
        model.SignUp(request);
    }

    public void responseReady(RegisterResponse resp) {
        myInterface.onSuccessPhone(resp);
    }

    public void errorReady(String message) {
        myInterface.onInvalid(message);
    }

    public void tryToLogin(LoginRequest req) {
        model.Login(req);
    }

    public void responseReady(LoginResponse data) {
        myInterface.onLoginValid(data);
    }

    public void updateToken(String userId, String token) {
        model.updateToken(userId,token);
    }

    public void onTokenInvalidUpdate() {
        myInterface.onUpdateInvalidToken();

    }

    public void onTokenUpdated() {
        myInterface.onUpdateToken();

    }
}
